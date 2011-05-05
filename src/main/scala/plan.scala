package org.thimblr {
  import net.liftweb.json._
  import net.liftweb.json.JsonParser._

  object Parsing {
    import java.io._
    import java.text._

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

    def when [T](predicate: => Boolean)(expression: => T) = {
      if(predicate) Some(expression) else None
    }

    class Slashable(s: String) {
      def / (at: Int) = (s take at, s drop (at+1))
    }

    implicit def string2Slashable(s: String) = new Slashable(s)

    implicit val formats = new DefaultFormats {
      override def dateFormatter = new SimpleDateFormat("yyyyMMddHHmmss")
    }
  }

  package plan {
    import Parsing._

    object Domainex {
      def unapply(str: String) = {
        val lastDot = str lastIndexOf "."
        when(lastDot>0) { 
          str/lastDot 
        }
      }
    }

    object Addressex {
      def unapply(str: String) = {
        val lastAt = str lastIndexOf "@"
        Some {
          if(lastAt>0)
            str/lastAt 
          else 
            (str, None)
        }
      }
    }
  }
}

// vim: set sw=2 set softtabstop=2 et:
