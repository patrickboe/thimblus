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
package org.thimblus.local

import org.thimblus.io._
import java.io._
import org.thimblus.io.IO._

object LocalIO {
  def makeLoader(path: String) = () => {
    try {
      stringify(new FileReader(path))
    } catch { 
      case ex: FileNotFoundException => throw PlanNotFoundException(ex)
    }
  }

  def makeRecorder(path: String)(content: String) = { 
    if(!new File(path).exists) {
      throw PlanNotFoundException(new FileNotFoundException("There is no plan file at "+path))
    }
    streamify(content,new FileWriter(path))
  }
}

case class PlanNotFoundException(cause: Exception) extends Exception(cause)

// vim: sw=2:softtabstop=2:et:
