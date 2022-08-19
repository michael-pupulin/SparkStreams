ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion :="2.11.8"

val sparkVersion = "2.4.0"

lazy val root = (project in file("."))
  .settings(
    name := "SparkStream",
      libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % sparkVersion,
        "org.apache.spark" %% "spark-sql" % sparkVersion,

        // streaming
        "org.apache.spark" %% "spark-streaming" % sparkVersion
//
//        // streaming-kafka
//        "org.apache.spark" % "spark-sql-kafka-0-10_2.12" % sparkVersion,
//
//        // low-level integrations
//        "org.apache.spark" %% "spark-streaming-kafka-0-10" % sparkVersion
        )
  )
