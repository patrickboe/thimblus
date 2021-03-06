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
package org.thimblus.local

import org.thimblus.model._
import net.liftweb.json.Serialization.{read,write}
import net.liftweb.json._
import org.thimblus.io.IO._
import org.thimblus.data._

class LocalModel(
  implicit val thimblusFormats: Formats,
  implicit val load: () => String,
  implicit val planTarget: String => Unit
)  extends HomeModel (

    poster = (metadata,plan,post)=>{
      val newPlan=plan + post
      planTarget(Planex(metadata, write(newPlan)))
    },

    loadPlan = () => {
      val Planex(metadata, planStr) = load()
      (metadata, read[Plan](planStr))
    }

)

// vim: sw=2:softtabstop=2:et:
