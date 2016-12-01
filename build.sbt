name := """order-transaction"""

version := "0.0.1"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  cache,
  ws,
  "mysql" % "mysql-connector-java" % "5.1.40",
  "com.typesafe.play" %% "play-slick" % "2.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "2.0.0",
  specs2 % Test,
  "org.scalacheck" %% "scalacheck" % "1.12.4" % Test,
  "org.specs2" %% "specs2-scalacheck" % "3.6" % Test,
  "me.jeffmay" %% "play-json-tests" % "1.3.0" % Test,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

resolvers ++= Seq(
  "zalando-bintray" at "https://dl.bintray.com/zalando/maven",
  "scalaz-bintray"  at "http://dl.bintray.com/scalaz/releases",
  "jeffmay"         at "https://dl.bintray.com/jeffmay/maven",
  Resolver.url("sbt-plugins", url("http://dl.bintray.com/zalando/sbt-plugins"))(Resolver.ivyStylePatterns)
)

lazy val root = (project in file(".")).enablePlugins(PlayScala, ApiFirstCore, ApiFirstPlayScalaCodeGenerator, ApiFirstSwaggerParser)

routesGenerator := InjectedRoutesGenerator

apiFirstParsers := Seq(ApiFirstSwaggerParser.swaggerSpec2Ast.value).flatten

playScalaAutogenerateTests := true
