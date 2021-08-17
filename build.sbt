name := "HTTPS-Requests-Scala"

version := "0.1"

scalaVersion := "2.13.6"

ThisBuild / organization := "org.hrs"

mappings in (Compile, packageBin) ~= { _.filter(!_._1.getName.endsWith(".conf")) }