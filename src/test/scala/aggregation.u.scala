package org.thimblr.test.aggregation

import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.TestFailedException
import java.util.Date
import org.thimblr.plan._
import org.thimblr.Aggregation._

class AggregationSuite extends WordSpec with ShouldMatchers {
  "sort" should {
    "sort the messages in a plan by time" in {
      val sortedPlan=sort(unsortedPlan)
      val sortedTexts = (for(m <- sortedPlan.messages) yield m.text) mkString "\n"
      sortedTexts should equal (
"""Hello
Is it me you're looking for?
I can see it in your eyes.
I can see it in your smile."""
      )
    }
  }

  val now=System.currentTimeMillis
  val unsortedPlan = 
    Plan(
      "myname@me.com",
      List(Follower("test@test.com"),Follower("alpha@beta.com")),
      List(
        Message("Is it me you're looking for?",new Date(now-2000)),
        Message("I can see it in your eyes.",new Date(now-3000)),
        Message("Hello",new Date(now-1000)),
        Message("I can see it in your smile.",new Date(now-4000))
      )
    )
}

// vim: set sw=2 set softtabstop=2 et:
