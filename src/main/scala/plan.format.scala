package org.thimblr.plan

import org.thimblr.Util._
import java.util.{Date,TimeZone}
import java.text.SimpleDateFormat
import net.liftweb.json._

object Domainex {
  def unapply(str: String) = {
    val lastDot = str lastIndexOf "."
    when(lastDot>0) { 
      str/lastDot 
    }
  }
}

object Planex {
  def apply(metadata: String, plan: String) = {
    String.format("%1$sPlan:\n%2$s", metadata, plan)
  }
  def unapply(str: String) = {
    val ixPlanLabel = str indexOf "Plan:"
    when(ixPlanLabel>=0) {
      (str take ixPlanLabel, (str drop (ixPlanLabel+5)).trim)
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

class ThimblrFormats(userZone: TimeZone) extends Formats {
  
  private val full=new SimpleDateFormat("yyyyMMddHHmmssZ")
  private val lenient=new SimpleDateFormat("yyyyMMddHHmmss")
  full.setTimeZone(userZone)
  lenient.setTimeZone(TimeZone.getTimeZone("UTC"))

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
// vim: set sw=2 set softtabstop=2 et:
