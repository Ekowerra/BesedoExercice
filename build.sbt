ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.0"

lazy val root = (project in file("."))
  .settings(
    name := "BesedoExercice",
  )
  .settings(libraryDependencies += "org.typelevel" %% "cats-core" % "2.9.0")
  .settings(libraryDependencies += "org.typelevel" %% "cats-effect" % "3.5.1")
  .settings(libraryDependencies ++= Seq(
    "org.typelevel" %% "log4cats-core" % "2.6.0",
    "org.typelevel" %% "log4cats-slf4j" % "2.6.0",
    "ch.qos.logback" % "logback-classic" % "1.4.7" % Runtime
  ))
  .settings(libraryDependencies += "eu.timepit" %% "refined" % "0.11.0")
  .settings(libraryDependencies ++= Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-refined",
    "io.circe" %% "circe-parser",
  ).map(_ % circeVersion))
  .settings(libraryDependencies += "org.latestbit" %% "circe-tagged-adt-codec" % "0.11.0")
  .settings(libraryDependencies += "org.gnieh" %% "fs2-data-json-circe" % "1.8.0")
  .settings(libraryDependencies ++= Seq("co.fs2" %% "fs2-core" % "3.9.1", "co.fs2" %% "fs2-io" % "3.9.1"))
  .settings(libraryDependencies += "is.cir" %% "ciris" % "3.2.0")
  .settings(libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.17")
  .settings(libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.17" % "test")
  .settings(libraryDependencies += "org.scalatestplus" %% "scalacheck-1-17" % "3.2.17.0" % "test")
  .settings(libraryDependencies += "com.google.jimfs" % "jimfs" % "1.3.0" % Test)

val circeVersion = "0.14.6"

