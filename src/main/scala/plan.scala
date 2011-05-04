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

    def when [T](predicate: => Boolean)(expression: => T) = {
      if(predicate) Some(expression) else None
    }

    class Slashable(s: String) {
      def / (at: Int) = (s take at, s drop (at+1))
    }

    implicit def string2Slashable(s: String) = new Slashable(s)
  }

  package plan {

    import Parsing._
    import net.liftweb.json.JsonParser._

    object Domainex {
      def unapply(str: String) = {
        val lastDot = str lastIndexOf "."
        when(lastDot>0) { 
          str/lastDot 
        }
      }
    }

    object Timex {
      def unapply(str: String) = {
        when(str.length==14 && isLong(str)){
          val intAt = (start: Int,stop: Int) => str.substring(start,stop).toInt
          Time(intAt(0,4),intAt(4,6),intAt(6,8),intAt(8,10),intAt(10,12),intAt(12,14))
        }
      }

      private def isLong(str: String) = {
        try { str.toLong>0 } catch { case ex: NumberFormatException => false }
      }
    }

    case class Time(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int)

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
