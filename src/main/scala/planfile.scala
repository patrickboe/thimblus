package thimblr

import java.io.FileReader

object Planfile {
  def streamer(path: String)() = new java.io.FileReader(path)
}

// vim: set sw=2 set softtabstop=2 et:
