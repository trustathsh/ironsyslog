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

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import de.hshannover.f4.trust.ifmapj.IfmapJ;
import de.hshannover.f4.trust.ifmapj.IfmapJHelper;
import de.hshannover.f4.trust.ifmapj.channel.SSRC;
import de.hshannover.f4.trust.ifmapj.exception.IfmapErrorResult;
import de.hshannover.f4.trust.ifmapj.exception.IfmapException;
import de.hshannover.f4.trust.ifmapj.identifier.Identifiers;
import de.hshannover.f4.trust.ifmapj.identifier.Identity;
import de.hshannover.f4.trust.ifmapj.identifier.IdentityType;
import de.hshannover.f4.trust.ifmapj.identifier.IpAddress;
import de.hshannover.f4.trust.ifmapj.identifier.MacAddress;
import de.hshannover.f4.trust.ifmapj.messages.PublishDelete;
import de.hshannover.f4.trust.ifmapj.messages.PublishRequest;
import de.hshannover.f4.trust.ifmapj.messages.Requests;
import de.hshannover.f4.trust.ifmapj.metadata.StandardIfmapMetadataFactory;
import de.hshannover.f4.trust.ironsyslog.ep.events.AggregatedLoginFailedEvent;
import de.hshannover.f4.trust.ironsyslog.ep.events.Event;
import de.hshannover.f4.trust.ironsyslog.ep.events.IpMacEvent;
import de.hshannover.f4.trust.ironsyslog.ep.events.MalwareDetectedEvent;
import de.hshannover.f4.trust.ironsyslog.util.Configuration;

/**
 * 
 * @author Leonard Renners
 * 
 */
public final class IronSyslogPublisher {

	private static StandardIfmapMetadataFactory mMf = IfmapJ
			.createStandardMetadataFactory();

	private static final Logger LOGGER = Logger
			.getLogger(IronSyslogPublisher.class);

	private static SSRC mSsrc;
	private static DocumentBuilder mDocumentBuilder;

	private static final String XMLNS = "http://simu-project.de/XMLSchema/1";

	/**
	 * Death constructor for code convention -> final class because utility
	 * class
	 */
	private IronSyslogPublisher() {

	}

	/**
	 * The init methode initiates the Ifmap Session and the XML Document Builder
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
		} catch (Exception e) {
			LOGGER.error("could not connect to ifmap server", e);
			System.exit(1);
		}
		LOGGER.info("Session successfully created");

		LOGGER.debug("Initialising DocumentBuilder");
		try {
			mDocumentBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOGGER.debug("DocumentBuilder successfully created");
	}

	/**
	 * Publishes an LoginFailed as an login-failed metadata in the ifmap server.
	 * 
	 * @param event
	 *            the event to publish
	 */
	public static void publishIfMap(Event event) {
		LOGGER.debug("Trying to publish an unsupported event -> doing nothing. "
				+ event);
	}

	/**
	 * Publishes an IpMacEvent as an meta-ip-mac metadata in the ifmap server.
	 * 
	 * @param event
	 *            the event to publish
	 */
	public static void publishIfMap(IpMacEvent event) {
		LOGGER.debug("Publishing: " + event);

		IpAddress ip = Identifiers.createIp4(event.getIpAddress());
		MacAddress mac = Identifiers.createMac(event.getMacAddress());

		publish(Requests.createPublishReq(Requests.createPublishUpdate(ip, mac,
				mMf.createIpMac())));
	}

	/**
	 * Publishes an MalwareEvent as an event metadata in the ifmap server.
	 * 
	 * @param event
	 *            the event to publish
	 */
	public static void publishIfMap(MalwareDetectedEvent event) {
		LOGGER.debug("Publishing: " + event);
		Document meta = mMf.createEvent(event.getName(), event.getDiscoveredTime(),
				event.getDiscovererId(), event.getMagnitude(),
				event.getConfidence(), event.getSignificance(), event.getType(),
				event.getOtherTypeDefinition(), event.getInformation(), event.getVulnerabilityUri());

		Identity dnsName = Identifiers.createIdentity(IdentityType.dnsName, event.getHost());
		
		publish(Requests.createPublishReq(Requests.createPublishUpdate(dnsName, meta)));
		// mMf.createEvent("Malware Found by Windows Event Log",
		// event.getTimestamp().toString(), "1", 0, 0, Significance.important,
		// EventType.other, "Virus", null, null);
		//
		//
		// publish(Requests.createPublishReq(Requests.createPublishUpdate(ip,
		// mac,
		// mMf.createIpMac())));
	}

	/**
	 * Publishes an LoginFailedEvent as an login-failed metadata in the ifmap
	 * server.
	 * 
	 * @param event
	 *            the event to publish
	 */
	public static void publishIfMap(AggregatedLoginFailedEvent event) {
		try {
			String xmlServiceIdentity = "<service name=\""
					+ event.getServiceName() + "\" host-name=\""
					+ event.getServiceHost()
					+ "\" administrative-domain=\"\" xmlns=\"" + XMLNS + "\"/>";
			String xmlLoginFailedIdentity = "<login-failed user=\""
					+ event.getUserId() + "\" ip=\"" + event.getUserIp()
					+ "\" service=\"" + event.getServiceName()
					+ "\" service-host=\"" + event.getServiceHost()
					+ "\" administrative-domain=\"\" xmlns=\"" + XMLNS
					+ "\" />";
			IpAddress userIp = Identifiers.createIp4(event.getUserIp());
			Identity userName = Identifiers.createIdentity(
					IdentityType.userName, event.getUserId());
			Identity service = Identifiers
					.createExtendedIdentity(xmlServiceIdentity);
			Identity loginFailed = Identifiers
					.createExtendedIdentity(xmlLoginFailedIdentity);

			if (event.getCount() > 0) {
				LOGGER.debug("Publishing: " + event);

				String xmlLoginFailedUser = "<simu:login-failed-user xmlns:simu=\""
						+ XMLNS + "\" ifmap-cardinality=\"singleValue\" />";
				String xmlLoginFailedId = "<simu:login-failed-id xmlns:simu=\""
						+ XMLNS + "\" ifmap-cardinality=\"singleValue\" />";
				String xmlLoginFailedIp = "<simu:login-failed-ip xmlns:simu=\""
						+ XMLNS + "\" ifmap-cardinality=\"singleValue\" />";
				String xmlLoginFailedInfo = "<simu:login-failed-info count=\""
						+ event.getCount() + "\" xmlns:simu=\"" + XMLNS
						+ "\" ifmap-cardinality=\"singleValue\" />";

				Document updateLoginFailedUser = createDocument(xmlLoginFailedUser);
				Document updateLoginFailedId = createDocument(xmlLoginFailedId);
				Document updateLoginFailedIp = createDocument(xmlLoginFailedIp);
				Document updateLoginFailedInfo = createDocument(xmlLoginFailedInfo);

				PublishRequest update = Requests.createPublishReq();
				update.addPublishElement(Requests.createPublishUpdate(service,
						loginFailed, updateLoginFailedId));
				update.addPublishElement(Requests.createPublishUpdate(userName,
						loginFailed, updateLoginFailedUser));
				update.addPublishElement(Requests.createPublishUpdate(userIp,
						loginFailed, updateLoginFailedIp));
				update.addPublishElement(Requests.createPublishUpdate(
						loginFailed, updateLoginFailedInfo));

				publish(update);
			} else {
				PublishDelete pd1 = Requests.createPublishDelete(loginFailed,
						service);
				PublishDelete pd2 = Requests.createPublishDelete(loginFailed,
						userName);
				PublishDelete pd3 = Requests.createPublishDelete(loginFailed,
						userIp);
				PublishDelete pd4 = Requests.createPublishDelete(loginFailed);

				PublishRequest del = Requests.createPublishReq();
				del.addPublishElement(pd1);
				del.addPublishElement(pd2);
				del.addPublishElement(pd3);
				del.addPublishElement(pd4);

				publish(del);
			}
		} catch (IfmapException e) {
			LOGGER.error("Error while creating extended identifiers.", e);
		}
	}

	/**
	 * Encapsulates the publish and error handling
	 * 
	 * @param req
	 *            the request to publish
	 */
	private static void publish(PublishRequest req) {
		try {
			mSsrc.publish(req);
			LOGGER.debug("Publish successful");
		} catch (IfmapErrorResult e) {
			LOGGER.error("Error while publishing: " + e.getMessage());
		} catch (IfmapException e) {
			LOGGER.error("Error while publishing: " + e.getMessage());
		}
	}

	/**
	 * Creates a W3C Document for a given XML String
	 * 
	 * @param xml
	 *            The xml to parse
	 * @return The corresponding document
	 */
	private static Document createDocument(String xml) {
		try {
			StringReader reader = new StringReader(xml);
			InputSource input = new InputSource(reader);
			return mDocumentBuilder.parse(input);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
}
