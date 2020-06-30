package ai.economicdatasciences.datavec

import org.apache.spark.SparkConf
import org.datavec.api.transform.schema.Schema
// import org.datavec.spark.transform.misc.StringToWritablesFunction
// import org.datavec.spark.transform.misc.WritablesToStringFunction
// import org.datavec.spark.transform.SparkTransformExecutor
// import org.datavec.api.transform.condition.column.CategoricalColumnCondition
// import org.datavec.api.transform.condition.ConditionOp
// import org.datavec.api.transform.TransformProcess
// import org.datavec.api.transform.filter.ConditionFilter
// import org.datavec.api.util.ClassPathResource
// import org.apache.spark.api.java.JavaSparkContext
// import org.datavec.api.records.reader.impl.csv.CSVRecordReader
//
// import java.util.Arrays
// import java.util.HashSet

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
            .build
    }
}
