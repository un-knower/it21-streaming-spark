package com.it21learning.config.settings

trait ConsumerSettings extends KafkaSettings {
  //polling time out in milli-seconds
  def pollingTimeout: Long = 1000L
}
