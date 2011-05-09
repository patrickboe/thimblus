package org.thimblr.swing

import org.thimblr.model.HomeModel
import org.thimblr.ui.Dispatch

object App extends {
  val model = new HomeModel(
    s=>Unit,
    ()=>null
  )
} with SwingView {
  Dispatch(this,model)
}

// vim: set ts=2 sw=2 set softtabstop=2 et:
