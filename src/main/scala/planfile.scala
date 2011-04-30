package org.thimblr.io

import java.io._

object Local {
  def streamer(path: String) = () => new FileReader(path)
}

// vim: set sw=2 set softtabstop=2 et:
