package com.it21learning.config

import com.it21learning.config.settings.ProducerSettings

case class ProducerConfig(urlBroker: String, urlSchemaRegistry: String) {
  //settings
  trait Settings extends ProducerSettings {
    //broker url
    def brokerUrl: String = urlBroker
    //schema registry url
    def schemaRegistryUrl: String = urlSchemaRegistry
  }
}

