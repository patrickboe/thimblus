import sbt._

class ThimblusProject(info: ProjectInfo) extends DefaultProject(info)
{
 val scalaSwing = "org.scala-lang" % "scala-swing" % "2.8.1"
 val liftJson = "net.liftweb" % "lift-json_2.8.1" % "2.3"
 val scalaTest = "org.scalatest" % "scalatest_2.8.1" % "1.5"
}

// vim: sw=2:softtabstop=2:et:
