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
package org.thimblus.test.repo

import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.TestFailedException
import net.liftweb.json._
import akka.actor._
import Actor._
import org.thimblus.repo._
import org.thimblus.data._
import java.text.SimpleDateFormat
import net.liftweb.json.Serialization.{read,write}


class PlanRepoSuite extends WordSpec with ShouldMatchers {
  var written = ""
  val testPlan = Plan("murphy.brown@aol.com",Nil,Nil)
  val metaData = "beard of bees"
  implicit val testFormats = new DefaultFormats{
      override def dateFormatter =
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    }
  val stringifiedPlan=Planex(metaData,write(testPlan))
  object TestEnv {
    lazy val jsonFormats = testFormats
    lazy val load = () => "beard of bees"
    lazy val planTarget = (s: String) => written=s
    /*
    : (String=>Unit) = _ match {
      case _ =>"bees are on beard"
    }
    */
  }
  "a posted plan" should {
    "be written to the target" in {
      val a = actorOf(new PlanRepo(TestEnv)).start()
      val msg = (metaData, testPlan)
      (a !! msg).get should be (stringifiedPlan)
      a.stop()
      written should equal (stringifiedPlan)
    }
  }

  "a load request" should {
    "return the load results" in {
    }
  }
}
/*
import net.liftweb.json.Serialization.{read,write}
import net.liftweb.json._
import org.thimblus.io.IO._
import org.thimblus.config.Live._
    poster = (metadata,plan,post)=>{
      val newPlan=plan + post
      planTarget(Planex(metadata, write(newPlan)))
    },

    loadPlan = () => {
      val Planex(metadata, planStr) = load()
      (metadata, read[Plan](planStr))
    }
*/
