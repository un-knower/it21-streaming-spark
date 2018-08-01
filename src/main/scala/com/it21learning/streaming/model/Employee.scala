package com.it21learning.streaming.model

import scala.annotation.switch
import org.apache.avro.Schema
import org.apache.avro.specific.SpecificRecordBase

/*
 The Employee data object, which is automatically generated with schema by:
 https://avro2caseclass.herokuapp.com
 */
@SerialVersionUID(20180722L)
case class Employee(var name: String, var age: Int, var gender: String, var title: String,
    var department: String, var webUrl: String) extends SpecificRecordBase with Serializable  {
  //default constructor
  def this() = this("", 0, "", "", "", "")

  //the get method
  def get(field$: Int): AnyRef = {
    //check
    (field$: @switch) match {
      case pos if pos == 0 => { name }.asInstanceOf[AnyRef]
      case pos if pos == 1 => { age }.asInstanceOf[AnyRef]
      case pos if pos == 2 => { gender }.asInstanceOf[AnyRef]
      case pos if pos == 3 => { title }.asInstanceOf[AnyRef]
      case pos if pos == 4 => { department }.asInstanceOf[AnyRef]
      case pos if pos == 5 => { webUrl }.asInstanceOf[AnyRef]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
  }
  //the put method
  def put(field$: Int, value: Any): Unit = {
    //check
    (field$: @switch) match {
      case pos if pos == 0 => this.name = { value.toString }.asInstanceOf[String]
      case pos if pos == 1 => this.age = { value }.asInstanceOf[Int]
      case pos if pos == 2 => this.gender = { value.toString }.asInstanceOf[String]
      case pos if pos == 3 => this.title = { value.toString }.asInstanceOf[String]
      case pos if pos == 4 => this.department = { value.toString }.asInstanceOf[String]
      case pos if pos == 5 => this.webUrl = { value.toString }.asInstanceOf[String]
      case _ => new org.apache.avro.AvroRuntimeException("Bad index")
    }
    ()
  }
  //get the schema
  def getSchema: Schema = Employee.SCHEMA$
}

object Employee {
  //the static schema
  val SCHEMA$ = new Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Employee\",\"namespace\":\"com.it21learning.streaming.model\",\"fields\":[{\"name\":\"name\",\"type\":\"string\"},{\"name\":\"age\",\"type\":\"int\"},{\"name\":\"gender\",\"type\":\"string\"},{\"name\":\"title\",\"type\":\"string\"},{\"name\":\"department\",\"type\":\"string\"},{\"name\":\"webUrl\",\"type\":\"string\"}]}")
}