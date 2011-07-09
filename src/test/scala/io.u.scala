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
package org.thimblus.test.plan

import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.TestFailedException
import java.io._
import org.thimblus.io.IO._

class IOSpec extends WordSpec with ShouldMatchers {
  "stringify" when {
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
        val failReader = new Reader {
          var onReadException = new IOException("failing on read by design")
          override def close() = {
            onReadException = new IOException("can't read because the reader is closed")  
          }
          override def read(cbuf: Array[Char],off: Int, len: Int): Int = {
            throw onReadException
          }
        }
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

  "streamify" when {
    "passed a string and a makeWriter function" should {
      "write the string to the writer and close the writer" in {
        val mockWriter = new Writer {
          var writtenValue=""
          var finalValue=""
          override def close() = { 
            finalValue = writtenValue 
          }
          override def flush() = {}
          override def write(cbuf: Array[Char],off: Int, len: Int) = {
            writtenValue=cbuf.mkString.trim
          }
        }

        val test = "squishy"
        streamify(test,mockWriter)
        assert(test==mockWriter.finalValue, 
          "expected \"" + test + "\" but got \"" + mockWriter.finalValue + "\"")
      }
    }

    "receiving an exception from its writer" should {
      "close the writer and rethrow the exception" in {
        val failWriter = new Writer {
          var isClosed=false
          override def flush() = {}
          override def close() = {
            isClosed=true
          }
          override def write(cbuf: Array[Char],off: Int, len: Int) = {
            throw new IOException("can't write by design")
          }
        }
        intercept[IOException]{ streamify("test string",failWriter) }
        assert(failWriter.isClosed)
      }
    }
  }

}

// vim: sw=2:softtabstop=2:et:
