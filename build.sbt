name := "enums"

organization in ThisBuild := "org.julienrf"

scalaVersion in ThisBuild := "2.11.8"

crossScalaVersions := Seq("2.10.6", scalaVersion.value)

scalacOptions in ThisBuild ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-unchecked",
  "-Yinline-warnings",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xlint",
  "-Xfuture"
)

lazy val shapelessAndTestDeps = Def.setting(Seq(
  "com.chuusai" %%% "shapeless" % "2.3.0",
  "org.scalacheck" %%% "scalacheck" % "1.12.5" % Test,
  "org.scalatest" %%% "scalatest" % "3.0.0-M14" % Test
))

lazy val commonSettings = Seq(
  libraryDependencies ++= macroParadise(scalaVersion.value) ++ shapelessAndTestDeps.value
) ++ warnUnusedImport

val values =
  crossProject.crossType(CrossType.Pure)
    .in(file("values"))
    .settings(commonSettings: _*)
    .settings(
      name := "enum-values"
    )

val valuesJS = values.js
val valuesJVM = values.jvm

val labels =
  crossProject.crossType(CrossType.Pure)
    .in(file("labels"))
    .settings(commonSettings: _*)
    .settings(
      name := "enum-labels"
    )

val labelsJS = labels.js
val labelsJVM = labels.jvm

val enum =
  crossProject.crossType(CrossType.Pure)
    .in(file("enum"))
    .settings(commonSettings: _*)
    .settings(
      name := "enum"
    ).dependsOn(values)

val enumJS = enum.js
val enumJVM = enum.jvm

val enums =
  project.in(file("."))
    .settings(
      publishArtifact := false
    )
    .aggregate(valuesJS, valuesJVM, labelsJS, labelsJVM, enumJS, enumJVM)

lazy val warnUnusedImport = Seq(
  scalacOptions ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, 10)) => Seq()
      case Some((2, n)) if n >= 11 => Seq("-Ywarn-unused-import")

    }
  },
  scalacOptions in (Compile, console) ~= {_.filterNot("-Ywarn-unused-import" == _)},
  scalacOptions in (Test, console) <<= (scalacOptions in (Compile, console))
)

def macroParadise(v: String): Seq[ModuleID] =
  if (v.startsWith("2.10")) Seq(compilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full))
  else Seq.empty
