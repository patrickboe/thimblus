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
package org.thimblus.io

import java.io._
import java.net._
import org.thimblus.io._
import IO._

object Finger {
  def makeFingerer(connector: FingerConnector)(implicit cs: ThimblCharset) = () => {
    val addr = InetAddress.getByName(connector.host)
    val sock = new Socket(addr, 79)
    try{
      using(new InputStreamReader(sock.getInputStream(),cs.charset)) { reader=>
        using(new OutputStreamWriter(sock.getOutputStream(),cs.charset)) { writer =>
          streamify(connector.user+"\r\n",writer)
          stringify(reader)
        }
      }
    } finally { sock.close() }
  }
}

case class FingerConnector(user: String, host: String)

// vim: sw=2:softtabstop=2:et
