package org.thimblr {

  object Util {

    def when [T](predicate: => Boolean)(expression: => T) = {
      if(predicate) Some(expression) else None
    }

    class Slashable(s: String) {
      def / (at: Int) = (s take at, s drop (at+1))
    }

    implicit def string2Slashable(s: String) = new Slashable(s)

    class Questionable[T](thing: T) {
      def ?? (fallback: T) = {
        if(thing!=null) thing else fallback
      }
    }

    implicit def thing2Questionable[T](thing: T) = new Questionable[T](thing)

  }
    
  package plan {
    import Util._
    import java.util.{TimeZone,Date}
    import java.io._
    import net.liftweb.json._
    import net.liftweb.json.JsonParser._
    import java.text._

    object Parsing {
      implicit val formats = new ThimblrFormats()

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

    case class Plan(address: String, following: List[Follower], messages: List[Message])
    
    case class Follower(address: String)

    case class Message(text: String, time: String)

    class ThimblrFormats extends Formats {
      
      private val full=new SimpleDateFormat("yyyyMMddHHmmssz")
      private val lenient=new SimpleDateFormat("yyyyMMddHHmmss")
      private val utc=TimeZone.getTimeZone("UTC")
      full.setTimeZone(utc)
      lenient.setTimeZone(utc)

      val dateFormat = new net.liftweb.json.DateFormat {    
        def parse(s: String) = try {
          Some(full.parse(s))
        } catch {
          case e: java.text.ParseException => try {
              Some(lenient.parse(s))
            } catch {
              case e: java.text.ParseException => None
            }
        }

        def format(d: Date) = full.format(d)
      }
    }
  }
}

// vim: set sw=2 set softtabstop=2 et:
