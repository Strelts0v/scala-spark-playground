name := "scala-spark-playground"
organization in ThisBuild := "com.example"
scalaVersion in ThisBuild := "2.11.12"

// PROJECTS

lazy val global = project
  .in(file("."))
  .settings(settings)
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    common,
    sparkRedshiftAdapter
  )

lazy val common = project
  .settings(
    name := "common",
    settings,
    libraryDependencies ++= commonDependencies
  )
  .disablePlugins(AssemblyPlugin)

lazy val sparkRedshiftAdapter = project
  .settings(
    name := "sparkRedshiftAdapter",
    settings,
    assemblySettings,
    libraryDependencies ++= commonDependencies ++ Seq(
      dependencies.sparkAvro,
      dependencies.sparkRedshift,
      dependencies.awsSdkS3,
      dependencies.awsHadoop,
      dependencies.hadoopCommon
    )
  )
  .dependsOn(
    common
  )

// DEPENDENCIES

lazy val dependencies =
  new {
    val logbackV = "1.2.3"
    val logstashV = "4.11"
    val scalaLoggingV = "3.7.2"
    val slf4jV = "1.7.25"
    val typesafeConfigV = "1.3.1"
    val scalatestV = "3.0.4"
    val scalacheckV = "1.13.5"
    val sparkV = "2.3.4"
    val scalaCompatV = "2.11"
    val scalaTestV = "3.0.5"
    val sparkRedshiftV = "3.0.0-preview1"
    val sparkAvroV = "3.1.0"
    val awsSdkV = "1.11.271"
    val awsHadoopV = "3.1.1"
    val hadoopV = "3.1.1"

    val logback = "ch.qos.logback" % "logback-classic" % logbackV
    val logstash = "net.logstash.logback" % "logstash-logback-encoder" % logstashV
    val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingV
    val slf4j = "org.slf4j" % "jcl-over-slf4j" % slf4jV
    val typesafeConfig = "com.typesafe" % "config" % typesafeConfigV
    val scalatest = "org.scalatest" %% "scalatest" % scalatestV
    val scalacheck = "org.scalacheck" %% "scalacheck" % scalacheckV
    val spark = "org.apache.spark" %% "spark-sql" % sparkV
    val sparkAvro = "com.databricks" % s"spark-avro_${scalaCompatV}" % sparkAvroV
    val sparkRedshift = "com.databricks" % s"spark-redshift_${scalaCompatV}" % sparkRedshiftV
    val awsSdkS3 = "com.amazonaws" % "aws-java-sdk-s3" % awsSdkV
    val awsHadoop = "org.apache.hadoop" % "hadoop-aws" % awsHadoopV
    val hadoopCommon = "org.apache.hadoop" % "hadoop-common" % hadoopV
  }

lazy val commonDependencies = Seq(
  dependencies.logback,
  dependencies.logstash,
  dependencies.scalaLogging,
  dependencies.slf4j,
  dependencies.typesafeConfig,
  dependencies.spark,
  dependencies.scalatest % "test",
  dependencies.scalacheck % "test"
)

// SETTINGS

lazy val settings =
  commonSettings ++
    wartremoverSettings ++
    scalafmtSettings

lazy val compilerOptions = Seq(
  "-unchecked",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-deprecation",
  "-encoding",
  "utf8"
)

lazy val commonSettings = Seq(
  scalacOptions ++= compilerOptions,
  resolvers ++= Seq(
    "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")
  )
)

lazy val wartremoverSettings = Seq(
  wartremoverWarnings in (Compile, compile) ++= Warts.allBut(Wart.Throw)
)

lazy val scalafmtSettings =
  Seq(
    scalafmtOnCompile := true,
    scalafmtTestOnCompile := true,
    scalafmtVersion := "1.2.0"
  )

lazy val assemblySettings = Seq(
  assemblyJarName in assembly := name.value + ".jar",
  assemblyMergeStrategy in assembly := {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case "application.conf"            => MergeStrategy.concat
    case x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  }
)
