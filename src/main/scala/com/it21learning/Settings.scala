package com.it21learning

import scala.concurrent.duration._

object Settings {
  //broker url
  def brokerUrl: String = "localhost:9092"
  //schema registry url
  def schemaRegistryUrl: String = "http://localhost:8081"

  //streaming interval in seconds
  def streamingInterval: Int = 60
  //polling time out in milli-seconds
  def pollingTimeout: Long = 1000L
  //window length
  def windowLength: Duration = 5 minutes
  //sliding interval
  def slidingInterval: Duration = 2 minutes

  //the stock source topic
  def stockTopic: String = "stock-streaming-source"
  //the stock summary topic
  def summaryTopic: String = "stock-streaming-sink"

  //topics
  def testTopic: String = "it21-streaming"
}
