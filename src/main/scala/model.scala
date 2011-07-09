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

import scala.swing._
import scala.swing.event._
import org.thimblus.plan._

class HomeModel(poster: (String,Plan,String)=>Unit, planLoader: ()=>(String,Plan)) extends HomeSource {
  var metadata: String=null
  planLoader() match { case (x,y) => { metadata=x; plan=y; } }

  def post(s: String) = {
    poster(metadata,plan,s)
    planLoader() match { case (x,y) => { plan=y } }
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

// vim: sw=2:softtabstop=2:et:
