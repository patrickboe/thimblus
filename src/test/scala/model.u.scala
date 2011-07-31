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
package org.thimblus.test.model

import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.TestFailedException
import scala.swing._
import scala.swing.event._
import org.thimblus.ui._
import org.thimblus.model._
import org.thimblus.data._
import org.thimblus.io.IO.using
import akka.actor.Actor
import akka.event.EventHandler
import Actor._

class ModelSuite extends WordSpec with ShouldMatchers {
  "HomeSource" should {
    "publish an event when its plan gets updated" in {
      var updated=false
      val testHomeSource = new HomeSource{}
      val testPlan=Plan("test@test.com",null,null)
      val tester = new Reactor {
        listenTo(testHomeSource)
        reactions += {
          case PlanUpdate(p) => updated = (p == testPlan)
          case _ =>
        }
      }

      testHomeSource.plan=testPlan
      assert(updated)
    }
  }
}
