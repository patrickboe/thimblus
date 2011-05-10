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
  private val planTarget=writerMaker(planPath)
  private val thimblusFormats= new ThumblusFormats(TimeZone.getDefault())

  val model = new HomeModel(

    poster = (metadata,plan,post)=>{
      implicit val formats=thimblusFormats
      val newPlan=plan + post
      streamify(Planex(metadata, write(newPlan)), planTarget())
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
