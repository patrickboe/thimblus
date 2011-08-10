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
package org.thimblus.test.plan

import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.TestFailedException
import java.util.Date
import java.text.SimpleDateFormat
import net.liftweb.json._
import net.liftweb.json.Serialization.{read,write}
import net.liftweb.json.JsonAST._
import org.thimblus.Util._
import org.thimblus.data._

class PlanSpec extends WordSpec with ShouldMatchers {

  object Fixtures {
    val mockingTimeString = "5/7/11 9:09 AM"
    val mockingTime = new SimpleDateFormat("M/d/y h:m a").parse(mockingTimeString)
    implicit val formats = new Formats {
      val dateFormat = new net.liftweb.json.DateFormat {
        def parse(s:String) = when (s==mockingTimeString) { mockingTime }
        def format(d: Date) = if (d==mockingTime) mockingTimeString else ""
      }
    }
    val testPlan = String.format("""
    {
      "following": [
          {"address": "djjz@wphila.pa.state.gov"},
          {"address": "freshp@belaire.ca.state.gov"}
        ],
      "address": "worldwide@phila.gov",
      "messages": [
          {"text": "I'm risin' to the shine", "time": "%1$s"},
          {"text": "I ain't left the rest",   "time": "%1$s"},
          {"text": "Laying around, louging.", "time": "%1$s"}
        ]
    }
    """,mockingTimeString)
  }

  import Fixtures._

  "Follower" should {
    "match followers" in {
      val f = read[Follower]("""{"address": "123 Brown Street"}""")
      assert(f.address=="123 Brown Street")
    }

    "serialize reversibly to a string" in {
      val f = Follower("bob@twpks.gov")
      val json = write(f)
      val doubleParsed = read[Follower](json)
      assert(f==doubleParsed)
    }
  }

  "Message" should {
    "match messages" in {
      val jsonMsg =(parse(testPlan) \ "messages")(0)
      val m = jsonMsg.extract[Message]
      assert(m.text == "I'm risin' to the shine", "text value " + m.text + " unexpected")
      assert(m.time == mockingTime, 
        "expected time value " + mockingTime + 
        ", got " +  m.time)
    }
    
    "serialize reversibly to a string" in {
      val m = Message("test message", mockingTime)
      val json = write(m)
      val doubleParsed = read[Message](json)
      assert(m==doubleParsed)
    }
  }

  "Plan" should {
    val p=read[Plan](testPlan)

    "match a string" in {
      assert(p.address=="worldwide@phila.gov")
      assert(p.following.length==2)
      assert(p.messages.length==3)
      assert(p.following.head==Follower("djjz@wphila.pa.state.gov"))
      assert(p.messages.head== Message( "I'm risin' to the shine",  mockingTime))
    }

    "serialize reversibly to a string" in {
      val planString=write(p)
      val doubleParsed=read[Plan](planString)
      assert(doubleParsed==p)
    }

    "add Message posts" in {
      val newPost = Message("Meet us up later on at the Plat",new Date())
      val newPlan = p + newPost
      val latest = newPlan.messages.head
      latest should equal (newPost)
      newPlan should not be {equal (p)}
    }

    "add String posts" in {
      val newPost = "Meet us up later on at the Plat"
      val newPlan = p + newPost
      val latest = newPlan.messages.head
      assert(latest.time after new Date(System.currentTimeMillis-5000), 
        "newest post should have a recent date.")
      latest.text should equal (newPost)
      newPlan should not be {equal (p)}
    }
  }
}

// vim: sw=2:softtabstop=2:et:
