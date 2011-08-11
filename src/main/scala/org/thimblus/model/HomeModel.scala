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
import akka.actor._
import akka.event.EventHandler
import java.util.Date

class HomeModelA(
  service: IPlanDispatch, 
  store: HomeStore,
  time: ()=>Date
)
extends Actor {
  private val loaderRepo = service.getRepo()
  private var view: Channel[Any] = null

  override def postStop() = {
    service.close()
  }

  def receive = {
    case r: Request => {
      view=self.channel
      loaderRepo ! r
    }

    case (metadata: String, plan: Plan) => {
      store.plan=plan
      store.metadata=metadata
      view ! plan
    }

    case post: String => {
      store.plan += Message(post, time())
      loaderRepo ! (store.metadata, store.plan)
      self.reply(store.plan)
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
