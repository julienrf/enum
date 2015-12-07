pomExtra in ThisBuild := (
    <url>http://github.com/julienrf/enum</url>
    <licenses>
    <license>
        <name>MIT License</name>
        <url>http://opensource.org/licenses/mit-license.php</url>
    </license>
    </licenses>
    <scm>
    <url>git@github.com:julienrf/enums.git</url>
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

import ReleaseTransformations._

releaseProcess in ThisBuild := Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runClean,
    runTest,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    ReleaseStep(action = Command.process("publishSigned", _)),
    ReleaseStep(action = Command.process("publishDoc", _)),
    setNextVersion,
    commitNextVersion,
    ReleaseStep(action = Command.process("sonatypeReleaseAll", _)),
    pushChanges
)