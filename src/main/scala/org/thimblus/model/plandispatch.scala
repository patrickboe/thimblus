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

import akka.actor._
import java.io.Closeable

trait IPlanDispatch extends Closeable {
  def getRepo(): ActorRef
}

class PlanDispatch(generator: ()=>ActorRef) extends IPlanDispatch {
  private var actors: List[ActorRef] = Nil
  def getRepo() = {
    actors ::= generator().start()
    actors.head
  }
  def close() = {
    actors.map(_.stop())
  }
}
