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
/*
 * workflow:
 * check to see if ssh keys exist
 * if not, generate them
 * ssh to server, trying key exchange
 * if that fails,
 * prompt for password
 * handle password failure
 * now authenticated, issue the command to trust key
 * 
 * now should be able to write to plan
*/
package org.thimblus.test.ssh

import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.TestFailedException
import org.thimblus.ssh.SSH._

class SSHSpec extends WordSpec with ShouldMatchers {
  "keycheck" when {
    "there are valid keys" should {
      "return true" in {
        val filecheck = (name: String) => { 
          name=="~/.ssh/id_rsa" || 
          name=="~/.ssh/id_rsa.pub" ||
          name=="~/.ssh/known_hosts"
        }
        assert(keycheck(filecheck))
      }
    }
    "missing private keys" should {
      "return false" in {
        val missingPriv = (s: String) => {
          s == "~/.ssh/id_rsa.pub" || s == "~/.ssh/known_hosts"
        }
        keycheck(missingPriv) should be (false)
      }
    }
    "missing public keys" should {
      "return false" in {
        val missingPub = (s: String) => {
          s == "~/.ssh/id_rsa" || s == "~/.ssh/known_hosts"
        }
        keycheck(missingPub) should be (false)
      }
    }
    "missing hosts" should {
      "return false" in {
        val missingHosts = (s: String) => {
          s == "~/.ssh/id_rsa.pub" || s == "~/.ssh/id_rsa"
        }
        keycheck(missingHosts) should be (false)
      }
    }
  }
}

// vim: set sw=2 set softtabstop=2 et:
