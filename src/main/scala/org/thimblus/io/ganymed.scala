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
import ch.ethz.ssh2.{Connection,ServerHostKeyVerifier,SCPClient}
import org.thimblus.ssh._
import org.thimblus.io._
import IO._

object Ganymed {
  def makeRecorder(connector: SSHConnector, destination: Destination, verifier: ServerHostKeyVerifier)(content: String) = {
    val connection=new Connection(connector.hostname)
    try{
      connection.connect(verifier)
      val keyfile=new File(connector.keypath)
      if(!connection.authenticateWithPublicKey(connector.username,keyfile,connector.pass)){
        throw new IOException("connected but failed to authenticate")
      }
      val client=new SCPClient(connection)
      client.put(
        destination.pickle(content),
        destination.file,
        destination.directory,
        destination.mode
      )
    } finally {
      connection.close() 
    }
  }
}
// vim: sw=2:softtabstop=2:et:
