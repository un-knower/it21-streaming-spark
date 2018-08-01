package com.it21learning.streaming

import com.it21learning.common.IT21Producer
import com.it21learning.config.settings.ProducerSettings
import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.kafka.common.serialization.StringSerializer

/*
  KS: KeySerializer
  VS: ValueSerializer
 */
@SerialVersionUID(20180722L)
class AvroProducer(client: String) extends IT21Producer[StringSerializer, KafkaAvroSerializer](client) { self: ProducerSettings =>

}
