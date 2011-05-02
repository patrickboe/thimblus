package org.thimblr

import net.liftweb.json.JsonParser._
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

  def parseJSON (json: String) = {
    parse(json)  
  }
}


// vim: set sw=2 set softtabstop=2 et:
