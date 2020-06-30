package ai.economicdatasciences.spark

import org.apache.spark._
import org.apache.spark.sql._
import org.apache.log4j.{Level, Logger}

object SparkInit {
    val rootLogger = Logger.getRootLogger()
    rootLogger.setLevel(Level.WARN)
    val logFile = "logs/sparkLog.txt"
    val spark = SparkSession.builder().master("local[*]").appName("scalaDL").getOrCreate()
    val logData = spark.read.textFile(logFile).cache()
    spark.sparkContext.setLogLevel("WARN") // .config("spark.logConf","true").config("spark.logLevel","WARN")
}
