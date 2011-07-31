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
  "HomeModel" should {
    "load the current plan and metadata on creation" in {
      val tastyPlan=Plan("tasty@burgers.com",null,null)
      val yumData="Contents of drawer: a dozen donuts"
      val loadPlan=()=>(yumData,tastyPlan)
      val subject=new HomeModel((x,y,z)=>Unit,loadPlan)
      subject.plan should equal (tastyPlan)
      subject.metadata should equal (yumData)
    }

    "load the latest plan, but don't load metadata, after a post" in {
      var plan=Plan("one@test.com",null,null)
      var metadata="first metadata"
      val loadPlan=()=>(metadata,plan)
      val subject=new HomeModel((x,y,z)=>Unit,loadPlan)
      plan=Plan("two@test.com",null,null)
      metadata="second metadata"
      subject.post("blah")
      subject.plan should equal(plan)
      subject.metadata should equal ("first metadata")
    }

    "call its poster when post is called on it" in {
      var called=false
      val expectedPost ="bees are a beneficial animal"
      val expectedMetadata = "some people hate bees"
      val expectedPlan = Plan("wasp@hive.net",null,null)
      val subject = new HomeModel(
        (metadata,plan,post)=>
        {
          called=true
          post should equal(expectedPost)
          plan should equal(expectedPlan)
          metadata should equal (expectedMetadata)
        },
        ()=>(null,null))
      subject.metadata=expectedMetadata
      subject.plan=expectedPlan
      subject.post(expectedPost)
      assert(called)
    }
  }

  "HomeModelA" should {
    """set its store's plan to what it gets back in
    response to a load message to its repository""" in {
      val curPlan=new Plan(null,Nil,Nil)
      var svcIsOpen=false
      val svc = new PlanService {
        private val repo = 
          actorOf(new Actor{
            def receive = {
              case LoadRequest() => self.reply(curPlan)
              case _ =>
            }
          })
        def getRepo() = {
          svcIsOpen=true
          repo.start()
        }
        def close() = {
          svcIsOpen=false
          repo.stop()
        }
      }
      val store = new HomeStore {
        var plan: Plan=null
      }
      using(new HomeModelA(svc,store)) { model => }
      store.plan should equal (curPlan)
      svcIsOpen should be (false)
    }
  }

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
