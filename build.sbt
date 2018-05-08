import sbtassembly.AssemblyPlugin.autoImport.ShadeRule

import scala.language.postfixOps

name := "sqs_playground"

shellPrompt in ThisBuild := { state => s"${Project.extract(state).currentRef.project}-${version.value}> " }

scalacOptions in ThisBuild ++= Seq("-unchecked", "-deprecation")

lazy val credentialsVal =
  sys.env.get("SEMAPHORE").orNull match {
    case "true" => Credentials(Path.userHome / "ivy_credentials")
    case _ => Credentials(Path.userHome / ".ivy2" / ".credentials")
  }

packagedArtifacts := Map.empty

lazy val commonSettings = Seq(
  updateOptions := updateOptions.value.withCachedResolution(true),

  publishTo := {
    val nexus = "https://videoamp.jfrog.io/videoamp/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "snapshot")
    else
      Some("releases" at nexus + "release")
  },

  mainClass in Compile := Some("com.videoamp.sqs_playground.SQSApp"),

  testFrameworks += new TestFramework("org.scalameter.ScalaMeterFramework"),

  publishArtifact in Test := false,

  parallelExecution in Test := false,
  fork in Test := true,

  organization := "com.videoamp",
  scalaVersion := "2.11.11",

  credentials += credentialsVal,

  resolvers += "vamp release repo" at "https://videoamp.jfrog.io/videoamp/release/",
  resolvers += "vamp snapshot repo" at "https://videoamp.jfrog.io/videoamp/snapshot/",
  resolvers += "vamp test repo" at "https://videoamp.jfrog.io/videoamp/test",
  resolvers += Resolver.sonatypeRepo("public"),
  resolvers += "clojars.org" at "https://clojars.org/repo/",
  resolvers += "central" at "http://repo1.maven.org/maven2",
  resolvers += Resolver.jcenterRepo,

  javaOptions += "-Djava.library.path=" + baseDirectory.value / "lib"
)

assemblyShadeRules in assembly := Seq(
  ShadeRule.rename("com.amazonaws.auth.**" -> "shaded.com.amazonaws.auth.@1")
    .inLibrary("com.amazonaws" % "aws-java-sdk-core" % "1.11.321")
    .inProject,
  ShadeRule.rename("com.amazonaws.**" -> "shaded.com.amazonaws.@1")
    .inLibrary("com.amazonaws" % "aws-java-sdk-core" % "1.11.321")
    .inProject,
  ShadeRule.rename("com.amazonaws.services.sqs.**" -> "shaded.com.amazonaws.services.sqs.@1")
    .inLibrary("com.amazonaws" % "aws-java-sdk-sqs" % "1.11.321")
    .inProject,
  ShadeRule.rename("com.amazonaws.handlers.**" -> "shaded.com.amazonaws.handlers.@1")
    .inLibrary("com.amazonaws" % "aws-java-sdk-core" % "1.11.321")
    .inProject,
  ShadeRule.rename("com.amazonaws.client.builder.**" -> "shaded.com.amazonaws.client.builder.@1")
    .inLibrary("com.amazonaws" % "aws-java-sdk-core" % "1.11.321")
    .inProject,
  ShadeRule.rename("com.amazonaws.util.**" -> "shaded.com.amazonaws.util.@1")
    .inLibrary("com.amazonaws" % "aws-java-sdk-core" % "1.11.321")
    .inProject
)

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case PathList("META-INF", "io.netty.versions.properties", xs @ _*) => MergeStrategy.first
  case x => MergeStrategy.first
}

libraryDependencies ++= Dependencies.test
libraryDependencies ++= Dependencies.spark.map(_ % "provided")
libraryDependencies ++= Dependencies.videoampSparkUtils
libraryDependencies ++= Dependencies.cleanroomApiProtobufs
libraryDependencies ++= Dependencies.logging
libraryDependencies ++= Dependencies.config
libraryDependencies ++= Dependencies.scalapbjson
libraryDependencies ++= Dependencies.scalapb
libraryDependencies ++= Dependencies.sqs

lazy val root = Project(id = "sqs_playground",
  base = file(".")
).settings(commonSettings)


publish := ()
publishLocal := ()
