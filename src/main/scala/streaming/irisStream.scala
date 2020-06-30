package ai.economicdatasciences.streaming

import org.apache.spark.SparkConf
import org.datavec.api.transform.schema.Schema
import org.datavec.api.transform.TransformProcess
import org.datavec.api.transform.TransformProcess._


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
    }
}
