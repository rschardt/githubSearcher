scalaVersion := "2.12.6"
name := "githubSearcher"
//organization := "Robert Schardt"
version := "1.0"

//libraryDependencies += "org.typelevel" %% "cats-core" % "1.1.0"
//libraryDependencies += "io.vertx" %% "vertx-web-common-scala" % "3.5.3"

libraryDependencies ++= Seq(
  "io.vertx" %% "vertx-lang-scala" % "3.5.3",
  "io.vertx" %% "vertx-web-scala" % "3.5.3",
  "io.vertx" %% "vertx-web-client-scala" % "3.5.3",
  "org.specs2" %% "specs2-core" % "4.3.4" % Test
)

//scalacOptions in Test ++= Seq("-Yrangepos")
