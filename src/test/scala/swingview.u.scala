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
package org.thimblus.swing

import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.TestFailedException
import scala.swing._
import org.thimblus.model._
import org.thimblus.plan._

class SwingSuite extends WordSpec with ShouldMatchers {
  "myPosts" should { 
    val startingMessages=
      Message("Second Message",null) :: Message("First Message",null) :: Nil
    val startingPlan=Plan(null,Nil,startingMessages)
    val testModel= new HomeSource {
        plan=startingPlan
    }
    val tester  = new { 
      val model=testModel 
    } with SwingView 

    "maintain a formatted list of past posts" in {
      val newMessages= 
        Message("Third Message",null) :: startingMessages
      val updatedPlan=Plan(null,Nil,newMessages)
      val startingPosts = tester.myPosts.text
      testModel.plan=updatedPlan
      val updatedPosts = tester.myPosts.text
      startingPosts should equal (
"""Second Message
First Message"""
      )
      updatedPosts should equal (
"""Third Message
Second Message
First Message"""
      )
    }

    "have an appropriate title" in {
      val title=tester.top.title
      title should equal ("Thimblus")
    }

    "put its myPosts, post, and message controls in a main frame" in {
      val bp=tester.top.contents.head.asInstanceOf[BoxPanel]
      val contents = bp.contents
      contents should have length (3)
      contents.filter(c=>c eq tester.myPosts) should have length (1)
      contents.filter(c=>c eq tester.post) should have length (1)
      contents.filter(c=>c eq tester.message) should have length (1)
    }
  }
}
// vim: sw=2:softtabstop=2:et:
