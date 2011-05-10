package org.thimblus
import org.thimblus.plan._

object Aggregation {

  implicit val messageOrder = new Ordering[Message] { 
    def compare(x: Message,y: Message) = { y.time compareTo x.time }
  }

  def sort(unsorted: Plan) = {
    Plan(unsorted.address, unsorted.following, unsorted.messages.sorted)
  }

}

// vim: set sw=2 set softtabstop=2 et:
