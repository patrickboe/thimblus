package org.thimblr.plan

import org.thimblr.Util._
import java.util.Date
import net.liftweb.json._
import net.liftweb.json.JsonParser._

case class Plan(address: String, following: List[Follower], messages: List[Message])

case class Follower(address: String)

case class Message(text: String, time: Date)

// vim: set sw=2 set softtabstop=2 et:
