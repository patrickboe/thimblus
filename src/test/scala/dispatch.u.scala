package org.thimblus.test.ui

import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.TestFailedException
import java.util.Date
import scala.swing._
import scala.swing.event._
import org.thimblus.ui._
import org.thimblus.model._
import org.thimblus.plan._

class UISuite extends WordSpec with ShouldMatchers {
  "Dispatch" should {
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
      val dispatch = Dispatch(mockView,mockModel)
      mockView.post.doClick
      posted should equal (expected)
      mockView.message.text should equal ("")
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

  "View" should {
    "subscribe to events on the model" in {
      object mockSource extends HomeSource
      val view = new { 
          val model = mockSource 
        } with View {
          val post = null
          val message = null
          var noticedPlanUpdate = false
          reactions += {
            case PlanUpdate(`model`)=>noticedPlanUpdate=true  
        }
      }
      mockSource.publish(PlanUpdate(mockSource))
      assert(view.noticedPlanUpdate)
    }
  }
  
  "HomeSource" should {
    "publish an event when its plan gets updated" in {
      var updated=false
      val testHomeSource = new HomeSource{}
      val tester = new Reactor {
        listenTo(testHomeSource)
        reactions += {
          case PlanUpdate(p) => updated = (p eq testHomeSource)
          case _ =>
        }
      }

      testHomeSource.plan=Plan("test@test.com",null,null)
      assert(updated)
    }
  }
}

// vim: set sw=2 set softtabstop=2 et:
