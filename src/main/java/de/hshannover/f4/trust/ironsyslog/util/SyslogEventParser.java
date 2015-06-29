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
 * This file is part of ironsyslog, version 0.0.4,
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

package de.hshannover.f4.trust.ironsyslog.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.ironsyslog.ep.events.Event;
import de.hshannover.f4.trust.ironsyslog.ep.events.IpMacEvent;
import de.hshannover.f4.trust.ironsyslog.ep.events.IronSyslogServerEvent;

/**
 * Utility Class parsing the initial IronSyslogEvent depending on its purpose
 * (e.g. LoginFailed or Database Event).
 * 
 * @author Leonard Renners
 * 
 */
public final class SyslogEventParser {

    private static final Logger LOGGER = Logger
            .getLogger(SyslogEventParser.class);

    /**
     * Death constructor for code convention -> final class because utility
     * class
     */
    private SyslogEventParser() {

    }

    /**
     * 
     * @param event
     * @return The parsed event
     */
    public static Event parseEvent(IronSyslogServerEvent event) {
        String message = event.getMessage();
        if (message.startsWith("dnsmasq-dhcp")) {
            Pattern p = Pattern
                    .compile("dnsmasq-dhcp\\[\\d+\\]: DHCPACK\\((.*)\\) (\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}) (..:..:..:..:..:..) (.*)");
            Matcher m = p.matcher(message);
            if (m.find()) {
                String dhcpInterface = m.group(1);
                String ipAddress = m.group(2);
                String macAddress = m.group(3);
                String deviceName = m.group(4);
                IpMacEvent result = new IpMacEvent(ipAddress, macAddress,
                        event.getHost());
                return result;
            }
        }
        return null;
    }
}
