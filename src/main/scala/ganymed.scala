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

import java.io._;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import org.thimblus.ssh._;

object Ganymed {
  def readerMaker(connector: SSHConnector, path: String, errorStreamHandler: GanymedReader=>Unit) = 
    () => {
        val connection=new Connection(connector.hostname)
        connection.connect()
        val keyfile=new File(connector.keypath)
        if(!connection.authenticateWithPublicKey(connector.username,keyfile,connector.pass)){
            connection.close()
            throw new IOException("connected but failed to authenticate")
        }
        val session = connection.openSession
        session.execCommand("cat " + path)
        val ganyReaderFrom = (f: Session=>InputStream) => {
          new GanymedReader(new InputStreamReader(new StreamGobbler(f(session))),session,connection)
        }
        errorStreamHandler(ganyReaderFrom(s=>s.getStderr))
        ganyReaderFrom(s=>s.getStdout)
    }
}

class GanymedReader(remoteReader: Reader, session: Session, connection: Connection) 
extends Reader with Closeable {
  def read(a: Array[Char],b: Int,c: Int)=remoteReader.read(a,b,c)
  def close()={
    remoteReader.close()
    session.close()
    connection.close()
  }
}

class GanymedWriter(remoteWriter: Writer, session: Session, connection: Connection) 
extends Writer with Closeable {
  def write(a: Array[Char],b: Int,c: Int)=remoteWriter.write(a,b,c)
  def flush()=remoteWriter.flush()
  def close()={
    remoteWriter.close()
    session.close()
    connection.close()
  }
}
// vim: sw=2:softtabstop=2:et:
