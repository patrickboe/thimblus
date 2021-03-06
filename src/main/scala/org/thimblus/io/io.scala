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
package org.thimblus.io

import java.io._
import java.nio.charset._

object IO { 
  def stringify (reader: Reader) = {
    using(new BufferedReader(reader)) { br=>
      Stream
        .continually(br.read)
        .takeWhile(_ != -1)
        .map(_.toChar)
        .mkString
    }
  }

  def streamify (string: String, writer: Writer) = {
    using(new BufferedWriter(writer)) { bw=>
      bw.write(string)
      bw.flush()
    }
  }

  def using[A,R<:Closeable](r : R)(f : R=>A) : A = {
    try{
      f(r)
    } finally {
      r.close()
    }
  }
}

case class PlanNotFoundException(cause: Exception) extends Exception(cause)

case class ThimblCharset(charset: Charset)

case class Path(file: String, directory: String)

trait Destination extends Path {
  def pickle(s: String): Array[Byte]
  val mode: String
} 

// vim: sw=2:softtabstop=2:et:
