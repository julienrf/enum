name := "enums"

organization in ThisBuild := "org.julienrf"

scalaVersion in ThisBuild := "2.12.1"

crossScalaVersions := Seq("2.10.6", "2.11.9", scalaVersion.value)

scalacOptions in ThisBuild ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-unchecked",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xlint",
  "-Xfuture"
)

lazy val shapelessAndTestDeps = Def.setting(Seq(
  "com.chuusai" %%% "shapeless" % "2.3.2",
  "org.scalacheck" %%% "scalacheck" % "1.13.4" % Test,
  "org.scalatest" %%% "scalatest" % "3.0.1" % Test
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
  crossProject.crossType(CrossType.Pure)
    .in(file("values"))
    .settings(commonSettings ++ publishSettings: _*)
    .settings(
      name := "enum-values",
      apiURL := Some(url(s"http://julienrf.github.io/enum-values/${version.value}/api/"))
    )

val valuesJS = values.js
val valuesJVM = values.jvm

val labels =
  crossProject.crossType(CrossType.Pure)
    .in(file("labels"))
    .settings(commonSettings ++ publishSettings: _*)
    .settings(
      name := "enum-labels",
      apiURL := Some(url(s"http://julienrf.github.io/enum-labels/${version.value}/api/"))
    )

val labelsJS = labels.js
val labelsJVM = labels.jvm

val enum =
  crossProject.crossType(CrossType.Pure)
    .in(file("enum"))
    .settings(commonSettings ++ publishSettings: _*)
    .settings(
      name := "enum",
      apiURL := Some(url(s"http://julienrf.github.io/enum/${version.value}/api/"))
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
        ReleaseStep(action = Command.process("publishDoc", _)),
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
      case Some((2, n)) if n >= 11 => Seq("-Ywarn-unused-import")

    }
  },
  scalacOptions in (Compile, console) ~= {_.filterNot("-Ywarn-unused-import" == _)},
  scalacOptions in (Test, console) <<= (scalacOptions in (Compile, console))
)

def macroParadise(v: String): Seq[ModuleID] =
  if (v.startsWith("2.10")) Seq(compilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full))
  else Seq.empty

val publishDoc = taskKey[Unit]("Publish API documentation for all artifacts")

publishDoc := {
  IO.copyDirectory((doc in (valuesJVM, Compile)).value, Path.userHome / "sites" / "julienrf.github.com" / "enum-values" / version.value / "api")
  IO.copyDirectory((doc in (labelsJVM, Compile)).value, Path.userHome / "sites" / "julienrf.github.com" / "enum-labels" / version.value / "api")
  IO.copyDirectory((doc in (enumJVM, Compile)).value, Path.userHome / "sites" / "julienrf.github.com" / "enum" / version.value / "api")
}
