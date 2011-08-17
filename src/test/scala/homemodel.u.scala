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

import java.util.Date
import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.TestFailedException
import org.thimblus.model._
import org.thimblus.data._
import org.thimblus.io.IO.using
import org.thimblus.repo._
import akka.actor._
import scala.swing._
import akka.config.Supervision._
import akka.event.EventHandler
import Actor._

class HomeModelSuite extends WordSpec with ShouldMatchers {
  val curPlan=new Plan(null,Nil,Nil)
  val curTime = new Date()
  val metaData = "bunnies are delicious"

  class SvcStub(mockRepo: ActorRef) {
    var svcIsOpen=false
    val svc = new IPlanDispatch {
      private val repo = mockRepo
      def getRepo() = {
        svcIsOpen=true
        repo.start()
      }
      def close() = {
        svcIsOpen=false
        repo.stop()
      }
    }
    def time() = curTime
  }

  "HomeModelA" should {
    """set its store's plan to what it gets back in
    response to a load message to its repository""" in {
      val svc = new SvcStub(
        actorOf(new Actor{
          def receive = {
            case PlanRequest => self.channel ! (metaData,curPlan)
          }
        })
      )
      val model = new HomeModelA(svc.svc,svc.time)
      Thread.sleep(10)
      model.plan should equal (curPlan)
      model.close()
      svc.svcIsOpen should be (false)
    }

    """publish changes to its plan""" in {
      val svc = new SvcStub(
        actorOf(new Actor{ def receive = {case _ =>} })
      )
      val updatedPlan=Plan("ho",Nil,Nil)
      val model = new HomeModelA(svc.svc,svc.time)
      val listener = new Reactor {
        var plan: Plan = null
        reactions += { case PlanUpdate(x) => plan=x }
        listenTo(model)
      }
      model.plan = updatedPlan
      listener.plan should equal (updatedPlan)
    }

    """respond to a new post by adding it to the current plan,
    flushing it to the repository, and returning the new plan""" in {
      val newPost = "GrApE nUtS!"
      val newPlan = curPlan + Message(newPost,curTime)
      var flushed = false
      val svc = new  SvcStub(
        actorOf(new Actor{
          def receive = { 
            case (m,p) => {
              m should equal (metaData)
              p should equal (newPlan)
              flushed = true
            } 
            case _ => self.channel ! (metaData, curPlan)
          }
        })
      )
      val model = new HomeModelA(svc.svc,svc.time)
      Thread.sleep(10)
      model.post(newPost)
      model.plan should equal (newPlan)
      Thread.sleep(10)
      model.close()
      flushed should be (true)
    }

    """be able to clean up all its resources in an immediate error""" ignore {
    }
  }

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
}
