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

trait Recorder { def record(content: String) }

object IO { 
  def stringify (planReader: => Reader) = {
    val reader = new BufferedReader(planReader)
    try {
      Stream
        .continually(reader.read)
        .takeWhile(_ != -1)
        .map(_.toChar)
        .mkString
    } finally {
      reader.close()
    }
  }

  def streamify (string: String, planWriter: =>Writer) = {
    val writer = new BufferedWriter(planWriter)
    try {
      writer.write(string)
    } finally {
      writer.close()
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

// vim: sw=2:softtabstop=2:et:
