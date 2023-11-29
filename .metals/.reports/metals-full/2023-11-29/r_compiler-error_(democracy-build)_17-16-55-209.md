jar:file://<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-sbt/main_2.12/1.8.2/main_2.12-1.8.2-sources.jar!/sbt/BuildSyntax.scala
### scala.reflect.internal.FatalError: no context found for source-jar:file://<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-sbt/main_2.12/1.8.2/main_2.12-1.8.2-sources.jar!/sbt/BuildSyntax.scala,line-3,offset=20

occurred in the presentation compiler.

action parameters:
offset: 20
uri: jar:file://<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-sbt/main_2.12/1.8.2/main_2.12-1.8.2-sources.jar!/sbt/BuildSyntax.scala
text:
```scala
/*
 * sbt
 * Copyrig@@ht 2011 - 2018, Lightbend, Inc.
 * Copyright 2008 - 2010, Mark Harrah
 * Licensed under Apache License 2.0 (see LICENSE)
 */

package sbt

import sbt.internal.DslEntry
import sbt.librarymanagement.Configuration

private[sbt] trait BuildSyntax {
  import scala.language.experimental.macros
  def settingKey[T](description: String): SettingKey[T] = macro std.KeyMacro.settingKeyImpl[T]
  def taskKey[T](description: String): TaskKey[T] = macro std.KeyMacro.taskKeyImpl[T]
  def inputKey[T](description: String): InputKey[T] = macro std.KeyMacro.inputKeyImpl[T]

  def enablePlugins(ps: AutoPlugin*): DslEntry = DslEntry.DslEnablePlugins(ps)
  def disablePlugins(ps: AutoPlugin*): DslEntry = DslEntry.DslDisablePlugins(ps)
  def configs(cs: Configuration*): DslEntry = DslEntry.DslConfigs(cs)
  def dependsOn(deps: ClasspathDep[ProjectReference]*): DslEntry = DslEntry.DslDependsOn(deps)
  // avoid conflict with `sbt.Keys.aggregate`
  def aggregateProjects(refs: ProjectReference*): DslEntry = DslEntry.DslAggregate(refs)

  implicit def sbtStateToUpperStateOps(s: State): UpperStateOps =
    new UpperStateOps.UpperStateOpsImpl(s)
}
private[sbt] object BuildSyntax extends BuildSyntax

```



#### Error stacktrace:

```
scala.tools.nsc.interactive.CompilerControl.$anonfun$doLocateContext$1(CompilerControl.scala:100)
	scala.tools.nsc.interactive.CompilerControl.doLocateContext(CompilerControl.scala:100)
	scala.tools.nsc.interactive.CompilerControl.doLocateContext$(CompilerControl.scala:99)
	scala.tools.nsc.interactive.Global.doLocateContext(Global.scala:113)
	scala.meta.internal.pc.PcDefinitionProvider.definitionTypedTreeAt(PcDefinitionProvider.scala:151)
	scala.meta.internal.pc.PcDefinitionProvider.definition(PcDefinitionProvider.scala:68)
	scala.meta.internal.pc.PcDefinitionProvider.definition(PcDefinitionProvider.scala:16)
	scala.meta.internal.pc.ScalaPresentationCompiler.$anonfun$definition$1(ScalaPresentationCompiler.scala:321)
```
#### Short summary: 

scala.reflect.internal.FatalError: no context found for source-jar:file://<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-sbt/main_2.12/1.8.2/main_2.12-1.8.2-sources.jar!/sbt/BuildSyntax.scala,line-3,offset=20