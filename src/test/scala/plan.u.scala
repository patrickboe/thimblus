package org.thimblr.test.plan

import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.TestFailedException
import java.io._
import net.liftweb.json.JsonAST._
import org.thimblr.Plan._

class PlanFileSpec extends WordSpec with ShouldMatchers {
  "readToString" when {
    "passed a makeReader function" should {
      "return the full string from the reader it produces" in {
        val expected = "This is a test string"
        val reader = new StringReader(expected)
        val actual = stringify(reader)
        assert(actual==expected, "result " + actual + " does not match expected output")
        try {
          intercept[IOException] { val test = reader.read }
        } catch {
          case ex: TestFailedException => fail("stringify should close reader after use",ex)
        }
      }
    }
    
    "receiving an exception from its reader" should {
      "close its reader and rethrow the exception" in {
        val failPlanReader = new FailReader()
        val readException = intercept[IOException] { 
          stringify(failPlanReader)
        }
        assert(readException.getMessage=="failing on read by design", 
          "stringify rethrew the wrong exception: " + readException.toString)
        val afterErrorReadException = intercept[IOException] { val test = failPlanReader.read() }
        assert(afterErrorReadException.getMessage=="can't read because the reader is closed",
          "reader is not throwing the expected IOException for a closed reader, so it's probably been left open.")
      }
    }
  }

  //parse json
  //convert an address string to an address obj
  //convert a time string to a time obj
  //convert a message pair to a message obj
  //sort a list of addresses
  //sort a list of messages
  //build a plan from a sorted list of messages, a sorted list of followers, and an address

  "parseJSON" when {
    "passed a plan string" should {
      "return a corresponding plan case class" in {
        val testJSON =
"""
{
  "following": [
      {"address": "djjz@wphila.gov"},
      {"address": "freshp@belaire.gov"}
    ],
  "address": "worldwide@phila.gov",
  "messages": [
      {"text": "I'm risin' to the shine", "time": "12:20"},
      {"text": "I ain't left the rest", "time": "3:37"},
      {"text": "Laying around, louging.", "time": "2:00"}
    ]
}
"""
        val plan = parseJSON(testJSON)
        val JArray(following) = plan \ "following"
        val JArray(messages) = plan \ "messages"
        val JString(firstFollowed) = following.head \ "address"
        val JString(address) = plan \ "address"
        val JString(firstMessage) = messages.head \ "text"
        assert(firstFollowed == "djjz@wphila.gov",
          "unexpected first address: " + firstFollowed)
        assert(following.size == 2, "unexpected following size")
        assert(address == "worldwide@phila.gov", "unexpected address")
        assert(firstMessage =="I'm risin' to the shine", "unexpected first status")
        assert(messages.size == 3, "unexpected messages size")
      }
    }
  }
}

class FailReader extends Reader {
  var onReadException = new IOException("failing on read by design")
  override def close() = {
    onReadException = new IOException("can't read because the reader is closed")  
  }
  override def read(cbuf: Array[Char],off: Int, len: Int): Int = {
    throw onReadException
  }
}

// vim: set sw=2 set softtabstop=2 et:
