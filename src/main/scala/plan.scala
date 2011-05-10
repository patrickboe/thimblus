package org.thimblus.plan

import org.thimblus.Util._
import java.util.Date
import net.liftweb.json._
import net.liftweb.json.JsonParser._

trait PlanWatcher {
   var plan: Plan 
}

case class Plan(address: String, following: List[Follower], messages: List[Message]) {
  def + (post: String) = {
    Plan(this.address, this.following, Message(post,new Date()) :: this.messages)
  }
}

case class Follower(address: String)

case class Message(text: String, time: Date)

// vim: set sw=2 set softtabstop=2 et:
