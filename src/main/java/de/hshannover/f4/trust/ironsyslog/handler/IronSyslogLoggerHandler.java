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

package de.hshannover.f4.trust.ironsyslog.handler;

import java.net.SocketAddress;
import java.util.Date;

import org.apache.log4j.Logger;

import com.nesscomputing.syslog4j.server.SyslogServerEventIF;
import com.nesscomputing.syslog4j.server.SyslogServerIF;
import com.nesscomputing.syslog4j.server.SyslogServerSessionEventHandlerIF;

/**
 * Implements a handler for the Syslog4j server to log the events using log4j.
 * 
 * @author Leonard Renners
 * 
 */
public class IronSyslogLoggerHandler implements
        SyslogServerSessionEventHandlerIF {

    private static Logger LOGGER = Logger
            .getLogger(IronSyslogLoggerHandler.class);

    /**
     * Constructor.
     */
    public IronSyslogLoggerHandler() {
    }

    @Override
    public void initialize(SyslogServerIF syslogServer) {
        LOGGER.info(syslogServer.getProtocol() + " server on port "
                + syslogServer.getActualPort() + " initialized");
    }

    @Override
    public Object sessionOpened(SyslogServerIF syslogServer,
            SocketAddress socketAddress) {
        LOGGER.info(syslogServer.getProtocol() + " server on port "
                + syslogServer.getActualPort() + " opened");
        return null;
    }

    @Override
    public void event(Object session, SyslogServerIF syslogServer,
            SocketAddress socketAddress, SyslogServerEventIF event) {
        String date = (event.getDate() == null ? new Date() : event.getDate())
                .toString();
        String facility = event.getFacility().name();
        String level = event.getLevel().name();
        LOGGER.info(syslogServer.getProtocol() + " server on port "
                + syslogServer.getActualPort() + " recieved an Event: " + "{"
                + facility + "} " + date + " " + level + " "
                + event.getMessage());
    }

    @Override
    public void exception(Object session, SyslogServerIF syslogServer,
            SocketAddress socketAddress, Exception exception) {
        LOGGER.error("An Exception on the " + syslogServer.getProtocol()
                + " server on port " + syslogServer.getActualPort()
                + " occured!", exception);
    }

    @Override
    public void sessionClosed(Object session, SyslogServerIF syslogServer,
            SocketAddress socketAddress, boolean timeout) {
        LOGGER.info(syslogServer.getProtocol() + " server on port "
                + syslogServer.getActualPort() + " closed");
    }

    @Override
    public void destroy(SyslogServerIF syslogServer) {
        LOGGER.info(syslogServer.getProtocol() + " server on port "
                + syslogServer.getActualPort() + " destroyed");
    }
}
