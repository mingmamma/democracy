// Declare a lower-case package name that matches the directory containing the source code file
// https://docs.scala-lang.org/tour/packages-and-imports.html#creating-a-package
package democracy

/**
 * A grade to assign to a candidate. There are seven possible grades (from
 * the worst to the best): `Bad`, `Mediocre`, `Inadequate`, `Passable`, `Good`,
 * `VeryGood`, and `Excellent`.
 *
 * Grades can be compared by using their `ordinal` method:
 *
 * {{{
 *   Grade.Mediocre.ordinal < Grade.Good.ordinal
 * }}}
 */
// Defining a simple, constant-like enum
// https://docs.scala-lang.org/scala3/book/domain-modeling-tools.html#Enums_Scala_3_only
enum Grade:
  case Bad, Mediocre, Inadequate, Passable, Good, VeryGood, Excellent

object Grade:
  /**
   * @return The median grade of a collection of grades.
   *
   * The median grade can be computed by sorting the collection
   * and taking the element in the middle. If there are an even
   * number of grades, any of the two grades that are just before
   * or after the middle of the sequence are correct median values.
   *
   * Grades can be compared by using their `ordinal` method.
   *
   * Hints: use the following operations:
   * - `sortBy` and `ordinal` to sort the collection of grades,
   * - `size` to compute the number of elements,
   * - `apply` to select an element at a specific index.
   */
  def median(grades: Seq[Grade]): Grade =
    // available ops of Seq trait: https://docs.scala-lang.org/overviews/collections-2.13/seqs.html#operations-in-class-seq
    grades
      // noting that the usage of enum variants usually requires
      // the enums to well-sorted in their declaration, as in this
      // example and another example where ordinal of 4 direction
      // variants can be used to derive other directions correctly 
      .sortBy(grade => grade.ordinal)
      .apply(grades.size/2)
      // .apply(grades.length/2)
end Grade

/**
 * A candidate in an election.
 * @param name (unique) name of the candidate (e.g., “Barack Obama”)
 */
case class Candidate(name: String)

/**
 * A ballot, which assigns a grade to each candidate of an election.
 * @param grades The grades assigned to each candidate
 */
case class Ballot(grades: Map[Candidate, Grade])

/**
 * An election is defined by a simple description and a set of possible
 * candidates.
 * @param description  Description of the election (e.g., “Presidential Election”)
 * @param candidates Possible candidates
 */
case class Election(description: String, candidates: Set[Candidate]):
  /**
   * @return The candidate that wins this election, according to the Majority
   *         Judgement voting process.
   *
   * @param ballots The ballots for this election
   *
   * The ballots ''must'' assign a grade to each of the `candidates` of this
   * election.
   */
  def elect(ballots: Seq[Ballot]): Candidate =
    assert(ballots.nonEmpty)
    assert(ballots.forall(ballot => ballot.grades.keySet == candidates))

    // Re-structure the data to get all the grades assigned to
    // each candidate by all the voters

    // First step: use the operation `flatMap` to flatten the ballots
    // into a sequence of all (Candidate, Grade) pairs found in the ballots
    // Noting that using flatMap on the Seq collection ballots returns a Seq collection
    // https://docs.scala-lang.org/overviews/collections-2.13/trait-iterable.html#operations-in-class-iterable
    val allGrades: Seq[(Candidate, Grade)] =
      ballots.flatMap(ballot => ballot.grades)

    // Second step: use the operation `groupMap` to transform the Seq
    // collection of pairs of `(Candidate, Grade)` into a `Map`
    // containing all the grades that were assigned to a given
    // `Candidate`.
    val gradesPerCandidate: Map[Candidate, Seq[Grade]] =
      allGrades.groupMap(candidateGradePair => candidateGradePair._1)(candidateGradePair => candidateGradePair._2)
      // less efficient but equivalent approach by chaining
      // groupBy and map ops
      // allGrades.groupBy(candidateGradePair => 
      //   candidateGradePair._1).map(candidateWithSeq => 
      //     (candidateWithSeq._1, candidateWithSeq._2.map(candidateGradePair => 
      //       candidateGradePair._2)))

    findWinner(gradesPerCandidate)
  end elect

  /**
   * @return The winner of this election, according to the Majority Judgement
   *         voting process.
   *
   * @param gradesPerCandidate The grades that have been assigned to each
   *                             candidate by the voters.
   */
  def findWinner(gradesPerCandidate: Map[Candidate, Seq[Grade]]): Candidate =
    // In case all the candidates have an empty collection of grades (this
    // can happen because of the tie-breaking algorithm, see below), the winner
    // is chosen by lottery from among the candidates.
    if gradesPerCandidate.forall((candidate, grades) => grades.isEmpty) then
      val candidatesSeq = gradesPerCandidate.keys.toSeq
      // get a random num between 0 (inclusive) and candidatesSeq.size (exclusive)
      // as the index to select a candidate at random
      val randomIndex   = util.Random.between(0, candidatesSeq.size)
      candidatesSeq(randomIndex)
    else
      // Otherwise, find the highest median grade assigned to a candidate.
      // Use the operation `values` to select the collections of grades,
      // then use the operation `filter` to keep only the non empty grades,
      // then use the operation `map` to compute the median value of each collection
      // of grades, and finally use the operation `maxBy` to find the highest
      // median grade.
      val bestMedianGrade: Grade =
        gradesPerCandidate
          .values
          .filter(grades => grades.nonEmpty == true)
          .map(grades => Grade.median(grades))
          .maxBy(medianGrades => medianGrades.ordinal)

      // Use the operation `filter` to select all the candidates that got the
      // same best median grade (as the case may be)
      val bestCandidates: Map[Candidate, Seq[Grade]] =
        gradesPerCandidate
          .filter((candidate, gradesOfCandidate) => Grade.median(gradesOfCandidate) == bestMedianGrade)

      // In case only one candidate got the best median grade, it’s the winner!
      if bestCandidates.size == 1 then
        // Use the operation `head` to retrieve the only element
        // of the collection `bestCandidates`
        bestCandidates.head._1
      else
        // Otherwise, there is a tie between several candidates. The tie-breaking
        // algorithm is the following:
        // “If more than one candidate has the same highest median-grade, the winner is
        // discovered by removing (one-by-one) any grades equal in value to the shared
        // median grade from each tied candidate's total. This is repeated until only one
        // of the previously tied candidates is currently found to have the highest
        // median-grade.” (source: https://en.wikipedia.org/wiki/Majority_judgment)
  
        // Use the operation `map` to transform each element of the `bestCandidates`.
        // And use the operation `diff` to remove one `bestMedianGrade` from the
        // grades assigned to the candidates.
        val bestCandidatesMinusOneMedianGrade: Map[Candidate, Seq[Grade]] =
          bestCandidates.map((candidate, gradesOfCandidate) => 
            (candidate, gradesOfCandidate.diff(Seq(bestMedianGrade))))
  
        // Finally, call `findWinner` on the reduced collection of candidates,
        // `bestCandidatesMinusOneMedianGrade`.
        findWinner(bestCandidatesMinusOneMedianGrade)
  end findWinner

end Election
