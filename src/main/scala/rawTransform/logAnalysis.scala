package ai.economicdatasciences.rawTransform

import org.apache.spark.SparkConf
import org.datavec.api.transform.schema.Schema
import org.apache.spark.api.java.JavaSparkContext
import org.datavec.api.util.ClassPathResource
import org.datavec.api.records.reader.impl.regex.RegexLineRecordReader
import org.datavec.spark.transform.misc.StringToWritablesFunction
import org.datavec.spark.transform.AnalyzeSpark
import org.datavec.api.transform.TransformProcess
import org.datavec.api.writable.IntWritable
import org.datavec.api.transform.condition.string.StringRegexColumnCondition
import org.datavec.api.transform.ReduceOp
import org.datavec.api.transform.reduce.Reducer
import org.datavec.api.transform.reduce.Reducer.Builder


object LogsAnalysis {
  def main(args: Array[String]): Unit = {
    val schema = new Schema.Builder()
      .addColumnString("host")
      .addColumnString("timestamp")
      .addColumnString("request")
      .addColumnInteger("httpReplyCode")
      .addColumnInteger("replyBytes")
      .build()

    val conf = new SparkConf
    conf.setMaster("local")
    conf.setAppName("DataVec Log Analysis Example")

    val sc = new JavaSparkContext(conf)

    val directory = new ClassPathResource("data/access_log").getFile.getAbsolutePath()
    var logLines = sc.textFile(directory)
    logLines = logLines.filter {
      (s: String) =>
      s.matches("(\\S+) - - \\[(\\S+ - \\d{4})\\] \"(.+)\" (\\d+) (\\d+|-)")
    }

    val regex = "(\\S+) - - \\[(\\S+ - \\d{4})\\] \"(.+)\" (\\d+) (\\d+|-)"
    val rr = new RegexLineRecordReader(regex, 0)
    val parsed = logLines.map(new StringToWritablesFunction(rr))

    val dqa = AnalyzeSpark.analyzeQuality(schema, parsed)
    println("------- Data Quality --------")
    println(dqa)

    val tp = new TransformProcess.Builder(schema)
      .conditionalReplaceValueTransform("replyBytes", new IntWritable(0), new StringRegexColumnCondition("replyBytes", "\\D+"))
      // .reduce(new Reducer.Builder(ReduceOp.CountUnique))



  }
}
