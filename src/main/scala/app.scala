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
package org.thimblus.swing

import java.util.TimeZone
import net.liftweb.json.Serialization.{read,write}
import net.liftweb.json._
import org.thimblus.model.HomeModel
import org.thimblus.ui.Dispatch
import org.thimblus.io.Local._
import org.thimblus.io.IO._
import org.thimblus.plan._

object App extends {
  private val planPath="src/test/resources/testplans/.plan"
  private val planSource=readerMaker(planPath)
  private val planTarget=recorderMaker(planPath)
  private val thimblusFormats= new ThumblusFormats(TimeZone.getDefault())

  val model = new HomeModel(

    poster = (metadata,plan,post)=>{
      implicit val formats=thimblusFormats
      val newPlan=plan + post
      planTarget().record(Planex(metadata, write(newPlan)))
    },

    planLoader = () => {
      implicit val formats=thimblusFormats
      val Planex(metadata, planStr) = stringify(planSource())
      (metadata, read[Plan](planStr))
    }

  )

} with SwingView {
  Dispatch(this,model)
}

// vim: set ts=2 sw=2 set softtabstop=2 et:
