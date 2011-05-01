package org.thimblr

import java.io.{Reader,BufferedReader}

object Plan {

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

  def parseJSONPlan (plan: String) = {
    null
  }
}


// vim: set sw=2 set softtabstop=2 et:
