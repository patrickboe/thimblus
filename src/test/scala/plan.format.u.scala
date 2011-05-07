package org.thimblr.test.plan

import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.TestFailedException
import net.liftweb.json._
import java.util.{Locale,Calendar,TimeZone,Date}
import org.thimblr.Util._
import org.thimblr.plan.Format._

class PlanFormatSpec extends WordSpec with ShouldMatchers {
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
}


// vim: set sw=2 set softtabstop=2 et:
