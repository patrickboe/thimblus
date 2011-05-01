package org.thimblr.test.model

import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import java.io._
import org.thimblr.io._

class PlanFileSpec extends WordSpec with ShouldMatchers {
  "streamer" when {
    "called with a valid path" should {
      "return a lazy stream from the file in that path" in {
        val path = "src/test/resources/testplans/.plan.simple"
        val overwriter = new FileWriter(path)
        try {
          overwriter.write(
"""This file
Has three lines
Of text."""
          )
        } finally { overwriter.close() }
        val makeStream = Local.streamer(path)
        val appender = new FileWriter(path,true)
        try { appender.write("YEAAAAAAAAAA!") } finally { appender.close() }
        val expected =
"""This file
Has three lines
Of text.YEAAAAAAAAAA!"""
        lazy val planStream = makeStream()
        try {
          val actual = Stream
                        .continually(planStream.read)
                        .takeWhile(_ != -1)
                        .map(_.toChar)
                        .mkString
          assert(actual == expected,
            "\n" + actual + " should have been\n" + expected + " but was not.")
        } finally {
          planStream.close()
        }
      }
    }
    
    "called with an invalid path" should {
      "throw a corresponding PlanNotFound exception" in {
        val path = "src/test/resources/testplans/.plan.missing"
        val makeStream=Local.streamer(path)
        val ex = intercept[PlanNotFoundException] {
          val planStream=makeStream()
        }
        val cause = ex.getCause
        intercept[FileNotFoundException] { throw cause }
        val expectedMessage = cause.toString
        assert(ex.getMessage==expectedMessage, 
          "expected this exception message: " + expectedMessage + ", but got this: " + ex.getMessage)
      }
    }
  }
}
// vim: set sw=2 set softtabstop=2 et:
