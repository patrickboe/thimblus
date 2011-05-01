package org.thimblr.test.io

import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import java.io._
import org.thimblr.io._

class PlanFileSpec extends WordSpec with ShouldMatchers {
  "readerMaker" when {
    "called with a valid path" should {
      "return a lazy reader from the file in that path" in {
        val path = "src/test/resources/testplans/.plan.simple"
        val overwriter = new FileWriter(path)
        try {
          overwriter.write(
"""This file
Has three lines
Of text."""
          )
        } finally { overwriter.close() }
        val makeReader = Local.readerMaker(path)
        val appender = new FileWriter(path,true)
        try { appender.write("YEAAAAAAAAAA!") } finally { appender.close() }
        val expected =
"""This file
Has three lines
Of text.YEAAAAAAAAAA!"""
        val planReader = makeReader()
        try {
          val actual = Stream
                        .continually(planReader.read)
                        .takeWhile(_ != -1)
                        .map(_.toChar)
                        .mkString
          assert(actual == expected,
            "\n" + actual + " should have been\n" + expected + " but was not.")
        } finally {
          planReader.close()
        }
      }
    }
    
    "called with an invalid path" should {
      "throw a corresponding PlanNotFound exception" in {
        val path = "src/test/resources/testplans/.plan.missing"
        val makeReader=Local.readerMaker(path)
        val ex = intercept[PlanNotFoundException] {
          val planReader=makeReader()
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
