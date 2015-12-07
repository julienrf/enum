name := "enums"

organization := "org.julienrf"

scalaVersion in ThisBuild := "2.11.7"

scalacOptions in ThisBuild ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-unchecked",
  "-Yinline-warnings",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-unused-import",
  "-Ywarn-value-discard",
  "-Xlint",
  "-Xfuture"
)

lazy val shapelessAndTestDeps = Def.setting(Seq(
  "com.chuusai" %%% "shapeless" % "2.2.5",
  "org.scalacheck" %%% "scalacheck" % "1.12.5" % Test,
  "org.scalatest" %%% "scalatest" % "3.0.0-M14" % Test
))

val values =
  crossProject.crossType(CrossType.Pure)
    .in(file("values"))
    .settings(
      name := "enum-values",
      libraryDependencies ++= shapelessAndTestDeps.value
    )

val valuesJS = values.js

val valuesJVM = values.jvm

val labels =
  crossProject.crossType(CrossType.Pure)
    .in(file("labels"))
    .settings(
      name := "enum-labels",
      libraryDependencies ++= shapelessAndTestDeps.value
    )

val labelsJS = labels.js

val labelsJVM = labels.jvm

val enum =
  project.in(file("."))
    .settings(
      publishArtifact := false
    )
    .aggregate(valuesJS, valuesJVM, labelsJS, labelsJVM)
