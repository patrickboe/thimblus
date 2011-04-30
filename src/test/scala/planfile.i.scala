package org.thimblr.test.model

import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import java.io.FileWriter
import thimblr.Planfile._

class PlanFileSpec extends WordSpec with ShouldMatchers {
  "streamer" when {
    "called with a string argument" should {
      "return a function that streams from the file in that path" in {
        val planStream = streamer("src/test/resources/testplans/.plan.simple")()
        val actual = Stream
                      .continually(planStream.read)
                      .takeWhile(-1 !=)
                      .map(_.toChar)
                      .mkString
        val expected =
"""This file
Has three lines
Of text.
"""
        try {
          assert(actual == expected,
            "\n" + actual + " should have been\n" + expected + " but was not.")
        } finally {
          planStream.close()
        }
      }
    }
  }
}
// vim: set sw=2 set softtabstop=2 et:
