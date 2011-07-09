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

import java.io.File;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import org.thimblus.ssh._;

object Ganymed {
  def readerMaker(connector: SSHConnector, path: String) = () => {
        val c=new Connection(connector.hostname)
        c.connect()
        val keyfile=new File(connector.keypath)
        if(!c.authenticateWithPublicKey(connector.username,keyfile,connector.pass)){
            //throw something    
        }
        val session = c.openSession
        session.execCommand("cat " + path)
        val out = new StreamGobbler(session.getStdout)
        val err = new StreamGobbler(session.getStderr)
        session.close()
        c.close()
    }
}
// vim: set sw=2 set softtabstop=2 et:
