package org.thimblr.io

import java.io._

object Local {
  def streamer(path: String) = () => {
    try{
      new FileReader(path)
    } catch { 
      case ex: FileNotFoundException => throw PlanNotFoundException(ex)
    }
  }
}

case class PlanNotFoundException(inner: Exception) extends Exception(inner)

// vim: set sw=2 set softtabstop=2 et:
