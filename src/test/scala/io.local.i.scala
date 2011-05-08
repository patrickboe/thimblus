package org.thimblr.test.io

import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import java.io._
import org.thimblr.io._
import java.util.UUID

class PlanFileSpec extends WordSpec with ShouldMatchers {
  val path = "src/test/resources/testplans/.plan.io"
  val badpath = "src/test/resources/testplans/.plan.missing"
  new File(path).createNewFile()
  
  "writerMaker" when {
    "called with a valid path" should {
      "return a lazy writer for the file in that path" in {
        val expected = "This is a unique test: " + UUID.randomUUID()
        val makeWriter = Local.writerMaker(path)
        val planWriter = makeWriter()
        try{
          planWriter.write(expected)
        } finally { 
          planWriter.close()
        }
        val testReader = new FileReader(path)
        try{
          val actual = Stream
                        .continually(testReader.read)
                        .takeWhile(_ != -1)
                        .map(_.toChar)
                        .mkString
          assert(actual == expected, actual + " was not " + expected)
        } finally {
          testReader.close()
        }
      }
    }

    "called with an invalid path" should {
      "throw a corresponding PlanNotFound exception" in {
        val makeWriter=Local.writerMaker(badpath)
        val ex = intercept[PlanNotFoundException] {
          val planWriter=makeWriter()
        }
        val cause = ex.getCause
        assert(cause.getMessage == "There is no plan file at " + badpath)
        assert(cause.isInstanceOf[FileNotFoundException])
        val expectedMessage = cause.toString
        assert(ex.getMessage==expectedMessage, 
          "expected this exception message: " + expectedMessage + ", but got this: " + ex.getMessage)
      }
    }
  }

  "readerMaker" when {
    "called with a valid path" should {
      "return a lazy reader from the file in that path" in {
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
        val makeReader=Local.readerMaker(badpath)
        val ex = intercept[PlanNotFoundException] {
          val planReader=makeReader()
        }
        val cause = ex.getCause
        assert(cause.isInstanceOf[FileNotFoundException])
        val expectedMessage = cause.toString
        assert(ex.getMessage==expectedMessage, 
          "expected this exception message: " + expectedMessage + ", but got this: " + ex.getMessage)
      }
    }
  }
}
// vim: set sw=2 set softtabstop=2 et: