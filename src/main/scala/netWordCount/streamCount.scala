package ai.economicdatasciences.netWordCount

import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.storage.StorageLevel

object NetWordCount {
    def main(args: Array[String]): Unit = {
        println(args(0))
        val sparkConf = new SparkConf().setAppName("netWordCount").setMaster("local")
        val ssc = new StreamingContext(sparkConf, Seconds(1))
        val lines = ssc.socketTextStream(args(0), args(1).toInt,
            StorageLevel.MEMORY_AND_DISK_SER)

        val words = lines.flatMap(_.split(" "))
        val wordCounts = words.map(x => (x, 1)).reduceByKey(_ + _)
        wordCounts.print()

        ssc.start()
        ssc.awaitTermination()
    }
}
