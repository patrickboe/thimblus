package org.thimblus.swing

import scala.swing._
import org.thimblus.model._

trait SwingView
  extends SimpleSwingApplication 
  with org.thimblus.ui.View {

  val myPosts = new TextArea
  val post = new Button { text = "post" }
  val message = new TextField
  
  def top = new MainFrame {
    title = "Thumblus"
    contents = new BoxPanel(Orientation.Vertical) {
      contents += message
      contents += post
      contents += myPosts
    }
  }
}

// vim: set ts=2 sw=2 set softtabstop=2 et:
