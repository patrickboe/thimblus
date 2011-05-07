package org.thimblr {

  object Util {

    def when [T](predicate: => Boolean)(expression: => T) = {
      if(predicate) Some(expression) else None
    }

    class Slashable(s: String) {
      def / (at: Int) = (s take at, s drop (at+1))
    }

    implicit def string2Slashable(s: String) = new Slashable(s)

    class Questionable[T](thing: T) {
      def ?? (fallback: T) = {
        if(thing!=null) thing else fallback
      }
    }

    implicit def thing2Questionable[T](thing: T) = new Questionable[T](thing)

  }

}

// vim: set sw=2 set softtabstop=2 et:
