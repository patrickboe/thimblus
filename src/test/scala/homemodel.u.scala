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
import akka.actor._
import akka.config.Supervision._
import akka.event.EventHandler
import Actor._

class HomeModelSuite extends WordSpec with ShouldMatchers {
  val curPlan=new Plan(null,Nil,Nil)
  val curTime = new Date()
  val metaData = "bunnies are delicious"

  class svcStub(mockRepo: ActorRef) {
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
    val store = new HomeStore {
      var plan: Plan=null
      var metadata: String=null
    }
    def time() = curTime
  }

  "HomeModelA" should {
    """set its store's plan to what it gets back in
    response to a load message to its repository""" in {
      val svc = new svcStub(
        actorOf(new Actor{
          def receive = {
            case Request("plan") => self.reply(metaData,curPlan)
          }
        })
      )
      val model = actorOf(new HomeModelA(svc.svc,svc.store,svc.time))
      try{
        model.start()
        svc.store.plan should equal (null)
        (model !! Request("plan")) should equal (Some(curPlan))
      } finally {
        model.stop()
      }
      svc.store.plan should equal (curPlan)
      svc.store.metadata should equal (metaData)
      svc.svcIsOpen should be (false)
    }

    """respond to a new post by adding it to the current plan,
    flushing it to the repository, and returning the new plan""" in {
      val newPost = "GrApE nUtS!"
      val newPlan = curPlan + Message(newPost,curTime)
      var flushed = false
      val svc = new  svcStub(
        actorOf(new Actor{
          def receive = { case newPlan => flushed=true }
        })
      )
      svc.store.metadata=metaData
      svc.store.plan=curPlan
      val model = actorOf(new HomeModelA(svc.svc,svc.store,svc.time))
      try{
        model.start()
        (model !! newPost) should equal (Some(newPlan))
      } finally {
        model.stop()
      }
      svc.store.plan should equal (newPlan)
      svc.store.metadata should equal (metaData)
      flushed should be (true)
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
