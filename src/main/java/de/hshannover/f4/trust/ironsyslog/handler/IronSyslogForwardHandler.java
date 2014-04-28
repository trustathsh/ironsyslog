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

import com.nesscomputing.syslog4j.Syslog;
import com.nesscomputing.syslog4j.impl.AbstractSyslog;
import com.nesscomputing.syslog4j.impl.net.tcp.TCPNetSyslog;
import com.nesscomputing.syslog4j.impl.net.tcp.ssl.SSLTCPNetSyslog;
import com.nesscomputing.syslog4j.impl.net.udp.UDPNetSyslog;
import com.nesscomputing.syslog4j.server.SyslogServerEventIF;
import com.nesscomputing.syslog4j.server.SyslogServerIF;
import com.nesscomputing.syslog4j.server.SyslogServerSessionEventHandlerIF;

/**
 * Implements a handler for the Syslog4j server to forward the events to an
 * existing syslog server.
 * 
 * @author Leonard Renners
 * 
 */
public class IronSyslogForwardHandler implements
        SyslogServerSessionEventHandlerIF {

    private final String mForwardHost;
    private final int mForwardPort;
    private final String mForwardProtocol;

    /**
     * Constructor.
     * 
     * @param host
     *            IP of the existing syslog server
     * @param port
     *            Port to forward the events to
     * @param protocol
     *            Protocol used by the existing server
     */
    public IronSyslogForwardHandler(String host, int port, String protocol) {
        this.mForwardHost = host;
        this.mForwardPort = port;
        this.mForwardProtocol = protocol;
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
        AbstractSyslog syslog = (AbstractSyslog) Syslog
                .getInstance(mForwardProtocol);
        switch (mForwardProtocol) {
        case "tcp":
            ((TCPNetSyslog) syslog).getConfig().setHost(mForwardHost);
            ((TCPNetSyslog) syslog).getConfig().setPort(mForwardPort);
            break;
        case "udp":
            ((UDPNetSyslog) syslog).getConfig().setHost(mForwardHost);
            ((UDPNetSyslog) syslog).getConfig().setPort(mForwardPort);
            break;
        case "ssltcp":
            ((SSLTCPNetSyslog) syslog).getConfig().setHost(mForwardHost);
            ((SSLTCPNetSyslog) syslog).getConfig().setPort(mForwardPort);
            break;
        default:
            break;
        }
        syslog.getWriter().write(event.getRaw());
    }

    @Override
    public void exception(Object session, SyslogServerIF syslogServer,
            SocketAddress socketAddress, Exception exception) {
        try {
            throw exception;
        } catch (Exception e) {
            System.out.println("Forward failed");
        }
    }

    @Override
    public void sessionClosed(Object session, SyslogServerIF syslogServer,
            SocketAddress socketAddress, boolean timeout) {
    }

    @Override
    public void destroy(SyslogServerIF syslogServer) {
    }

}
