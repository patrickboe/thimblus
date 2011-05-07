package org.thimblr.io

import java.io._

object IO { 
  def stringify (planReader: => Reader) = {
    val reader = new BufferedReader(planReader)
    try {
      Stream
        .continually(reader.read)
        .takeWhile(_ != -1)
        .map(_.toChar)
        .mkString
    } finally {
      reader.close()
    }
  }
}

// vim: set sw=2 set softtabstop=2 et:
