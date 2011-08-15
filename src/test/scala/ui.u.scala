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
package org.thimblus.test.ui

import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.TestFailedException
import scala.swing._
import scala.swing.event._
import org.thimblus.ui._
import org.thimblus.model._
import org.thimblus.data._

class UISuite extends WordSpec with ShouldMatchers {
  "Controller" should {
    "call model.post in response to a post button click event" in {
      var posted = ""
      val expected = "My Next Post..."
      val mockModel = new HomeModel((x,y,s) => posted = s, ()=>(null,null))
      val mockView = new {
          val model = mockModel
        } with View { 
          val post = new Button()
          val message = new TextField(expected)
        }
      val dispatch = Controller(mockView,mockModel)
      mockView.post.doClick
      posted should equal (expected)
      mockView.message.text should equal ("")
    }
  }
}

// vim: sw=2:softtabstop=2:et:
