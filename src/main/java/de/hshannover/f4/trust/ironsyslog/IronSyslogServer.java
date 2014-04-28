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

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.nesscomputing.syslog4j.Syslog;
import com.nesscomputing.syslog4j.SyslogLevel;
import com.nesscomputing.syslog4j.server.SyslogServer;
import com.nesscomputing.syslog4j.server.SyslogServerConfigIF;
import com.nesscomputing.syslog4j.server.SyslogServerIF;
import com.nesscomputing.syslog4j.server.SyslogServerSessionEventHandlerIF;
import com.nesscomputing.syslog4j.server.impl.net.tcp.ssl.SSLTCPNetSyslogServerConfig;
import com.nesscomputing.syslog4j.server.impl.net.tcp.ssl.SSLTCPNetSyslogServerConfigIF;

/**
 * Main Class of the ironsyslog collector.
 */
public class IronSyslogServer {
    public static final String KEYSTORE_PATH = "/ironsyslog.jks";
    public static final String KEYSTORE_PASS = "ironsyslog";

    private static final Logger LOGGER = Logger
            .getLogger(IronSyslogServer.class);

    private final ArrayList<SyslogServerIF> mRunningServers;
    private final ArrayList<SyslogServerSessionEventHandlerIF> mHandlers;

    /**
     * Default constructor. Initially, no server is listening.
     * 
     * Use {@link #setupServer} or {@link #setupProxyServer} to start an actual
     * server.
     */
    public IronSyslogServer() {
        mRunningServers = new ArrayList<>();
        mHandlers = new ArrayList<>();
    }

    /**
     * Only for testing purposes! Sends a simple syslog message to the given
     * host
     * 
     * @param host
     *            The ip of the syslog server
     * @param port
     *            The port for the syslog server
     * @param protocol
     *            The transport protocol for syslog
     * @param level
     *            The log message severity
     * @param message
     *            The actual message
     */
    public void send(String host, int port, String protocol, SyslogLevel level,
            String message) {
        Syslog.getInstance(protocol).getConfig().setHost(host);
        Syslog.getInstance(protocol).getConfig().setPort(port);
        Syslog.getInstance(protocol).log(level, message);

    }

    /**
     * Sets up an syslog server. Note: There can only be one server per
     * protocol!
     * 
     * @param host
     *            The host to listen on (usually the ip of the computer that the
     *            program is being executed on)
     * @param port
     *            The port to listen on
     * @param protocol
     *            The transport protocol being used
     */
    public void setupServer(String host, int port, String protocol) {
        SyslogServerIF server = SyslogServer.getThreadedInstance(protocol);
        SyslogServerConfigIF config = server.getConfig();
        config.setHost(host);
        config.setPort(port);
        for (SyslogServerSessionEventHandlerIF handler : mHandlers) {
            config.addEventHandler(handler);
        }
        mRunningServers.add(server);
    }

    /**
     * Sets up an syslog server as a proxy with an existing syslog server to
     * forward the messages to. Note: There can only be one server per protocol!
     * 
     * @param host
     *            The host to listen on (usually the ip of the computer that the
     *            program is being executed on)
     * @param port
     *            The port to listen on
     * @param protocol
     *            The transport protocol being used
     * @param forwardHost
     *            The original syslog server to forward the log messages
     * @param forwardPort
     *            The port of the original syslog server
     * @param forwardProtocol
     *            The transport protocol for the original syslog server
     */
    public void setupProxyServer(String host, int port, String protocol,
            String forwardHost, int forwardPort, String forwardProtocol) {
        SyslogServerIF server = SyslogServer.getThreadedInstance(protocol);
        SyslogServerConfigIF config = server.getConfig();
        config.setHost(host);
        config.setPort(port);
        for (SyslogServerSessionEventHandlerIF handler : mHandlers) {
            config.addEventHandler(handler);
        }
        mRunningServers.add(server);
    }

    /**
     * Sets up an syslog server using ssl over tcp. The protocol name is
     * "ssltcp" by default
     * 
     * @param host
     *            The host to listen on (usually the ip of the computer that the
     *            program is being executed on)
     * @param port
     *            The port to listen on
     */
    public void setupSslTcpServer(String host, int port) {
        SSLTCPNetSyslogServerConfigIF config = new SSLTCPNetSyslogServerConfig();
        String path = IronSyslogServer.class.getResource(KEYSTORE_PATH)
                .getPath();
        config.setKeyStore(path);
        config.setKeyStorePassword(KEYSTORE_PASS);

        config.setTrustStore(path);
        config.setTrustStorePassword(KEYSTORE_PASS);

        config.setHost(host);
        config.setPort(port);

        SyslogServerIF server = SyslogServer.createThreadedInstance("ssltcp",
                config);
        for (SyslogServerSessionEventHandlerIF handler : mHandlers) {
            config.addEventHandler(handler);
        }
        mRunningServers.add(server);
    }

    /**
     * Sets up an syslog server listening for ssl over tcp as a proxy with an
     * existing syslog server to forward the messages to. The protocol name is
     * "ssltcp" by default
     * 
     * @param host
     *            The host to listen on (usually the ip of the computer that the
     *            program is being executed on)
     * @param port
     *            The port to listen on
     * @param forwardHost
     *            The original syslog server to forward the log messages
     * @param forwardPort
     *            The port of the original syslog server
     * @param forwardProtocol
     *            The transport protocol for the original syslog server
     */
    public void setupProxySslTcpServer(String host, int port,
            String forwardHost, int forwardPort, String forwardProtocol) {
        SSLTCPNetSyslogServerConfigIF config = new SSLTCPNetSyslogServerConfig();
        String path = IronSyslogServer.class.getResource(KEYSTORE_PATH)
                .getPath();
        config.setKeyStore(path);
        config.setKeyStorePassword(KEYSTORE_PASS);

        config.setTrustStore(path);
        config.setTrustStorePassword(KEYSTORE_PASS);

        config.setHost(host);
        config.setPort(port);

        for (SyslogServerSessionEventHandlerIF handler : mHandlers) {
            config.addEventHandler(handler);
        }

        SyslogServerIF server = SyslogServer.createThreadedInstance("sslTcp",
                config);
        mRunningServers.add(server);
    }

    // private void setupSSLClient() {
    // SSLTCPNetSyslogConfig config = new
    // SSLTCPNetSyslogConfig("192.168.1.53",10514);
    // Syslog.createInstance("sslTcp",config);
    // }

    /**
     * Shuts down all servers of the given protocols. Note: There can only be
     * one server per protocol!
     * 
     * @param protocol
     *            Transport protocol of the syslog server (e.g. UDP or TCP)
     */
    public void shutdownServer(String protocol) {
        ArrayList<SyslogServerIF> remove = new ArrayList<>();
        for (SyslogServerIF server : mRunningServers) {
            if (server.getProtocol().equals(protocol)) {
                server.shutdown();
                remove.add(server);
            }
        }
        mRunningServers.removeAll(remove);
    }

    /**
     * Shuts down all running syslog servers and removes all handlers
     */
    public void shutdown() {
        for (SyslogServerIF server : mRunningServers) {
            server.shutdown();
        }
        mRunningServers.clear();
        mHandlers.clear();
    }

    /**
     * Removes all handlers
     */
    public void removeAllHandlers() {
        for (SyslogServerIF server : mRunningServers) {
            server.getConfig().removeAllEventHandlers();
        }
        mHandlers.clear();
    }

    /**
     * Adds a handler to all existing and future servers
     * 
     * @param handler
     *            the handler to add
     */
    public void addHandler(SyslogServerSessionEventHandlerIF handler) {
        mHandlers.add(handler);
        for (SyslogServerIF server : mRunningServers) {
            server.getConfig().addEventHandler(handler);
        }
    }

    /**
     * Returns if any server is running (independent of the used protocol)
     * 
     * @return wether a server is running
     */
    public boolean isServerRunning() {
        return !mRunningServers.isEmpty();
    }

    /**
     * Returns if a server is running with the given procotol
     * 
     * @param protocol
     *            the protocol which is asked for
     * @return wether a server is running with that protocol
     */
    public boolean isServerRunning(String protocol) {
        for (SyslogServerIF server : mRunningServers) {
            if (server.getProtocol().equals(protocol)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the port if the server is listening on that protocol, otherwise
     * -1
     * 
     * @param protocol
     *            The transport protocol listening on
     * @return the port the server listens on the given protocol
     */
    public int getPortForProtocol(String protocol) {
        if (isServerRunning(protocol)) {
            return SyslogServer.getInstance(protocol).getConfig().getPort();
        } else {
            return -1;
        }
    }

}
