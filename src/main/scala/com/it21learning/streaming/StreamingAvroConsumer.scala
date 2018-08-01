package com.it21learning.streaming

import com.it21learning.common.StreamingIT21Consumer
import com.it21learning.config.settings.StreamingConsumerSettings
import io.confluent.kafka.serializers.KafkaAvroDeserializer
import org.apache.kafka.common.serialization.StringDeserializer

@SerialVersionUID(20180722L)
class StreamingAvroConsumer(group: String)
  extends StreamingIT21Consumer[StringDeserializer, KafkaAvroDeserializer](group) { self: StreamingConsumerSettings =>

}
