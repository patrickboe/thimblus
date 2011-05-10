package org.thimblr.swing

import java.util.TimeZone
import net.liftweb.json.Serialization.{read,write}
import net.liftweb.json._
import org.thimblr.model.HomeModel
import org.thimblr.ui.Dispatch
import org.thimblr.io.Local._
import org.thimblr.io.IO._
import org.thimblr.plan._

object App extends {
  private val planPath="src/test/resources/testplans/.plan"
  private val planSource=readerMaker(planPath)
  private val planTarget=writerMaker(planPath)
  private val thimblrFormats= new ThimblrFormats(TimeZone.getDefault())

  val model = new HomeModel(

    poster = (metadata,plan,post)=>{
      implicit val formats=thimblrFormats
      val newPlan=plan + post
      streamify(Planex(metadata, write(newPlan)), planTarget())
    },

    planLoader = () => {
      implicit val formats=thimblrFormats
      val Planex(metadata, planStr) = stringify(planSource())
      (metadata, read[Plan](planStr))
    }

  )

} with SwingView {
  Dispatch(this,model)
}

// vim: set ts=2 sw=2 set softtabstop=2 et:
