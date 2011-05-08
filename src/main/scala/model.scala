package org.thimblr.model

import scala.swing._
import scala.swing.event._
import org.thimblr.plan._

case class HomeModel(post: String=>Unit) extends HomeSource

trait HomeSource extends Publisher {
  private[this] var p: Plan = null
  def plan: Plan = p
  def plan_= (x: Plan) {
    p=x
    publish(PlanUpdate(this))
  }
}

case class PlanUpdate(source: HomeSource) extends Event

// vim: set sw=2 set softtabstop=2 et:
