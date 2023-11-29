jar:file://<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-sbt/main_2.12/1.8.2/main_2.12-1.8.2-sources.jar!/sbt/BuildSyntax.scala
### scala.reflect.internal.Types$TypeError: illegal cyclic reference involving object Predef

occurred in the presentation compiler.

action parameters:
uri: jar:file://<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-sbt/main_2.12/1.8.2/main_2.12-1.8.2-sources.jar!/sbt/BuildSyntax.scala
text:
```scala
/*
 * sbt
 * Copyright 2011 - 2018, Lightbend, Inc.
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

```
#### Short summary: 

scala.reflect.internal.Types$TypeError: illegal cyclic reference involving object Predef