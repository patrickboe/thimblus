package org.thimblr.ui

import scala.swing._
import scala.swing.event._
import org.thimblr.model._

case class Dispatch(view: View, model: HomeModel){
  def apply(e: Event) = {
    model.post("something")
  }
}

trait View {
  val Post: Publisher
}

// vim: set sw=2 set softtabstop=2 et:
