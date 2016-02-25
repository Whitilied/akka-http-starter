enablePlugins(JavaAppPackaging)

organization := "com.gamsd"
name := "starter"
version := "0.1"

scalaVersion := "2.11.7"
scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers ++= Seq(
  Resolver.defaultLocal,
  DefaultMavenRepository,
  Resolver.typesafeRepo("releases"),
  Resolver.typesafeIvyRepo("releases"),
  Resolver.bintrayRepo("hseeberger", "maven")
)

libraryDependencies ++= {
  val akka = "2.4.2"
  val json4s = "3.3.0"

  Seq(
    "com.typesafe.akka"             %% "akka-stream"                  % akka,
    "com.typesafe.akka"             %% "akka-http-core"               % akka,
    "com.typesafe.play"             %% "play-streams-experimental"    % "2.4.3",
    "de.heikoseeberger"             %% "akka-http-json4s"             % "1.5.2",
    "org.json4s"                    %% "json4s-native"                % json4s,
    "org.json4s"                    %% "json4s-ext"                   % json4s,
    "ch.qos.logback"                %  "logback-classic"              % "1.1.3",
    "org.apache.logging.log4j"      %  "log4j-to-slf4j"               % "2.3"
  )
}

//mainClass in Compile := Some("com.gamsd.starter.StarterAPI")

Revolver.settings