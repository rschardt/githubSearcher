enablePlugins(DockerPlugin)

scalaVersion := "2.12.6"
name := "githubSearcher"
version := "1.0"

libraryDependencies ++= Seq(
  "io.vertx" %% "vertx-lang-scala" % "3.5.3",
  "io.vertx" %% "vertx-web-scala" % "3.5.3",
  "io.vertx" %% "vertx-web-client-scala" % "3.5.3",
  "org.specs2" %% "specs2-core" % "4.3.4" % Test
)

dockerfile in docker := {

  val jarFile: File = sbt.Keys.`package`.in(Compile, packageBin).value
  val classpath = (managedClasspath in Compile).value
  val mainclass = mainClass.in(Compile, packageBin).value.getOrElse(sys.error("Expected exactly one main class"))
  val jarTarget = s"/app/${jarFile.getName}"
  // Make a colon separated classpath with the JAR file
  val classpathString = classpath.files.map("/app/" + _.getName)
    .mkString(":") + ":" + jarTarget
  new Dockerfile {
    // Base image
    from("openjdk:8-jre")
    // Add all files on the classpath
    add(classpath.files, "/app/")
    // Add the JAR file
    add(jarFile, jarTarget)
    // On launch run Java with the classpath and the main class
    entryPoint("java", "-cp", classpathString, mainclass)
    expose(8080)
  }
}

