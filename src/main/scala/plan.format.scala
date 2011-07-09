/*
 * Copyright 2011, Patrick Boe
 * =========================== 
 * This program is distributed under the terms of the GNU General Public License.
 *
 * This file is part of Thimblus.
 * 
 * Thimblus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Thimblus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Thimblus.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.thimblus.plan

import org.thimblus.Util._
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

class ThumblusFormats(userZone: TimeZone) extends Formats {
  
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
// vim: sw=2:softtabstop=2:et:
