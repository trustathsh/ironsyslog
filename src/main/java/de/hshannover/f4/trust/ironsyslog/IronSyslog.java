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
 * This file is part of ironsyslog, version 0.0.1,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2014 Trust@HsH
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

import de.hshannover.f4.trust.ironsyslog.ep.drools.IronSyslogDrools;
import de.hshannover.f4.trust.ironsyslog.handler.IronSyslogDroolsHandler;
import de.hshannover.f4.trust.ironsyslog.ifmap.IronSyslogPublisher;

/**
 * This class starts the application. It creates the threads for the syslog
 * server, registers a handler for drools, the event processing engine and a
 * publisher for ifmap.
 * 
 * @author Leonard renners
 * 
 */
public final class IronSyslog {

    /**
     * Death constructor for code convention -> final class because utility
     * class
     */
    private IronSyslog() {
    }

    /**
     * The Main method initialize the Configuration and the all components.
     * 
     */
    public static void main(String[] args) {
        System.setProperty("log4j.configuration", "log4j.properties");
        // Logger.getRootLogger().setLevel(Level.INFO);

        IronSyslogPublisher.init();

        IronSyslogServer sys = new IronSyslogServer();
        sys.setupServer("192.168.1.53", 9999, "udp");

        IronSyslogDrools droolsEngine = new IronSyslogDrools(
                "/rules/drools/eventrule.drl");
        sys.addHandler(new IronSyslogDroolsHandler(droolsEngine));
        // sys.addHandler(new IronSyslogLoggerHandler());

        // setupSSLServer();
        // setupSSLClient();
        // while (true) {
        // SyslogUtility.sleep(1000L);
        // sys.send("192.168.1.53", 514, "udp", SyslogLevel.INFO, "Testerin0");
        // }
    }
}
