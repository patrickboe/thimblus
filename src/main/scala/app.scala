package org.thimblr.swing

import org.thimblr.model._

object App extends {
  val model = new HomeModel(
    s=>Unit,
    ()=>null
  )
} with SwingView

// vim: set ts=2 sw=2 set softtabstop=2 et:
