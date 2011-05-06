package org.thimblr.test.plan

import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.TestFailedException
import java.io._
import java.util._
import net.liftweb.json._
import net.liftweb.json.JsonAST._
import org.thimblr.plan._
import org.thimblr.plan.Parsing._

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

  //convert a message pair to a message obj
  //sort a list of addresses
  //sort a list of messages
  //build a plan from a sorted list of messages, a sorted list of followers, and an address
  "Date" when {
    val nyc = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"),Locale.US)
    val chi = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"),Locale.US)
    "extracted from a correct GMT string" should {
      val d = parse("[\"20110315144022\"]")(0).extract[Date]

      "match the time minus four hours if you're in New York" in {
        nyc.setTime(d)
        assert(nyc.get(Calendar.YEAR)==2011 &&
          nyc.get(Calendar.MONTH)==Calendar.MARCH &&
          nyc.get(Calendar.DAY_OF_MONTH)==15 &&
          nyc.get(Calendar.HOUR_OF_DAY)==10 &&
          nyc.get(Calendar.MINUTE)==40 &&
          nyc.get(Calendar.SECOND)==22)
      }

      "match the time minus five hours if you're in Chicago" in {
        chi.setTime(d)
        assert(chi.get(Calendar.HOUR_OF_DAY)==9)
      }
    }

    "extracted in New York from a New York zone-adjusted string" should {
      "match the time exactly" in {
        val d = parse("[\"20110315144022-0400\"]")(0).extract[Date]
        nyc.setTime(d)
        assert(nyc.get(Calendar.HOUR_OF_DAY)==14)
      }
    }
  }

  "Domainex" when {
    "passed a multi-part name" should {
      "put the last part in the top field" in {
        val Domainex(sub, top) = "wphila.pa.state.gov"
        assert(sub == "wphila.pa.state")
        assert(top == "gov")
      }
    }

    "passed a single-part name" should {
      "not match" in {
        intercept[MatchError]{ val Domainex(sub, top) = "pickles" }
      }
    }
  }

  "Addressex" when {
    "passed a standard email" should {
      "separate the username and domain" in {
        val Addressex(name, domain) = "fprince@belaire.ca.state.gov"
        assert(name == "fprince")
        assert(domain == "belaire.ca.state.gov")
      }
    }

    "passed just a username" should {
      "match the username" in {
        val Addressex(name, domain) = "fprince"
        assert(name =="fprince")
        assert(domain==None)
      }
    }
  }

  val testPlan = """
  {
    "following": [
        {"address": "djjz@wphila.pa.state.gov"},
        {"address": "freshp@belaire.ca.state.gov"}
      ],
    "address": "worldwide@phila.gov",
    "messages": [
        {"text": "I'm risin' to the shine", "time": "12:20"},
        {"text": "I ain't left the rest", "time": "3:37"},
        {"text": "Laying around, louging.", "time": "2:00"}
      ]
  }
  """
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
