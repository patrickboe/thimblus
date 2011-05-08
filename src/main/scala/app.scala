package org.thimblr.swing

import scala.swing._

object App 
  extends SimpleSwingApplication 
  with org.thimblr.ui.View {
  
  val Post = new Button { text = "post" }

  def top = new MainFrame {
    title = "Thimblr"
    contents = new BoxPanel(Orientation.Vertical) {
    }
  }
  
}

// vim: set ts=2 sw=2 set softtabstop=2 et:
