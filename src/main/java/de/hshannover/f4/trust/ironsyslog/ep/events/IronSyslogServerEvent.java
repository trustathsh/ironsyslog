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

import java.nio.charset.Charset;
import java.util.Date;

import org.apache.log4j.Logger;

import com.nesscomputing.syslog4j.SyslogFacility;
import com.nesscomputing.syslog4j.SyslogLevel;
import com.nesscomputing.syslog4j.server.SyslogServerEventIF;
import com.nesscomputing.syslog4j.server.impl.event.SyslogServerEvent;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Copy-Paste implmenteation of {@link SyslogServerEvent} adding a creation
 * timestamp and a unique ID. Only available for creation using an existing
 * SyslogServerEventIF in the constructor.
 * 
 * @author Leonard Renners
 * 
 */
public class IronSyslogServerEvent extends Event implements SyslogServerEventIF {
    private static final Logger LOGGER = Logger
            .getLogger(IronSyslogServerEvent.class);

    public static final String DATE_FORMAT = "MMM dd HH:mm:ss yyyy";

    protected Charset mCharSet;
    protected byte[] mRawBytes;
    protected Date mDate;
    protected SyslogLevel mLevel;
    protected SyslogFacility mFacility;
    protected String mHost;
    protected boolean mIsHostStrippedFromMessage;
    protected String mMessage;

    /**
     * 
     * @param event
     */
    public IronSyslogServerEvent(SyslogServerEventIF event) {
        this.mCharSet = event.getCharSet();
        this.mRawBytes = event.getRaw();
        this.mDate = event.getDate();
        this.mLevel = event.getLevel();
        this.mFacility = event.getFacility();
        this.mHost = event.getHost();
        this.mIsHostStrippedFromMessage = event.isHostStrippedFromMessage();
        this.mMessage = event.getMessage();
    }

    public SyslogFacility getFacility() {
        return this.mFacility;
    }

    public void setFacility(SyslogFacility facility) {
        this.mFacility = facility;
    }

    /**
     * Copy-Paste implementation of {@link SyslogServerEvent}
     * 
     * @see SyslogServerEvent
     * 
     * @return The raw syslog data
     */
    @SuppressFBWarnings("EI_EXPOSE_REP")
    public byte[] getRaw() {
        byte[] newRawBytes = new byte[this.mRawBytes.length];
        System.arraycopy(this.mRawBytes, 0, newRawBytes, 0,
                this.mRawBytes.length);
        return newRawBytes;
    }

    @SuppressFBWarnings("EI_EXPOSE_REP")
    public Date getDate() {
        return this.mDate;
    }

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setDate(Date date) {
        this.mDate = date;
    }

    public SyslogLevel getLevel() {
        return this.mLevel;
    }

    public void setLevel(SyslogLevel level) {
        this.mLevel = level;
    }

    public String getHost() {
        return this.mHost;
    }

    public void setHost(String host) {
        this.mHost = host;
    }

    public boolean isHostStrippedFromMessage() {
        return mIsHostStrippedFromMessage;
    }

    public String getMessage() {
        return this.mMessage;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }

    @Override
    public Charset getCharSet() {
        return this.mCharSet;
    }

    @Override
    public void setCharSet(Charset charSet) {
        this.mCharSet = charSet;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[" + this.getClass().getSimpleName() + "] ");
        for (int i = 0; i < this.getRaw().length; i++) {
            buffer.append((char) this.getRaw()[i]);
        }
        return buffer.toString();
    }
}
