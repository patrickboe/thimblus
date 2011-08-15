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
package org.thimblus.repo

import org.thimblus.data._
import akka.actor._
import net.liftweb.json._
import net.liftweb.json.Serialization.{read,write}
import org.thimblus.io.IO._

class PlanRepo(env: {
  val jsonFormats: Formats
  val load: () => String
  val planTarget: String => Unit
  val planex: IPlanex
}) extends Actor {
  implicit val jsonFormats = env.jsonFormats
  def receive = {
    case (metaData: String, plan: Plan) => {
      val s = env.planex(metaData,write(plan))
      env.planTarget(s)
    }
    case r: PlanRequest => {
      val env.planex(metaData, planStr) = env.load()
      self.channel ! (metaData, read[Plan](planStr))
    }
  }
}

case class PlanRequest

