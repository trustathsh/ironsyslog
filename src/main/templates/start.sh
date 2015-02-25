#!/bin/sh
cd `dirname $0`
java -cp .:ironsyslog.jar de.hshannover.f4.trust.ironsyslog.IronSyslog $*
