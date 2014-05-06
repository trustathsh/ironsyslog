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
 * Event representing a failed login on a remote machine.
 * 
 * @author Leonard Renners
 * 
 */
public class LoginFailedEvent extends Event {

    private String mUserId;
    private String mUserIp;
    private String mServiceHost;
    private String mServiceName;
    private boolean mIsValidUser;

    /**
     * Constructor.
     * 
     * @param userId
     *            The user which login attempt has failed
     * @param userIp
     *            The IP from which the user tried to login
     * @param serviceHost
     *            The dns name of the server on which the user tried to login
     * @param serviceName
     *            The service on which the login attempt failed
     * @param isValidUser
     *            Wether the attempt was for an existing user
     */
    public LoginFailedEvent(String userId, String userIp, String serviceHost,
            String serviceName, boolean isValidUser) {
        super();
        setUserId(userId);
        setUserIp(userIp);
        setServiceHost(serviceHost);
        setServiceName(serviceName);
        setValidUser(isValidUser);
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        this.mUserId = userId;
    }

    public String getUserIp() {
        return mUserIp;
    }

    public void setUserIp(String userIp) {
        this.mUserIp = userIp;
    }

    public String getServiceHost() {
        return mServiceHost;
    }

    public void setServiceHost(String serviceHost) {
        this.mServiceHost = serviceHost;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[" + this.getClass().getSimpleName() + "] ");
        buffer.append(this.getServiceHost() + ": " + this.getUserIp() + " "
                + this.getUserId());
        return buffer.toString();
    }

    public boolean isValidUser() {
        return mIsValidUser;
    }

    public void setValidUser(boolean isValidUser) {
        this.mIsValidUser = isValidUser;
    }

    public String getServiceName() {
        return mServiceName;
    }

    public void setServiceName(String serviceName) {
        this.mServiceName = serviceName;
    }

}
