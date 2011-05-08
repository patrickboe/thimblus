package org.thimblr.test.ui

import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.TestFailedException
import scala.swing._
import scala.swing.event._
import org.thimblr.ui._
import org.thimblr.model._

class UISuite extends WordSpec with ShouldMatchers {
  "dispatch" should {
    "call model.post in response to a post button click event" in {
      var posted = false
      val mockModel = HomeModel(s => posted = true)
      val mockView = new View { val Post = new Button() }
      val dispatch = Dispatch(mockView,mockModel)
      dispatch(ButtonClicked(mockView.Post))
      assert(posted)
    }
  }
}

// vim: set sw=2 set softtabstop=2 et:
