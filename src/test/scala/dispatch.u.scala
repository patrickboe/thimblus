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
      val mockModel = HomeModel(s => posted = s)
      val mockView = new View { 
        val model = mockModel
        val post = new Button()
        val message = new TextField(expected)
      }
      val dispatch = Dispatch(mockView,mockModel)
      dispatch(ButtonClicked(mockView.post))
      posted should equal (expected)
      mockView.message.text should equal ("")
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
