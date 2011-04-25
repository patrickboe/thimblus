import sbt._

class GUIProject(info: ProjectInfo) extends DefaultProject(info)
{
 val scalaSwing = "org.scala-lang" % "scala-swing" % "2.8.1"
}

// vim: set sw=2 set softtabstop=2 et:
