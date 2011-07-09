Thimblus
========

What is it?
-----------

Thimblus will be a JVM desktop client for the open web microblogging platform, [Thimbl](http://www.thimbl.net). What it is right now is a few tests and their implementation. Integration tests end in .i.scala and unit tests end in .u.scala, but we haven't separated them into different builds yet.

Building
--------

Thimblus uses an sbt build.  Install sbt if you don't have it. Run sbt from the command line, then from the sbt prompt run `update` to get managed dependencies.  Then you're ready to develop. Type `actions` to see the available build actions.  Try using `~ test-quick` from the command prompt for online testing.
