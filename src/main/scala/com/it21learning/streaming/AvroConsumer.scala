package com.it21learning.streaming

import com.it21learning.common.IT21Consumer
import com.it21learning.config.settings.ConsumerSettings
import io.confluent.kafka.serializers.KafkaAvroDeserializer
import org.apache.kafka.common.serialization.StringDeserializer

@SerialVersionUID(20180722L)
class AvroConsumer(group: String) extends IT21Consumer[StringDeserializer, KafkaAvroDeserializer](group) { self: ConsumerSettings =>

}
