name := "wat"

version := "1.0"

scalaVersion := "2.11.7"

// Read here for optional jars and dependencies
libraryDependencies ++= Seq("org.specs2" %% "specs2-core" % "3.7.2" % "test")
libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.13.0" % "test"

scalacOptions in Test ++= Seq("-Yrangepos")
