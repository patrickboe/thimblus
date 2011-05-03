package org.thimblr {

  object Parsing {
    import java.io.{Reader,BufferedReader}

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

    class Slashable(s: String) {
      def / (at: Int) = (s take at, s drop (at+1))
    }

    implicit def string2Slashable(s: String) = new Slashable(s)
  }

  package plan {

    import Parsing._
    import net.liftweb.json.JsonParser._

    object Domain {
      def unapply(str: String) = {
        val lastDot = str lastIndexOf "."
        if(lastDot>0) Some(str/lastDot) else None
      }
    }

    object Address {
      def unapply(str: String) = {
        val lastAt = str lastIndexOf "@"
        if(lastAt>0) Some(str/lastAt) else None
      }
    }
  }
}

// vim: set sw=2 set softtabstop=2 et:
