package thimblr

import java.io._

object Planfile {
  def streamer(path: String)() = new FileReader(path)
}

// vim: set sw=2 set softtabstop=2 et:
