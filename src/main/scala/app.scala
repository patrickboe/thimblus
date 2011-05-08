package org.thimblr.swing

import scala.swing._
import org.thimblr.model._

class App
  extends SimpleSwingApplication 
  with org.thimblr.ui.View {
  
  val model = HomeModel(s=>Unit)
  val post = new Button { text = "post" }
  val message = new TextField

  def top = new MainFrame {
    title = "Thimblr"
    contents = new BoxPanel(Orientation.Vertical) {
    }
  }
}

// vim: set ts=2 sw=2 set softtabstop=2 et:
