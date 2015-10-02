#!/bin/bash

DIR=`dirname $0`

java -jar $DIR/target/bw-idreg-ldap-0.1-SNAPSHOT.jar \
  ldapserver \
  -dir /tmp/ldap \
  >output.log 2>&1 &

