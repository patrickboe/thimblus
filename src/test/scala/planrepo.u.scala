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
import org.thimblus.Util._

class PlanRepoSuite extends WordSpec with ShouldMatchers {

  var written = ""
  val testPlan = Plan("murphy.brown@aol.com",Nil,Nil)
  val testMetaData = "beard of bees"
  implicit val testFormats = new DefaultFormats{
      override def dateFormatter =
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    }
  val testPlanString = write(testPlan)
  val testPlanFileContent = "boogadaboogada"
  val testPlanex = new IPlanex{
    def apply(metadata: String, plan: String) = (metadata, plan) match {
      case (testMetaData, testPlanString) => testPlanFileContent
      case _ => null
    }
    def unapply(str: String) = {
      when (str==testPlanFileContent) { 
        (testMetaData, testPlanString) 
      }
    }
  }

  object TestEnv {
    lazy val jsonFormats = testFormats
    lazy val load = () => testPlanFileContent
    lazy val planTarget = (s: String) => written=s
    lazy val planex = testPlanex
  }

  "a posted plan" should {
    "be written to the target" in {
      val msg = (testMetaData, testPlan)
      val a = actorOf(new PlanRepo(TestEnv)).start()
      a !!! msg
      Thread.sleep(1)
      a.stop()
      written should equal (testPlanFileContent)
    }
  }

  "a load request" should {
    "return the load results" in {
      val a = actorOf(new PlanRepo(TestEnv)).start()
      (a !! PlanRequest()).get should equal (testMetaData, testPlan)
    }
  }
}

// vim: sw=2:softtabstop=2:et:
