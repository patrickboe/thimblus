package org.thimblr.test.ui

import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.TestFailedException
import java.util.Date
import scala.swing._
import scala.swing.event._
import org.thimblr.ui._
import org.thimblr.model._
import org.thimblr.plan._

class UISuite extends WordSpec with ShouldMatchers {
  "Dispatch" should {
    "call model.post in response to a post button click event" in {
      var posted = ""
      val expected = "My Next Post..."
      val mockModel = new HomeModel(s => posted = s, ()=>null)
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
    "load the current plan on creation" in {
      val tastyPlan=Plan("tasty@burgers.com",null,null)
      val loadPlan=()=>tastyPlan
      val subject=new HomeModel(s=>Unit,loadPlan)
      subject.plan should equal (tastyPlan)
    }

    "load the latest plan after a post" in {
      var plan=Plan("one@test.com",null,null)
      val loadPlan=()=>plan
      val subject=new HomeModel(s=>Unit,loadPlan)
      plan=Plan("two@test.com",null,null)
      subject.post("blah")
      subject.plan should equal(plan)
    }

    "call its poster when post is called on it" in {
      var testVal = ""
      val expected ="bees are a beneficial animal"
      val subject = new HomeModel(testVal=_,()=>null)
      subject.post(expected)
      testVal should equal (expected)
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
