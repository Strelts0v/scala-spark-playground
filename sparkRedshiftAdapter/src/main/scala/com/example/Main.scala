package com.example

import org.apache.spark.sql.SparkSession

object Main extends App {

  override def main(args: Array[String]): Unit = {
    val entity = Entity("id", NestedEntity("value"))
    println(entity)

    val spark = SparkSession
      .builder()
      .master("local")
      .appName("Simple Application")
      .getOrCreate()

    spark.stop()
  }

}
