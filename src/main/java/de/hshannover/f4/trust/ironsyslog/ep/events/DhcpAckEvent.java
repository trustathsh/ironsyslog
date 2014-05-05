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

package de.hshannover.f4.trust.ironsyslog.ep.events;

/**
 * Event class representing a DHCP ACK message, meaning the result of the
 * completed IP configuration for a client.
 * 
 * @author Leonard Renners
 * 
 */
public class DhcpAckEvent extends Event {

    private String mIpAddress;
    private String mMacAddress;
    private String mDhcpServer;
    private String mNetworkInterface;
    private String mDnsName;

    /**
     * Constructor.
     * 
     * @param ipAddress
     *            The IP-Address which has been assigned to the MAC-Address
     * @param macAddress
     *            The MAC-Address which the IP has been assigned to
     * @param dhcpServer
     *            The DHCP Server which has done the assignment
     * @param networkInterface
     *            The network interface on which the DHCP Server has processed
     *            the request
     */
    public DhcpAckEvent(String ipAddress, String macAddress, String dhcpServer,
            String networkInterface) {
        setIpAddress(ipAddress);
        setMacAddress(macAddress);
        setDhcpServer(dhcpServer);
        setNetworkInterface(networkInterface);

    }

    // /**
    // * Constructor.
    // *
    // * @param ipAddress
    // * The IP-Address which has been assigned to the MAC-Address
    // * @param macAddress
    // * The MAC-Address which the IP has been assigned to
    // * @param dhcpServer
    // * The DHCP Server which has done the assignment
    // * @param networkInterface
    // * The network interface on which the DHCP Server has processed
    // * the request
    // */
    // public DhcpAckEvent(String ipAddress, String macAddress, String
    // dhcpServer,
    // String networkInterface, String dnsName) {
    // setIpAddress(ipAddress);
    // setMacAddress(macAddress);
    // setDhcpServer(dhcpServer);
    // setNetworkInterface(networkInterface);
    // setDnsName(dnsName);
    // }

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

    public String getNetworkInterface() {
        return mNetworkInterface;
    }

    public void setNetworkInterface(String networkInterface) {
        this.mNetworkInterface = networkInterface;
    }

    public String getDnsName() {
        return mDnsName;
    }

    public void setDnsName(String dnsName) {
        this.mDnsName = dnsName;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[" + this.getClass().getSimpleName() + "] ");
        buffer.append(this.getDhcpServer() + "(" + this.getNetworkInterface()
                + "): " + this.getIpAddress() + " " + this.getMacAddress());
        return buffer.toString();
    }

}
