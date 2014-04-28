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

import com.nesscomputing.syslog4j.server.SyslogServerEventIF;
import com.nesscomputing.syslog4j.server.SyslogServerIF;
import com.nesscomputing.syslog4j.server.SyslogServerSessionEventHandlerIF;

import de.hshannover.f4.trust.ironsyslog.ep.drools.IronSyslogDrools;

/**
 * Implements a handler for the Syslog4j server to forward the events to the
 * drools engine.
 * 
 * @author Leonard Renners
 * 
 */
public class IronSyslogDroolsHandler implements
        SyslogServerSessionEventHandlerIF {

    private final IronSyslogDrools mEngine;

    /**
     * Constructor.
     * 
     * @param engine
     *            The engine to forward te events to
     */
    public IronSyslogDroolsHandler(IronSyslogDrools engine) {
        this.mEngine = engine;
    }

    @Override
    public void initialize(SyslogServerIF syslogServer) {
    }

    @Override
    public Object sessionOpened(SyslogServerIF syslogServer,
            SocketAddress socketAddress) {
        return null;
    }

    @Override
    public void event(Object session, SyslogServerIF syslogServer,
            SocketAddress socketAddress, SyslogServerEventIF event) {
        mEngine.insert(event);
    }

    @Override
    public void exception(Object session, SyslogServerIF syslogServer,
            SocketAddress socketAddress, Exception exception) {
    }

    @Override
    public void sessionClosed(Object session, SyslogServerIF syslogServer,
            SocketAddress socketAddress, boolean timeout) {
    }

    @Override
    public void destroy(SyslogServerIF syslogServer) {
    }

}
