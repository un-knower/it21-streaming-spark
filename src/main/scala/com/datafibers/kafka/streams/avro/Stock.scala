package com.datafibers.kafka.streams.avro

import org.apache.avro.Schema
import org.apache.avro.specific.SpecificRecordBase

import scala.annotation.switch

/*
 The Employee data object, which is automatically generated with schema by:
 https://avro2caseclass.herokuapp.com
 */
@SerialVersionUID(20180722L)
case class Stock(var refresh_time: String, var symbol: String, var company_name: String, var exchange: String,
      var open_price: Double, var ask_price: Double, var ask_size: Int, var bid_price: Double, var bid_size: Int, var price: Double)
    extends SpecificRecordBase with Serializable {
  //default constructor
  def this() = this("", "", "", "", 0.0, 0.0, 0, 0.0, 0, 0.0)

  //get method
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case pos if pos == 0 => { refresh_time }.asInstanceOf[AnyRef]
      case pos if pos == 1 => { symbol }.asInstanceOf[AnyRef]
      case pos if pos == 2 => { company_name }.asInstanceOf[AnyRef]
      case pos if pos == 3 => { exchange }.asInstanceOf[AnyRef]
      case pos if pos == 4 => { open_price }.asInstanceOf[AnyRef]
      case pos if pos == 5 => { ask_price }.asInstanceOf[AnyRef]
      case pos if pos == 6 => { ask_size }.asInstanceOf[AnyRef]
      case pos if pos == 7 => { bid_price }.asInstanceOf[AnyRef]
      case pos if pos == 8 => { bid_size }.asInstanceOf[AnyRef]
      case pos if pos == 9 => { price }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }

  //write
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case pos if pos == 0 => this.refresh_time = { value.toString }.asInstanceOf[String]
      case pos if pos == 1 => this.symbol = { value.toString }.asInstanceOf[String]
      case pos if pos == 2 => this.company_name = { value.toString }.asInstanceOf[String]
      case pos if pos == 3 => this.exchange = { value.toString }.asInstanceOf[String]
      case pos if pos == 4 => this.open_price = { value }.asInstanceOf[Double]
      case pos if pos == 5 => this.ask_price = { value }.asInstanceOf[Double]
      case pos if pos == 6 => this.ask_size = { value }.asInstanceOf[Int]
      case pos if pos == 7 => this.bid_price = { value }.asInstanceOf[Double]
      case pos if pos == 8 => this.bid_size = { value }.asInstanceOf[Int]
      case pos if pos == 9 => this.price = { value }.asInstanceOf[Double]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }

  //schema
  def getSchema: org.apache.avro.Schema = Stock.SCHEMA$
}

object Stock {
  //the schema
  val SCHEMA$ = new Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Stock\",\"namespace\":\"com.datafibers.kafka.streams.avro\",\"fields\":[{\"name\":\"refresh_time\",\"type\":\"string\"},{\"name\":\"symbol\",\"type\":\"string\"},{\"name\":\"company_name\",\"type\":\"string\"},{\"name\":\"exchange\",\"type\":\"string\"},{\"name\":\"open_price\",\"type\":\"double\"},{\"name\":\"ask_price\",\"type\":\"double\"},{\"name\":\"ask_size\",\"type\":\"int\"},{\"name\":\"bid_price\",\"type\":\"double\"},{\"name\":\"bid_size\",\"type\":\"int\"},{\"name\":\"price\",\"type\":\"double\"}]}")
}