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
package org.thimblus.test.io

import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.TestFailedException
import org.thimblus.io.Resource
import org.thimblus.io.ResourceControl._

class ResourceSpec extends WordSpec with ShouldMatchers {
  "using" when {
    val disposable = ()  => new Resource { 
      var disposed = false
      def dispose() = {
        disposed=true  
      }
    }

    "there is an error" should {
      "run its function and dispose" in {
        val disp = disposable()
        var started=false
        var finished=false
        intercept[Exception] {
          using(disp) { d =>
            started=true
            throw new Exception
            finished=true
          }
        }
        started should be (true)
        finished should be (false)
        disp.disposed should be (true)  
      }
    }

    "there is no error" should {
      "run its function and dispose" in {
        val disp = disposable()
        var ran=false
        using(disp) { d =>
          ran=true
        }
        ran should be (true)
        disp.disposed should be (true)
      }
    }
  }
}

// vim: set sw=2 set softtabstop=2 et:

