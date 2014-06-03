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
