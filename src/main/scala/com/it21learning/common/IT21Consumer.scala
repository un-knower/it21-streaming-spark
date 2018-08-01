package com.it21learning.common

import java.util.{Arrays, Properties}

import com.it21learning.config.settings.ConsumerSettings

import scala.reflect.ClassTag
import scala.reflect.runtime.universe.{TypeTag, typeOf}
import scala.collection.JavaConversions._
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecords
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig

/*
 KDS: Key Deserializer
 VDS: Value Deserializer
 */
@SerialVersionUID(20180722L)
abstract class IT21Consumer[KDS: TypeTag, VDS: TypeTag](group: String) extends Serializable { self: ConsumerSettings =>
  //create consumer
  protected def create[K, V](): KafkaConsumer[K, V] = {
    //properties
    val props = new Properties()
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerUrl)
    props.put(ConsumerConfig.GROUP_ID_CONFIG, group)
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, typeOf[KDS].typeSymbol.fullName)
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, typeOf[VDS].typeSymbol.fullName)
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
    props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true")
    //schema url
    props.put("schema.registry.url", schemaRegistryUrl)
    //create consumer
    new KafkaConsumer[K, V](props)
  }

  //consume
  def consume[K: ClassTag, V <: Product: ClassTag](topic: String, post: Seq[(K, V)] => Unit): Unit = {
    //consumer
    val consumer = create[K, V]()
    try {
      //subscribe
      consumer.subscribe(Arrays.asList(topic))

      //keep polling
      while ( true ) {
        //poll
        val records: ConsumerRecords[K, V] = consumer.poll(pollingTimeout)
        //check
        if ( records.count() > 0 ) {
          //parse
          val items: Seq[(K, V)] = {
            //loop
            for (record <- records.iterator().toList) yield (record.key(), record.value())
          }
          //post
          post(items)
        }
      }
    }
    finally {
      //close
      consumer.close()
    }
  }
}
