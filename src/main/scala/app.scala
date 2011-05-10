package org.thimblr.swing

import org.thimblr.model.HomeModel
import org.thimblr.ui.Dispatch
import org.thimblr.io.Local._

object App extends {
  private val planPath=".plan"
  private val planSource=readerMaker(planPath)
  private val planTarget=writerMaker(planPath)

  val model = new HomeModel(poster=(a,b,c)=>(),()=>(null,null))

    /*
    poster = (metadata,plan,post)=>{
      val newPlan=postTo(plan,post)
      streamify(Planex(metadata,newPlan),planTarget)
    },

    loadPlan = {
      val Planex(metadata, plan) = stringify(planSource))
      (metadata,plan)
    }
    */


} with SwingView {
  Dispatch(this,model)
}

// vim: set ts=2 sw=2 set softtabstop=2 et:
