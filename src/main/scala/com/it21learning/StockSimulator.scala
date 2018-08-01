package com.it21learning

import java.time.Instant

import scala.util.Random
import org.apache.spark.sql.SparkSession
import com.datafibers.kafka.streams.avro.Stock
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import com.it21learning.config.{ProducerConfig, StreamingConsumerConfig}
import com.it21learning.streaming.model.Summary
import com.it21learning.streaming.{AvroProducer, StreamingAvroConsumer}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.functions._

@SerialVersionUID(20180722L)
class StockSimulator(spark: SparkSession) extends Serializable {
  //run
  def run(): Unit = {
    //import
    import com.it21learning.StockSimulator.produceStock
    //trigger the producer
    Future { produceStock(Settings.stockTopic) }

    //config
    val cfg = StreamingConsumerConfig(Settings.brokerUrl, Settings.schemaRegistryUrl)
    import cfg.{Settings => SourceSettings}
    //read
    (new StreamingAvroConsumer("stock-streaming-consumer") with SourceSettings)
      .window[String, Stock](spark, "stock-streaming-source", (stocks: RDD[(String, Stock)]) => sinkSummary(stocks))
  }

  //write stock summary
  private def sinkSummary(stocks: RDD[(String, Stock)]): Unit = {
    //create dataset
    import spark.implicits._
    val ds = spark.createDataset(stocks.map(s => s._2))
      .groupBy(col("symbol"), col("company_name"), col("open_price"))
      .agg(
        min(col("refresh_time")).as("start_time"),
        max(col("refresh_time")).as("end_time"),
        avg(col("ask_price")).as("ask_price_mean"),
        sum(abs(col("ask_price") - col("price"))).as("ask_price_error"),
        avg(col("bid_price")).as("bid_price_mean"),
        sum(abs(col("bid_price") - col("price"))).as("bid_price_error"),
        avg(col("price")).as("price_mean"),
        stddev(col("price")).as("price_change"))
      .select("start_time", "end_time", "symbol", "company_name", "open_price",
        "ask_price_mean", "ask_price_error", "bid_price_mean", "bid_price_error", "price_mean", "price_change")
      .as[Summary]

    //brokerUrl & schema registry url
    val brokerUrl = spark.sparkContext.broadcast(Settings.brokerUrl)
    val schemaRegistryUrl = spark.sparkContext.broadcast( Settings.schemaRegistryUrl )
    //topic
    val summaryTopic = spark.sparkContext.broadcast( Settings.summaryTopic )

    //save
    ds.rdd.foreachPartition( partitionRecords => {
      //producer settings
      val producerConfig = ProducerConfig(brokerUrl.value, schemaRegistryUrl.value)
      //sinker config
      import producerConfig.{Settings => SinkSettings}
      //the sinker
      val sinker: AvroProducer = new AvroProducer("stock-streaming-sink") with SinkSettings
      //sink
      sinker.produce[String, Summary](summaryTopic.value, partitionRecords.map(s => (s.symbol, s)).toList)
    })
  }
}

//the companion object
object StockSimulator {
  //generator
  private val generator: Random = new Random(System.currentTimeMillis())

  //get time
  def getTimestamp(): String = Instant.ofEpochMilli(System.currentTimeMillis).toString()
  //adjust value by range
  import scala.math.{max, min}
  def adjust(value: Double): Double = max(min(value + (generator.nextDouble() - 0.5D)*value, value*1.8D), value*0.6D)

  //produce
  def produceStock(topic: String): Unit = {
    //init ask size
    val init_ask_size: Int = 2000
    //init bid size
    val init_bid_size: Int = 3180
    //stock
    val stock = Stock(getTimestamp(), "KGC", "KGC Ltd.", "sse", 11.92D, 15.29D, init_ask_size, 14.53D, init_bid_size, 12.87D)

    //config
    val config = new ProducerConfig(Settings.brokerUrl, Settings.schemaRegistryUrl)
    import config.{Settings => WritingSettings}
    //producer
    val producer = new AvroProducer("stock-producer") with WritingSettings
    //simulate
    while ( true ) {
      //new time
      stock.refresh_time = getTimestamp()
      //adjust
      stock.ask_price = adjust(stock.open_price)
      stock.ask_size = adjust(init_ask_size).toInt
      stock.bid_price = adjust(stock.open_price)
      stock.bid_size = adjust(init_bid_size).toInt
      stock.price = adjust(stock.open_price)
      //write
      producer.produce[String, Stock](topic, Array(("%s.%s".format(stock.symbol, stock.refresh_time), stock)))

      //interval
      Thread.sleep(700)
    }
  }
}
