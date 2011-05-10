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

  def streamify (string: String, planWriter: =>Writer) = {
    val writer = new BufferedWriter(planWriter)
    try {
      writer.write(string)
    } finally {
      writer.close()
    }
  }
}

// vim: set sw=2 set softtabstop=2 et:
