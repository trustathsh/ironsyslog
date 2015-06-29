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
package de.hshannover.f4.trust.ironsyslog.ep.events;

import de.hshannover.f4.trust.ifmapj.metadata.EventType;
import de.hshannover.f4.trust.ifmapj.metadata.Significance;

/**
 * Event class corresponding to the IFMAP meta-event Metadata.
 * 
 * @author Leonard Renners
 * 
 */
public class MetaEvent extends Event {
	private String mName;
	private EventType mType;
	private String mOtherTypeDefinition;
	private String mDiscoveredTime;
	private String mDiscovererId;
	private int mMagnitude;
	private int mConfidence;
	private Significance mSignificance;
	private String mInformation;
	private String mVulnerabilityUri;


	/**
	 * Constructor. 
	 * 
	 * @param name The name of the event
	 * @param type The type of the event
	 * @param discoveredTime The time that the event has been discovered
	 * @param discovererId The element that discovered the characteristic
	 * @param magnitude Indicates how widespread the effects of the activity are
	 * @param confidence Indicates how confident it accurately describes the activity of interest
	 * @param significance Indicates how important the event is
	 * @param information A human consumable informational string
	 * @param vulnerabilityUri used for events with type cve to indicate which vulnerability was
detected
	 */
	public MetaEvent(String name, EventType type, String discoveredTime, String discovererId,
			int magnitude, int confidence, Significance significance,
			String information, String vulnerabilityUri) {
		setName(name);
		setType(type);
		setDiscoveredTime(discoveredTime);
		setDiscovererId(discovererId);
		setMagnitude(magnitude);
		setConfidence(confidence);
		setSignificance(significance);
		setInformation(information);
		setVulnerabilityUri(vulnerabilityUri);
	}
	
	/**
	 * Constructor. 
	 * 
	 * @param name The name of the event
	 * @param type The type of the event
	 * @param discoveredTime The time that the event has been discovered
	 * @param discovererId The element that discovered the characteristic
	 * @param magnitude Indicates how widespread the effects of the activity are
	 * @param confidence Indicates how confident it accurately describes the activity of interest
	 * @param significance Indicates how important the event is
	 * @param information A human consumable informational string
	 * @param vulnerabilityUri used for events with type cve to indicate which vulnerability was
detected
	 */
	public MetaEvent(String name, EventType type, String discoveredTime, String discovererId,
			int magnitude, int confidence, int significance,
			String information, String vulnerabilityUri) {
		setName(name);
		setType(type);
		setDiscoveredTime(discoveredTime);
		setDiscovererId(discovererId);
		setMagnitude(magnitude);
		setConfidence(confidence);
		setSignificance(significance);
		setInformation(information);
		setVulnerabilityUri(vulnerabilityUri);
	}
	
	public String getName() {
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public EventType getType() {
		return mType;
	}

	public void setType(EventType type) {
		this.mType = type;
	}

	public String getVulnerabilityUri() {
		return mVulnerabilityUri;
	}

	public void setVulnerabilityUri(String vulnerabilityUri) {
		this.mVulnerabilityUri = vulnerabilityUri;
	}

	public String getDiscoveredTime() {
		return mDiscoveredTime;
	}

	public void setDiscoveredTime(String discoveredTime) {
		this.mDiscoveredTime = discoveredTime;
	}

	public String getDiscovererId() {
		return mDiscovererId;
	}

	public void setDiscovererId(String discovererId) {
		this.mDiscovererId = discovererId;
	}

	public int getMagnitude() {
		return mMagnitude;
	}

	public void setMagnitude(int magnitude) {
		this.mMagnitude = magnitude;
	}

	public int getConfidence() {
		return mConfidence;
	}

	public void setConfidence(int confidence) {
		this.mConfidence = confidence;
	}

	public Significance getSignificance() {
		return mSignificance;
	}

	public void setSignificance(Significance significance) {
		this.mSignificance = significance;
	}

	public void setSignificance(int significance) {
		this.mSignificance = Significance.values()[significance];
	}

	public String getInformation() {
		return mInformation;
	}

	public void setInformation(String information) {
		this.mInformation = information;
	}

	public String getOtherTypeDefinition() {
		return mOtherTypeDefinition;
	}

	public void setOtherTypeDefinition(String otherTypeDefinition) {
		this.mOtherTypeDefinition = otherTypeDefinition;
	}
	
//	@Override
//	public String toString() {
//		
//	}
}
