Map("a" -> List(11,111), "b" -> List(22,222)).flatMap((key,value)=>value)

Map("a" -> List(11,111), "b" -> List(22,222)).flatMap(_._2)


case class Ballot(grades: Map[String, String])
val bal1 = Ballot(Map("Can1" -> "zero", "Can2" -> "one"))
val bal2 = Ballot(Map("Can1" -> "zero", "Can2" -> "one"))
val bals = List(bal1,bal2)
bals.flatMap(ballot => ballot.grades)
