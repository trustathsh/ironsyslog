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

package de.hshannover.f4.trust.ironsyslog.ifmap;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.ifmapj.IfmapJ;
import de.hshannover.f4.trust.ifmapj.IfmapJHelper;
import de.hshannover.f4.trust.ifmapj.channel.SSRC;
import de.hshannover.f4.trust.ifmapj.exception.IfmapErrorResult;
import de.hshannover.f4.trust.ifmapj.exception.IfmapException;
import de.hshannover.f4.trust.ifmapj.exception.InitializationException;
import de.hshannover.f4.trust.ifmapj.identifier.Identifiers;
import de.hshannover.f4.trust.ifmapj.identifier.IpAddress;
import de.hshannover.f4.trust.ifmapj.identifier.MacAddress;
import de.hshannover.f4.trust.ifmapj.messages.Requests;
import de.hshannover.f4.trust.ifmapj.metadata.StandardIfmapMetadataFactory;
import de.hshannover.f4.trust.ironsyslog.ep.events.IpMacEvent;
import de.hshannover.f4.trust.ironsyslog.ep.events.LoginFailedEvent;

/**
 * 
 * @author Leonard Renners
 * 
 */
public final class IronSyslogPublisher {

    private static StandardIfmapMetadataFactory mf = IfmapJ
            .createStandardMetadataFactory();

    private static final Logger LOGGER = Logger
            .getLogger(IronSyslogPublisher.class);

    private static SSRC mSsrc;

    // public static void main(String[] args) {
    // initialise();
    // publishBinky(null);
    // }

    /**
     * Death constructor for code convention -> final class because utility
     * class
     */
    private IronSyslogPublisher() {

    }

    /**
     * The init methode initiates the Ifmap Session
     */
    public static void init() {
        LOGGER.info("Initialisiing Session using basic authentication");
        try {
            mSsrc = IfmapJ.createSSRC(Configuration.ifmapUrlBasic(),
                    Configuration.ifmapBasicUser(), Configuration
                            .ifmapBasicPassword(), IfmapJHelper
                            .getTrustManagers(IronSyslogPublisher.class
                                    .getResourceAsStream(Configuration
                                            .keyStorePath()), Configuration
                                    .keyStorePassword()));
            mSsrc.newSession();
        } catch (InitializationException e) {
            LOGGER.error(e);
        } catch (IfmapErrorResult e) {
            LOGGER.error(e);
        } catch (IfmapException e) {
            LOGGER.error(e);
        }
        LOGGER.info("Session successfully created");
    }

    /**
     * Publishes an IpMacEvent as an meta-ip-mac metadata in the ifmap server.
     * 
     * @param event
     *            the event to publish
     */
    public static void publishIpMac(IpMacEvent event) {
        try {
            LOGGER.debug("Publishing meta-ip-mac for: " + event);

            StandardIfmapMetadataFactory mf = IfmapJ
                    .createStandardMetadataFactory();
            IpAddress ip = Identifiers.createIp4(event.getIpAddress());
            MacAddress mac = Identifiers.createMac(event.getMacAddress());

            mSsrc.publish(Requests.createPublishReq(Requests
                    .createPublishUpdate(ip, mac, mf.createIpMac())));

            LOGGER.debug("Publish successful");
        } catch (IfmapException e) {
            LOGGER.error(e);
        } catch (IfmapErrorResult e) {
            LOGGER.error(e);
        }
    }

    /**
     * Publishes an LoginFailed as an login-failed metadata in the ifmap server.
     * 
     * @param event
     *            the event to publish
     */
    public static void publishLoginFailed(LoginFailedEvent event) {
        // try {
        LOGGER.debug("Publishing login-failed for: " + event);
        // TODO: Implement
        LOGGER.debug("Publish successful");
        // } catch (IfmapException e) {
        // LOGGER.error(e);
        // } catch (IfmapErrorResult e) {
        // LOGGER.error(e);
        // }
    }
}
