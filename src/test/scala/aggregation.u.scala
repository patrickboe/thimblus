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
package org.thimblus.test.aggregation

import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.TestFailedException
import java.util.Date
import org.thimblus.plan._
import org.thimblus.Aggregation._

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
