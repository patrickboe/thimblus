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
package org.thimblus.data

import org.thimblus.Util._
import java.util.Date
import net.liftweb.json._
import net.liftweb.json.JsonParser._

case class Plan(address: String, following: List[Follower], messages: List[Message]) {
  
  def + (post: Message) = {
    Plan(this.address, this.following, post :: this.messages)
  }

  def + (post: String) = {
    Plan(this.address, this.following, Message(post,new Date()) :: this.messages)
  }

}

case class Follower(address: String)

case class Message(text: String, time: Date)

// vim: sw=2:softtabstop=2:et:
