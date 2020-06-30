package ai.economicdatasciences.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.kafka.clients.consumer.ConsumerRecord

import org.apache.kafka.common.serialization.StringDeserializer


object DirectKafkaWordCount {
  def main(args: Array[String]): Unit = {
    System.err.println(s"""
      | Usage: DirectKafkaWordCount <spark_master> <brokers> <topics>
      |   <spark_master> is the spark master URL
      |   <brokers> list of one or more kafka brokers
      |   <topics> list of one or more topics to consume
      """
    )

    val Array(master, brokers, topics) = args

    val sparkConf = new SparkConf().setAppName("DirectKafkaWordCount")
      .setMaster(master)

    val ssc = new StreamingContext(sparkConf, Seconds(5))
    ssc.sparkContext.setLogLevel("WARN")

    val topicsSet = topics.split(",")
    val kafkaParams = Map[String, Object](
      "metadata.broker.list" -> brokers,
      "bootstrap.servers" -> "localhost:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "only",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    val messages = KafkaUtils.createDirectStream(ssc,
      PreferConsistent,
      Subscribe[String, String](topicsSet, kafkaParams)
    )

    val lines = messages.map(xStream => xStream.value)
    val words = lines.flatMap(_.split(" ")).filter(_.length > 2)
    val wordCounts = words.map(x => (x, 1L)).reduceByKey(_ + _)
    wordCounts.print()

    ssc.start()
    ssc.awaitTermination()

  }
}
