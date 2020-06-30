package ai.economicdatasciences.streaming

import org.apache.spark.SparkConf
import org.datavec.api.transform.schema.Schema
import org.datavec.api.transform.TransformProcess
import org.datavec.api.transform.TransformProcess._
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.kafka.clients.consumer.ConsumerRecord


import org.apache.kafka.common.serialization.StringDeserializer

object IrisStream {
    def main(args: Array[String]): Unit = {
        val inputDataSchema = new Schema.Builder()
            .addColumnsDouble("Sepal length", "Sepal width", "Petal length",
                "Petal width")
            .addColumnInteger("Species")
            .build()

        val tp = new TransformProcess.Builder(inputDataSchema)
            .removeColumns("Petal length", "Petal width")
            .build()

        val outputSchema = tp.getFinalSchema()

        val Array(master, brokers, topics) = args

        val sparkConf = new SparkConf().setAppName("DirectKafkaDataVec")
            .setMaster(master)
        val ssc = new StreamingContext(sparkConf, Seconds(5))
        val topicsSet = topics.split(" ").toSet
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
    }
}
