package com.it21learning.common

import com.it21learning.config.settings.StreamingConsumerSettings
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig
import org.apache.kafka.clients.consumer.ConsumerConfig
import scala.reflect.ClassTag
import scala.reflect.runtime.universe.{TypeTag, typeOf}
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext, Duration}
import org.apache.kafka.common.TopicPartition
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.rdd.RDD

/*
 KDS: Key Deserializer
 VDS: Value Deserializer
 */
@SerialVersionUID(20180722L)
abstract class StreamingIT21Consumer[KDS: TypeTag, VDS: TypeTag](group: String) extends Serializable { self: StreamingConsumerSettings =>
  //start
  def start[K: ClassTag, V <: Product: ClassTag](spark: SparkSession, topic: String, post: RDD[(K, V)] => Unit): Unit = {
    //the stream context
    val ssCtx = new StreamingContext(spark.sparkContext, Seconds(streamingInterval))
    try {
      //create the stream
      create[K, V](ssCtx, topic, group)
        .foreachRDD(rdd => post(rdd.map { cr: ConsumerRecord[K, V] => (cr.key(), cr.value()) }))

      //start
      ssCtx.start()
      //run till terminated
      ssCtx.awaitTermination()
    }
    finally {
      //close
      ssCtx.stop()
    }
  }

  //window
  def window[K: ClassTag, V <: Product: ClassTag](spark: SparkSession, topic: String, post: RDD[(K, V)] => Unit): Unit = {
    //the stream context
    val ssCtx = new StreamingContext(spark.sparkContext, Seconds(streamingInterval))
    try {
      //create the stream. To avoid the concurrency issue, we cache after window
	  //the issue details: https://github.com/apache/spark/pull/20997
      create[K, V](ssCtx, topic, group)
        .map { cr: ConsumerRecord[K, V] => (cr.key(), cr.value()) }
        .window(Duration(windowLength.toMillis), Duration(slidingInterval.toMillis))
        .cache()
      .foreachRDD(rdd => post(rdd))

      //start
      ssCtx.start()
      //run till terminated
      ssCtx.awaitTermination()
    }
    finally {
      //close
      ssCtx.stop()
    }
  }

  //create input-stream
  private def create[K: ClassTag, V <: Product: ClassTag](ssc: StreamingContext, topic: String, group: String): InputDStream[ConsumerRecord[K, V]] = {
    //parameters
    val kafkaParams = Map(
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> brokerUrl,
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> typeOf[KDS].typeSymbol.fullName,
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> typeOf[VDS].typeSymbol.fullName,
      ConsumerConfig.GROUP_ID_CONFIG -> group,
      ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> "earliest",
      KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG -> "true",
      //schema url
      "schema.registry.url" -> schemaRegistryUrl)
    //create the stream poll
    KafkaUtils.createDirectStream[K, V](
      ssc,
      LocationStrategies.PreferConsistent,
      //ConsumerStrategies.Assign(Array(new TopicPartition(topic, 0)), kafkaParams))
      ConsumerStrategies.Subscribe[K, V](List(topic), kafkaParams, Map(new TopicPartition(topic, 0) -> 0L)))
  }
}
