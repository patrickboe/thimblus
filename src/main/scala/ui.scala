package org.thimblus.ui

import scala.swing._
import scala.swing.event._
import org.thimblus.model._

case class Dispatch(view: View, model: HomeModel) extends Reactor {
  listenTo(view.post)
  reactions += {
    case ButtonClicked(view.post) => { 
        model.post(view.message.text)
        view.message.text=""
      }
  }
}

trait View extends Reactor {
  val model: HomeSource
  val post: Publisher
  val message: TextComponent
  listenTo(model)
}

// vim: set sw=2 set softtabstop=2 et:
