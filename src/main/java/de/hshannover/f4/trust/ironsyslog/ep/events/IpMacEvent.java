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

package de.hshannover.f4.trust.ironsyslog.ep.events;

import java.util.Date;

/**
 * Event class corresponding to the IFMAP meta-ip-mac Metadata.
 * 
 * @author Leonard Renners
 * 
 */
public class IpMacEvent extends Event {

    private String mIpAddress;
    private String mMacAddress;
    private String mDhcpServer;
    private Date mStartTime;
    private Date mEndTime;

    /**
     * Constructor.
     * 
     * @param ipAddress
     *            The IP-Address which has been assigned to the MAC-Address
     * @param macAddress
     *            The MAC-Address which the IP has been assigned to
     */
    public IpMacEvent(String ipAddress, String macAddress) {
        super();
        setIpAddress(ipAddress);
        setMacAddress(macAddress);
    }

    /**
     * Constructor.
     * 
     * @param ipAddress
     *            The IP-Address which has been assigned to the MAC-Address
     * @param macAddress
     *            The MAC-Address which the IP has been assigned to
     * @param dhcpServer
     *            The DHCP-Server which has created the IP lease
     */
    public IpMacEvent(String ipAddress, String macAddress, String dhcpServer) {
        super();
        setIpAddress(ipAddress);
        setMacAddress(macAddress);
        setDhcpServer(dhcpServer);
    }

    public String getIpAddress() {
        return mIpAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.mIpAddress = ipAddress;
    }

    public String getMacAddress() {
        return mMacAddress;
    }

    public void setMacAddress(String macAddress) {
        this.mMacAddress = macAddress;
    }

    public String getDhcpServer() {
        return mDhcpServer;
    }

    public void setDhcpServer(String dhcpServer) {
        this.mDhcpServer = dhcpServer;
    }

    public Date getStartTime() {
        return mStartTime;
    }

    public void setStartTime(Date startTime) {
        this.mStartTime = startTime;
    }

    public Date getEndTime() {
        return mEndTime;
    }

    public void setEndTime(Date endTime) {
        this.mEndTime = endTime;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[" + this.getClass().getSimpleName() + "] ");
        buffer.append(this.getDhcpServer() + ": " + this.getIpAddress() + " "
                + this.getMacAddress());
        return buffer.toString();
    }

}
