organization := "ai.economicdatasciences"

sbtVersion := "0.13.17"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
    "org.apache.spark" %% "spark-core" % "3.0.0",
    "org.apache.spark" %% "spark-sql" % "3.0.0"
)
