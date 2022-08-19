package Aggs

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.functions.{col, sum}
import org.apache.spark.sql.{DataFrame, SparkSession}

object aggregate {

  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)

  val spark: SparkSession = SparkSession.builder()
    .appName("StreamsAgg")
    .master("local[2]")
    .getOrCreate()

  // word counting
  def streamcount(): Unit = {
    // reading stream data from a socket, nc -lk 9000
    val line: DataFrame = spark.readStream
      .format("Socket")
      .option("host", "localhost")
      .option("port", 9000)
      .load()

    val linecount = line.selectExpr("count(*) as linecount")

    linecount.writeStream
      .format("console")
      .outputMode("complete")
      .start()
      .awaitTermination()
    // for aggregations, append and update are
    // only available
    // when using watermarks,
    // so we use complete here
  }

  // number adding
  def numcount(): Unit = {
    // reading stream data from a socket, nc -lk 9000
    val line: DataFrame = spark.readStream
      .format("Socket")
      .option("host", "localhost")
      .option("port", 9000)
      .load()

    val num = line.select(col("value")
      .cast("Double")
      .as("number"))
    val sumdf = num.select(sum(col("number")).as("sum so far"))

    sumdf.writeStream
      .format("console")
      .outputMode("complete")
      .start()
      .awaitTermination()
    // for aggregations, append and update are
    // only available
    // when using watermarks,
    // so we use complete here
  }

  // summing over groups with group by
  def groupcount(): Unit = {
    // reading stream data from a socket, nc -lk 9000
    val line: DataFrame = spark.readStream
      .format("Socket")
      .option("host", "localhost")
      .option("port", 9000)
      .load()

    val g = line.select(col("value").as("name"))
      .groupBy("name")
      .count()
      .sort(col("count").desc_nulls_last)

    g.writeStream
      .format("console")
      .outputMode("complete")
      .start()
      .awaitTermination()
    // for aggregations, append and update are
    // only available
    // when using watermarks,
    // so we use complete here
  }

  def main(args: Array[String]): Unit = {
    //numcount()
    //streamcount()
  groupcount()
  }

}
