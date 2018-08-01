package com.it21learning.config.settings

import scala.concurrent.duration._

trait StreamingConsumerSettings extends ConsumerSettings {
  //streaming interval in seconds
  def streamingInterval: Int

  //the length of a window
  def windowLength: Duration
  //the interval
  def slidingInterval: Duration
}
