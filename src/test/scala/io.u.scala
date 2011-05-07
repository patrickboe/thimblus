package org.thimblr.test.plan

import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.TestFailedException
import java.io._
import org.thimblr.io.IO._

class IOSpec extends WordSpec with ShouldMatchers {
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
        val readException = intercept[IOException] { 
          stringify(failReader)
        }
        assert(readException.getMessage=="failing on read by design", 
          "stringify rethrew the wrong exception: " + readException.toString)
        val afterErrorReadException = intercept[IOException] { val test = failReader.read() }
        assert(afterErrorReadException.getMessage=="can't read because the reader is closed",
          "reader is not throwing the expected IOException for a closed reader, so it's probably been left open.")
      }
    }
  }

  val failReader = new Reader {
    var onReadException = new IOException("failing on read by design")
    override def close() = {
      onReadException = new IOException("can't read because the reader is closed")  
    }
    override def read(cbuf: Array[Char],off: Int, len: Int): Int = {
      throw onReadException
    }
  }
}

// vim: set sw=2 set softtabstop=2 et:
