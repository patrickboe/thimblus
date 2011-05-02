import sbt._

class ThimblrProject(info: ProjectInfo) extends DefaultProject(info)
{
 val scalaSwing = "org.scala-lang" % "scala-swing" % "2.8.1"
 val liftJson = "net.liftweb" % "lift-json_2.8.1" % "2.3"
}

// vim: set sw=2 set softtabstop=2 et:
