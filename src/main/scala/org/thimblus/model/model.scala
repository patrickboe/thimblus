/*
 * Copyright 2011, Patrick Boe
 * =========================== 
 * This program is distributed under the terms of the GNU General Public License.
 *
 * This file is part of Thimblus.
 * 
 * Thimblus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Thimblus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Thimblus.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.thimblus.model

import java.util.Date
import scala.swing._
import scala.swing.event._
import org.thimblus.data._
import akka.actor._
import akka.event.EventHandler
import java.io.Closeable

trait HomeStore {
  var plan: Plan
}

trait HomeSource extends HomeStore with Publisher {
  private[this] var p: Plan = null
  def plan: Plan = p
  def plan_= (x: Plan) {
    p=x
    publish(PlanUpdate(x))
  }
}
case class Request(requested: String)

case class PlanUpdate(revised: Plan) extends Event

class PlanTimeoutException extends RuntimeException

class GibberishException(message: Any) extends RuntimeException(message.toString)

trait PlanService extends Closeable {
  def getRepo(): ActorRef
}

// vim: sw=2:softtabstop=2:et:
