package org.thimblr.io

import java.io._

object Local {
  def readerMaker(path: String) = () => {
    try {
      new FileReader(path)
    } catch { 
      case ex: FileNotFoundException => throw PlanNotFoundException(ex)
    }
  }
}

case class PlanNotFoundException(cause: Exception) extends Exception(cause)

// vim: set sw=2 set softtabstop=2 et:
