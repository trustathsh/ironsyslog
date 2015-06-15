ironsyslog
============

ironsyslog is a *highly experimental* integration of diverse Log-Messages
Systems into a MAP-Infrastructure. The integration aims to share security related informations, 
given by many  systems, with other network components in the [TNC architecture] [1]
via IF-MAP.

ironsyslog consists of three components:

* The "syslog server" - gets the information from the logging clients (they need to be configured to use this syslog server as the - where necessary additional - logging point) 
* An Event Processing Engine processes the different log messages as a handler for syslog events and fetches the latest information.
* The Publisher consumes the high-level events, where the information are transformed into IF-MAP Metadata and  published to a MAP server.
  
  The metadata that ironsyslog publishes depends on the mapping defined by the rules in the event processing step.

The binary package (`ironsyslog-x.x.x-bundle.zip`) of ironsyslog
is ready to run, all you need is to configure it to your needs.
If you like to build ironsyslog by your own you can use the
latest code from the [GitHub repository][githubrepo].


Requirements
============
To use the binary package of ironsyslog you need the following components:

* OpenJDK Version 1.7 or higher
* MAP server implementation (e.g. [irond] [3])
* Clients logging toward the syslog server
* optionally ironGui to see whats going on

To build and comile source code by yourself Maven 3 is also needed.


Configuration
=============
To setup the binary package you need to import the ironsyslog and MAP server
certificates into `ironsyslog.jks`.
If you want to use ironsyslog with irond the keystores of both are configured 
with ready-to-use testing certificates.

The remaining configuration parameters can be done through the
`ironsyslog.properties` file in the ironsyslog package.
In general you have to specify:

* the syslog server ip, port and protocol where ironsyslog is supposed to be listening on,
* the MAP-Server URL and credentials.



Secondly you have to setup the rules:

* Some example rules are provided in the `rules` folder.
* Configure the enabled rules in the `config.yml`

For example here is a rule to extract the information if a dhcpcd event (ip-mac assertions):
    
    rule "dhcpd DHCPACK"
    when
    	$ise:IronSyslogServerEvent(message.contains("dhcpd"), message.contains("DHCPACK"))
    then
    	debug("Rule \"" + drools.getRule().getName() + "\" fired.");
    	String message = $ise.getMessage();
    	Pattern p = Pattern.compile("dhcpd: DHCPACK on (\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}) to (..:..:..:..:..:..) via (.*)");
    	Matcher m = p.matcher(message);
    	if (m.find()) {
    		String networkInterface = m.group(3);
    		String ipAddress = m.group(1);
    		String macAddress = m.group(2);
    		insert ( new DhcpAckEvent(ipAddress, macAddress, $ise.getHost(), networkInterface) );
    	}
    end
The according configuration file:

     # All rule files have to end with the ".drl" extension and follow the following folder structure:
    \# - rules/drools/
    \#   - service/ : Syslog service rule for event transformation
    \#   - publish/ : ifmap rule for event publishing
    \#   - other/* : all remaining rules
    \# Inside the "other" folder an arbitrary folder structure can be used
    \# Services in the service/ folder to be included
    serviceInclude:
      - dnsmasq-dhcp
    \# Publish rules in the publish/ folder to be excluded
    publishExclude:


Building
========
You can build ironsyslog by executing:

	$ mvn package

in the root directory of the ironsyslog project.
Maven should download all further needed dependencies for you. After a successful
build you should find the `ironsyslog-x.x.x-bundle.zip` in the `target` sub-directory.


Running
=======
To run the binary package of ironsyslog simply execute:

	$ ./start.sh


Feedback
========
If you have any questions, problems or comments, please contact
	<trust@f4-i.fh-hannover.de>


LICENSE
=======
ironsyslog is licensed under the [Apache License, Version 2.0] [4].


Note
====

ironsyslog is an experimental prototype and is not suitable for actual use.

Feel free to fork/contribute.


[1]: http://www.trustedcomputinggroup.org/developers/trusted_network_connect
[2]: http://logstash.net/docs/1.4.2/
[3]: https://github.com/trustathsh/irond
[4]: http://www.apache.org/licenses/LICENSE-2.0.html
[githubrepo]: https://github.com/trustathsh/ironsyslog
[ifmapj]: https://github.com/trustathsh/ifmapj.git
[trustathsh]: http://trust.f4.hs-hannover.de
[hshannover]: https://www.hs-hannover.de/
