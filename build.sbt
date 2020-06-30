organization := "ai.economicdatasciences"

sbtVersion := "0.13.17"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
    "org.apache.spark" %% "spark-core" % "3.0.0",
    "org.apache.spark" %% "spark-sql" % "3.0.0",
    "org.apache.spark" %% "spark-streaming" % "3.0.0",
    "com.datastax.spark" %% "spark-cassandra-connector" % "3.0.0-alpha2",
    "io.circe" %% "circe-core" % "0.14.0-M1",
    "io.circe" %% "circe-generic" % "0.14.0-M1",
    "io.circe" %% "circe-parser" % "0.14.0-M1",
    "ch.qos.logback" % "logback-classic" % "1.2.3" % Test,
    "org.deeplearning4j" %% "dl4j-spark" % "1.0.0-beta7",
    "org.datavec" % "datavec-api" % "1.0.0-beta7",
    "org.datavec" %% "datavec-spark" % "1.0.0-beta7"
)
