name := """oktanaApi"""
organization := "com.ernestochero"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)
val circeVersion = "0.13.0"
val mongoDriverVersion = "2.9.0"
val playVersion = "2.8.1"
val mockitoVersion = "1.14.8"
scalaVersion := "2.13.2"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % playVersion,
  "org.mongodb.scala" %% "mongo-scala-driver" % mongoDriverVersion,
  "org.mockito" %% "mockito-scala" % mockitoVersion
)
libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.ernestochero.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.ernestochero.binders._"
