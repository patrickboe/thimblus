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

import scala.swing._
import org.thimblus.model._
import org.thimblus.plan._

trait SwingView
  extends org.thimblus.ui.View {

  val myPosts = new TextArea { 
    text = list(model.plan.messages)
  }
  val post = new Button { text = "post" }
  val message = new TextField
  
  def top = new MainFrame {
    title = "Thimblus"
    contents = new BoxPanel(Orientation.Vertical) {
      contents += message
      contents += post
      contents += myPosts
    }
  }

  private def list(messages: List[Message]) = 
    messages map (_.text) mkString "\n" 

  reactions += {
    case PlanUpdate(newPlan) => 
    myPosts.text=list(newPlan.messages)
  }
}
// vim: sw=2:softtabstop=2:et:
