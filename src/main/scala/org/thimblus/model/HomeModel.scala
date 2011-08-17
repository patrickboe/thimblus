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

import org.thimblus.data._
import scala.swing._
import scala.swing.event._
import akka.actor._
import akka.dispatch._
import akka.event.EventHandler
import java.util.Date
import org.thimblus.repo._
import java.io.Closeable

class HomeModelA(
  service: IPlanDispatch, 
  time: ()=>Date
) extends Publisher with Closeable {
  private var _plan: Plan = null
  private var _metadata: String = null
  def plan: Plan = _plan
  def plan_=(p: Plan) { 
    _plan = p 
    publish(PlanUpdate(p))
  }
  def post(msg: String) {
    plan += Message(msg, time())
    service.getRepo() ! (_metadata, plan)
  }
  def close() = service.close()
  
  (service.getRepo() !!! PlanRequest) onComplete { f =>
    f.result.get.asInstanceOf[Tuple2[String,Plan]] match {
      case (m,p) => { 
        plan=p 
        _metadata=m
      }
    }
  }
}

class HomeModel(
    poster: (String,Plan,String)=>Unit,
    loadPlan: ()=>(String,Plan)
  ) extends HomeSource {
  loadPlan() match { case (x,y) => { metadata=x; plan=y; } }

  def post(s: String) = {
    poster(metadata,plan,s)
    loadPlan() match { case (x,y) => { plan=y } }
  }
}
// vim: sw=2:softtabstop=2:et:
