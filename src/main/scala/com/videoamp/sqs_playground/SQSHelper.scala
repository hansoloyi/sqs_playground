package com.videoamp.sqs_playground

import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import com.amazonaws.services.sqs.model.{Message, ReceiveMessageRequest}

import scala.collection.JavaConverters._

class SQSHelper(queue: String, key: String, secret: String, numMessages: Int) {
  private val awsCreds = new BasicAWSCredentials(key, secret)
  private val receiveMessageRequest = new ReceiveMessageRequest(queue)

  private val sqs = AmazonSQSClientBuilder.standard().withRegion("us-east-1")
    .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
    .build()

  def getNextMessages(id: String): Array[Message] = {
    sqs.receiveMessage(receiveMessageRequest).getMessages().asScala.toArray
  }
}

object SQSHelper {

  def apply(queue: String, key: String, secret: String, numMessages: Int = 1) = new SQSHelper(queue, key, secret, numMessages)

}

//new SQSHelper("spades-events", "AKIAIZ3BFA6NF3L5CYKQ", "FvSwzZyjuSnI7N60JgpSF3UEts5rqqnvSUknkcP9")