package com.it21learning.config

import com.it21learning.config.settings.ConsumerSettings

case class ConsumerConfig(urlBroker: String, urlSchemaRegistry: String, timeout: Long = 1000L) {
  //the setting
  trait Settings extends ConsumerSettings {
    //broker url
    def brokerUrl: String = urlBroker
    //schema registry url
    def schemaRegistryUrl: String = urlSchemaRegistry

    //polling time out in milli-seconds
    override def pollingTimeout: Long = timeout
  }
}
