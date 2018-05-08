import sbt._

object Dependencies {
  val sparkVersion = "2.2.0"

  val videoampSparkUtils = Seq(
    "com.videoamp" %% "spark-util" % "3.0.0"
  )

  val test = Seq(
    "org.scalatest" %% "scalatest" % "3.0.4" % "test"
  )

  val spark = Seq(
    "org.apache.spark" %% "spark-core" % sparkVersion
      exclude ("com.typesafe", "config"),
    "org.apache.spark" %% "spark-hive" % sparkVersion
  )

  val logging = Seq(
    "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2"
  )

  val config = Seq(
    "com.typesafe" % "config" % "1.3.1"
  )

  val cleanroomApiProtobufs = Seq(
    "com.videoamp" %% "cleanroom-api-protobufs" % "1.0.34"
      exclude("com.google.api.grpc", "googleapis-common-protos")
  )

  val scalapb = Seq(
    "com.trueaccord.scalapb" %% "compilerplugin" % "0.6.7"
  )

  val scalapbjson = Seq(
    "com.trueaccord.scalapb" %% "scalapb-json4s" % "0.3.2"
  )

  val sqs = Seq(
    "com.amazonaws" % "aws-java-sdk-sqs" % "1.11.321"
  )

//  val awsSdk = Seq(
//    "com.amazonaws" % "aws-java-sdk" % "1.11.321"
//  )
}