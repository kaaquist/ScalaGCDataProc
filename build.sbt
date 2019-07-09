name := "scalagcdataproc"

version := "0.1"

scalaVersion := "2.12.8"

mainClass := Some("ScalaApp")
artifactName := {(sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>  "scalagcdataprocapp.jar" }

libraryDependencies ++= {
  val sparkVersion = "2.4.3"
  Seq(
    // Spark
    "org.apache.spark"                %% "spark-core"                   % sparkVersion,

    // Url URI stuff
    "io.lemonlabs"                    %% "scala-uri"                    % "1.4.5",

    // Config
    "com.github.pureconfig"           %% "pureconfig"                   % "0.10.1",

    // Logger
    "com.typesafe.scala-logging"      %% "scala-logging"                % "3.9.2",
    "org.log4s"                       %% "log4s"                        % "1.6.1",
    "ch.qos.logback"                   % "logback-classic"              % "1.2.3",
    
    // Test
    "org.scalatest"                   %% "scalatest"                    % "3.0.5"       % "test"
  )}