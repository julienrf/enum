import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

name := "enums"

organization in ThisBuild := "org.julienrf"

scalaVersion in ThisBuild := "2.13.1"

crossScalaVersions := Seq("2.11.8", "2.12.8", scalaVersion.value)

scalacOptions in ThisBuild ++= {
  val oldOptions = CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, n)) if n < 13 =>
      Seq("-Yno-adapted-args", "-Xfuture")
    case Some((2, _)) => Seq()
  }

  Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-unchecked",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-Xlint"
  ) ++ oldOptions
}

lazy val shapelessAndTestDeps = Def.setting(Seq(
  "com.chuusai" %%% "shapeless" % "2.3.3",
  "org.scalacheck" %%% "scalacheck" % "1.14.3" % Test,
  "org.scalatest" %%% "scalatest" % "3.1.1" % Test
))

lazy val commonSettings = Seq(
  libraryDependencies ++= macroParadise(scalaVersion.value) ++ shapelessAndTestDeps.value
) ++ warnUnusedImport

import ReleaseTransformations._

val publishSettings = Seq(
  scalacOptions in (Compile, doc) ++= Seq(
    "-doc-source-url", s"https://github.com/julienrf/enum/tree/v${version.value}€{FILE_PATH}.scala",
    "-sourcepath", baseDirectory.in(LocalRootProject).value.getAbsolutePath
  ),
  releasePublishArtifactsAction := PgpKeys.publishSigned.value,
  pomExtra := (
    <url>http://github.com/julienrf/enum</url>
      <licenses>
        <license>
          <name>MIT License</name>
          <url>http://opensource.org/licenses/mit-license.php</url>
        </license>
      </licenses>
      <scm>
        <url>git@github.com:julienrf/enum.git</url>
        <connection>scm:git:git@github.com:julienrf/enum.git</connection>
      </scm>
      <developers>
        <developer>
          <id>julienrf</id>
          <name>Julien Richard-Foy</name>
          <url>http://julien.richard-foy.fr</url>
        </developer>
      </developers>
    )
)

val values =
  crossProject(JSPlatform, JVMPlatform)
    .crossType(CrossType.Pure)
    .in(file("values"))
    .settings(commonSettings ++ publishSettings: _*)
    .settings(
      name := "enum-values"
    )

val valuesJS = values.js
val valuesJVM = values.jvm

val labels =
  crossProject(JSPlatform, JVMPlatform)
    .crossType(CrossType.Pure)
    .in(file("labels"))
    .settings(commonSettings ++ publishSettings: _*)
    .settings(
      name := "enum-labels"
    )

val labelsJS = labels.js
val labelsJVM = labels.jvm

val enum =
  crossProject(JSPlatform, JVMPlatform)
    .crossType(CrossType.Pure)
    .in(file("enum"))
    .settings(commonSettings ++ publishSettings: _*)
    .settings(
      name := "enum"
    ).dependsOn(values)

val enumJS = enum.js
val enumJVM = enum.jvm

val enums =
  project.in(file("."))
    .settings(
      publishArtifact := false,
      releaseCrossBuild := true,
      releaseProcess := Seq[ReleaseStep](
        checkSnapshotDependencies,
        inquireVersions,
        runClean,
        runTest,
        setReleaseVersion,
        commitReleaseVersion,
        tagRelease,
        publishArtifacts,
        setNextVersion,
        commitNextVersion,
        ReleaseStep(action = Command.process("sonatypeReleaseAll", _)),
        pushChanges
      )
    )
    .aggregate(valuesJS, valuesJVM, labelsJS, labelsJVM, enumJS, enumJVM)

lazy val warnUnusedImport = Seq(
  scalacOptions ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, 10)) => Seq()
      case Some((2, n)) if n >= 13 => Seq("-Ywarn-unused:imports")
      case Some((2, n)) if n >= 11 => Seq("-Ywarn-unused-import")
    }
  },
  scalacOptions in (Compile, console) ~= {
    _.filterNot(value => value == "-Ywarn-unused-import" || value == "-Ywarn-unused:imports")
  },
  scalacOptions in (Test, console) := (scalacOptions in (Compile, console)).value
)

def macroParadise(v: String): Seq[ModuleID] =
  if (v.startsWith("2.10")) Seq(compilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full))
  else Seq.empty
