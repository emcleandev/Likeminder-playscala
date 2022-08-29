name := """likefrance_1"""
organization := "lm"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.12.6"
libraryDependencies += "com.h2database" % "h2" % "1.4.192"
libraryDependencies += guice
libraryDependencies += javaWs
libraryDependencies += javaJdbc
libraryDependencies += ehcache
libraryDependencies += evolutions
libraryDependencies += jdbc
libraryDependencies += "org.glassfish.jaxb" % "jaxb-core" % "2.3.0.1"
libraryDependencies += "org.glassfish.jaxb" % "jaxb-runtime" % "2.3.2"

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.41"
