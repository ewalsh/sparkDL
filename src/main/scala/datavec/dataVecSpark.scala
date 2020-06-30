package ai.economicdatasciences.datavec

import org.apache.spark.SparkConf
import org.datavec.api.transform.schema.Schema
import org.datavec.spark.transform.misc.StringToWritablesFunction
import org.datavec.spark.transform.misc.WritablesToStringFunction
import org.datavec.spark.transform.SparkTransformExecutor
import org.datavec.api.transform.condition.column.CategoricalColumnCondition
import org.datavec.api.transform.condition.ConditionOp
import org.datavec.api.transform.TransformProcess
import org.datavec.api.transform.filter.ConditionFilter
import org.datavec.api.util.ClassPathResource
import org.apache.spark.api.java.JavaSparkContext
// import org.apache.spark.SparkContext
import org.datavec.api.records.reader.impl.csv.CSVRecordReader

import java.util.Arrays
import java.util.HashSet

import scala.collection.JavaConverters._

object DataVecSpark {
    def main(args: Array[String]): Unit = {
        val inputDataSchema = new Schema.Builder()
            .addColumnString("DateTimeString")
            .addColumnsString("CustormerID", "MerchantID")
            .addColumnInteger("NumItenmsInTransaction")
            .addColumnCategorical("MerchantCountryCode", List("USA", "CAN", "FR", "MX").asJava)
            .addColumnDouble("TransactionAmountUSD", 0.0, null, false, false)
            .addColumnCategorical("FraudLabel", List("Fraud", "Legit").asJava)
            .build()

        val tp = new TransformProcess.Builder(inputDataSchema)
          .removeColumns("CustomerID", "MerchantID")
          .filter(new ConditionFilter(
            new CategoricalColumnCondition("MerchantCountryCode",
            ConditionOp.NotInSet, new HashSet(Arrays.asList("USA", "CAN")))
          ))
          .build()

        val conf = new SparkConf
        conf.setMaster(args(0))
        conf.setAppName("DataVec Example")
        val sc = new JavaSparkContext(conf)

        val directory = new ClassPathResource("data/datavec-example-data.csv")
          .getFile().getAbsolutePath()
        val stringData = sc.textFile(directory)
        val rr = new CSVRecordReader
        val parsedInputData = stringData.map(new StringToWritablesFunction(rr))

        val processedData = SparkTransformExecutor.execute(parsedInputData, tp)

        val processedAsString = processedData.map(new WritablesToStringFunction(","))
        val processedCollected = processedAsString.collect()
        val inputDataCollected = stringData.collect()
    }
}
