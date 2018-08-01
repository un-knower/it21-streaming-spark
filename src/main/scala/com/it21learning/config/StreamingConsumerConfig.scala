package com.it21learning.config

import com.it21learning.config.settings.StreamingConsumerSettings

import scala.concurrent.duration._

case class StreamingConsumerConfig(urlBroker: String, urlSchemaRegistry: String,
      interval: Int = 30, lengthWindow: Duration = 3 minutes, intervalSliding: Duration = 150 seconds) {
  //settings
  trait Settings extends StreamingConsumerSettings {
    //broker url
    def brokerUrl: String = urlBroker
    //schema registry url
    def schemaRegistryUrl: String = urlSchemaRegistry

    //streaming interval in seconds
    def streamingInterval: Int = interval

    //the length of a window
    def windowLength: Duration = lengthWindow
    //the interval
    def slidingInterval: Duration = intervalSliding
  }
}
