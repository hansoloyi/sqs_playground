package com.videoamp.sqs_playground

import java.util.Base64
import com.typesafe.scalalogging.LazyLogging
import com.videoamp.linear_planner.optimizer.RunLinearOptimizerRequest
import org.apache.spark.sql.SparkSession

object SQSApp extends LazyLogging {

  val sqsHelper = SQSHelper("spades-events", "AKIAIZ3BFA6NF3L5CYKQ", "FvSwzZyjuSnI7N60JgpSF3UEts5rqqnvSUknkcP9")

  def run(): Unit = {
    val messages = sqsHelper.getNextMessages("")
    println(s"getting here: ${messages.length}")

    if (messages.length == 0) {
      Thread.sleep(10000)
    }

    messages.foreach(message => {
      val messageId = message.getMessageId
      val body = message.getBody

      val loRequest: RunLinearOptimizerRequest = RunLinearOptimizerRequest.parseFrom(Base64.getDecoder.decode(body))
      println(s"message_id: $messageId\nprotobuf: $loRequest")
    })

    run()
  }

  def main(args: Array[String]): Unit = {
    println("getting here")

    val spark =
      SparkSession
        .builder
        .appName(s"sqs_playground_hyi")
        .enableHiveSupport()
        .getOrCreate

    run()
  }
}