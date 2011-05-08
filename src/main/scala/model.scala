package org.thimblr.model

import scala.swing._
import scala.swing.event._
import org.thimblr.plan._

class HomeModel(poster: (String)=>Unit, loadPlan: ()=>Plan) extends HomeSource {
  plan=loadPlan()
  def post(s: String) = {
    poster(s)
    plan=loadPlan()
  }
}

trait HomeSource extends Publisher with PlanWatcher {
  private[this] var p: Plan = null
  def plan: Plan = p
  def plan_= (x: Plan) {
    p=x
    publish(PlanUpdate(this))
  }
}

case class PlanUpdate(source: HomeSource) extends Event

// vim: set sw=2 set softtabstop=2 et:
