package com.it21learning.streaming.model

import scala.annotation.switch
import org.apache.avro.Schema
import org.apache.avro.specific.SpecificRecordBase

/*
 The Employee data object, which is automatically generated with schema by:
 https://avro2caseclass.herokuapp.com
 */
@SerialVersionUID(20180722L)
case class Summary(var start_time: String, var end_time: String, var symbol: String, var company_name: String,
      var open_price: Double, var ask_price_mean: Double, var ask_price_error: Double, var bid_price_mean: Double,
      var bid_price_error: Double, var price_mean: Double, var price_change: Double)
    extends SpecificRecordBase with Serializable  {
  //default constructor
  def this() = this("", "", "", "", 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

  //get method
  def get(field$: Int): AnyRef = {
    (field$: @switch) match {
      case pos if pos == 0 => { start_time }.asInstanceOf[AnyRef]
      case pos if pos == 1 => { end_time }.asInstanceOf[AnyRef]
      case pos if pos == 2 => { symbol }.asInstanceOf[AnyRef]
      case pos if pos == 3 => { company_name }.asInstanceOf[AnyRef]
      case pos if pos == 4 => { open_price }.asInstanceOf[AnyRef]
      case pos if pos == 5 => { ask_price_mean }.asInstanceOf[AnyRef]
      case pos if pos == 6 => { ask_price_error }.asInstanceOf[AnyRef]
      case pos if pos == 7 => { bid_price_mean }.asInstanceOf[AnyRef]
      case pos if pos == 8 => { bid_price_error }.asInstanceOf[AnyRef]
      case pos if pos == 9 => { price_mean }.asInstanceOf[AnyRef]
      case pos if pos == 10 => { price_change }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }

  //put method
  def put(field$: Int, value: Any): Unit = {
    (field$: @switch) match {
      case pos if pos == 0 => this.start_time = { value.toString }.asInstanceOf[String]
      case pos if pos == 1 => this.end_time = { value.toString }.asInstanceOf[String]
      case pos if pos == 2 => this.symbol = { value.toString }.asInstanceOf[String]
      case pos if pos == 3 => this.company_name = { value.toString }.asInstanceOf[String]
      case pos if pos == 4 => this.open_price = { value }.asInstanceOf[Double]
      case pos if pos == 5 => this.ask_price_mean = { value }.asInstanceOf[Double]
      case pos if pos == 6 => this.ask_price_error = { value }.asInstanceOf[Double]
      case pos if pos == 7 => this.bid_price_mean = { value }.asInstanceOf[Double]
      case pos if pos == 8 => this.bid_price_error = { value }.asInstanceOf[Double]
      case pos if pos == 9 => this.price_mean = { value }.asInstanceOf[Double]
      case pos if pos == 10 => this.price_change = { value }.asInstanceOf[Double]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }

  //schema
  def getSchema: org.apache.avro.Schema = Summary.SCHEMA$
}

object Summary {
  //schema
  val SCHEMA$ = new Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Summary\",\"namespace\":\"com.it21learning.streaming.model\",\"fields\":[{\"name\":\"start_time\",\"type\":\"string\"},{\"name\":\"end_time\",\"type\":\"string\"},{\"name\":\"symbol\",\"type\":\"string\"},{\"name\":\"company_name\",\"type\":\"string\"},{\"name\":\"open_price\",\"type\":\"double\"},{\"name\":\"ask_price_mean\",\"type\":\"double\"},{\"name\":\"ask_price_error\",\"type\":\"double\"},{\"name\":\"bid_price_mean\",\"type\":\"double\"},{\"name\":\"bid_price_error\",\"type\":\"double\"},{\"name\":\"price_mean\",\"type\":\"double\"},{\"name\":\"price_change\",\"type\":\"double\"}]}")
}