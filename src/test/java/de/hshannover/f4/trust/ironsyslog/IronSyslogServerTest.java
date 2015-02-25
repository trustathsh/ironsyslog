/*
 * #%L
 * =====================================================
 *   _____                _     ____  _   _       _   _
 *  |_   _|_ __ _   _ ___| |_  / __ \| | | | ___ | | | |
 *    | | | '__| | | / __| __|/ / _` | |_| |/ __|| |_| |
 *    | | | |  | |_| \__ \ |_| | (_| |  _  |\__ \|  _  |
 *    |_| |_|   \__,_|___/\__|\ \__,_|_| |_||___/|_| |_|
 *                             \____/
 * 
 * =====================================================
 * 
 * Hochschule Hannover
 * (University of Applied Sciences and Arts, Hannover)
 * Faculty IV, Dept. of Computer Science
 * Ricklinger Stadtweg 118, 30459 Hannover, Germany
 * 
 * Email: trust@f4-i.fh-hannover.de
 * Website: http://trust.f4.hs-hannover.de/
 * 
 * This file is part of ironsyslog, version 0.0.3,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2014 - 2015 Trust@HsH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package de.hshannover.f4.trust.ironsyslog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.nesscomputing.syslog4j.Syslog;
import com.nesscomputing.syslog4j.SyslogLevel;

import de.hshannover.f4.trust.ironsyslog.syslog.IronSyslogServer;

/**
 * Test class for the setup of the syslog server.
 * 
 * @author Leonard Renners
 * 
 */
public class IronSyslogServerTest {

    private IronSyslogServer ironsyslog;
    private static final String HOST_IP = "192.168.1.53";

    /**
     * Sets up the test environment.
     */
    @Before
    public void setUp() {
        System.setProperty("log4j.defaultInitOverride", "false");
        System.setProperty("log4j.configuration", "log4j.properties");
        ironsyslog = new IronSyslogServer();
    }

    /**
     * Tests the server setup.
     */
    @Test
    public void testServerSetup() {
        assertFalse(ironsyslog.isServerRunning());
        assertFalse(ironsyslog.isServerRunning("udp"));

        ironsyslog.setupServer(HOST_IP, 9999, "udp");
        assertTrue(ironsyslog.isServerRunning());
        assertTrue(ironsyslog.isServerRunning("udp"));
        assertFalse(ironsyslog.isServerRunning("tcp"));

        ironsyslog.setupServer(HOST_IP, 63999, "tcp");
        assertEquals(63999, ironsyslog.getPortForProtocol("tcp"));
        assertTrue(ironsyslog.isServerRunning());
        assertTrue(ironsyslog.isServerRunning("tcp"));

        ironsyslog.shutdownServer("udp");
        assertFalse(ironsyslog.isServerRunning("udp"));
        assertTrue(ironsyslog.isServerRunning());

        ironsyslog.shutdownServer("tcp");
        assertFalse(ironsyslog.isServerRunning("tcp"));
        assertFalse(ironsyslog.isServerRunning());

        ironsyslog.setupSslTcpServer(HOST_IP, 63998);
        assertEquals(63998, ironsyslog.getPortForProtocol("ssltcp"));
        assertTrue(ironsyslog.isServerRunning());
        assertTrue(ironsyslog.isServerRunning("ssltcp"));

        ironsyslog.shutdownServer("ssltcp");
        assertFalse(ironsyslog.isServerRunning("ssltcp"));
        assertFalse(ironsyslog.isServerRunning());

        Syslog.getInstance("udp").getConfig().setHost(HOST_IP);
        Syslog.getInstance("udp").getConfig().setPort(514);
        Syslog.getInstance("udp").log(SyslogLevel.INFO, "test");
    }

    /**
     * Tears down the test environment.
     */
    @After
    public void tearDown() {
        ironsyslog.shutdown();
    }

}
