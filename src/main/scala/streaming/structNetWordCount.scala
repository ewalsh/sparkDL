package ai.economicdatasciences.streaming

// import org.apache.spark._
// import org.apache.spark.streaming._
// import org.apache.spark.streaming.StreamingContext
// import org.apache.spark.streaming.StreamingContext._
// import org.apache.spark.storage.StorageLevel
import org.apache.spark.sql.SparkSession

object StructuredNetworkWordCount {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("StructuredNetworkWordCount")
      .master(args(0))
      .getOrCreate()

    spark.sparkContext.setLogLevel("WARN")

    import spark.implicits._

    val lines = spark.readStream.format("socket")
      .option("host",args(1))
      .option("port",args(2).toInt)
      .load()

      val words = lines.as[String].flatMap(_.split(" ")).filter(_.length > 2)
      val wordCounts = words.groupBy("value").count()

      val query = wordCounts.writeStream.outputMode("complete")
        .format("console")
        .start()

      query.awaitTermination()
  }
}
