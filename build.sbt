val catsVersion = "2.1.1"
val circeVersion = "0.13.0"
val http4sVersion = "0.21.6"
val configVersion = "1.4.0"
val scraperVersion = "2.2.0"

lazy val commonSettings = Seq(
  name := "simple-crawler",
  version := "0.1",
  scalaVersion := "2.12.8"
)

lazy val main = (project in file("."))
  .settings(
    commonSettings,
    fork in Test := true,
    scalacOptions ++= Seq(
      "-deprecation",
      "-unchecked",
      "-feature",
      "-language:implicitConversions",
      "-language:higherKinds",
      "-Ypartial-unification"),
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core" % catsVersion,

      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-literal" % circeVersion,

      "org.http4s" %% "http4s-blaze-server" % http4sVersion,
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion,

      "net.ruippeixotog" %% "scala-scraper" % scraperVersion,

      "com.typesafe" % "config" % configVersion
    )
  )