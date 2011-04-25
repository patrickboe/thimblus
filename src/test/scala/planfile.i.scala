package org.thimblr.test.model

import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import java.io.FileWriter
import thimblr.Planfile._

class PlanFileSpec extends WordSpec with ShouldMatchers {
  "streamer" when {
    "called with a string argument" should {
      "return a function that streams from the file in that path" in {
        val testStreamer = streamer("test/testplans/.plan.simple") 
        val stream = testStreamer()
        try {
          assert(stream.toString() ==
"""This file
Has three lines
Of text."""
          )
        } finally {
          stream.close()
        }
      }
    }
  }
}
// vim: set sw=2 set softtabstop=2 et:
