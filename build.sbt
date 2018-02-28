organization := "Metrotek"

name := "EngineNode"

version := "1.0"

mainClass in (Compile, run) := Some("com.engine.node.EngineNodeMain")

val spring = "5.0.4.RELEASE"
val springBoot = "1.5.10.RELEASE"
val akkaVersion = "2.5.10"

lazy val root = project.in(file(".")).enablePlugins(JavaServerAppPackaging).settings(
  libraryDependencies ++= Seq(
    "org.springframework" % "spring-context" % spring,
    "org.springframework.boot" % "spring-boot-starter" % springBoot,
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-remote" % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster" % akkaVersion
  )
)