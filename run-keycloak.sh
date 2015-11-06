#!/bin/bash

DIR=`dirname $0`

DIR=`dirname $0`
source $DIR/bw-idreg-authsvr/keycloak-env.sh


if [ -f $KEYCLOAK_PATH/bin/standalone.sh ];
then
   echo "Running Keycloak with a port offset of 10."
   $KEYCLOAK_PATH/bin/standalone.sh \
       -Djboss.socket.binding.port-offset=$KEYCLOAK_PORT_OFFSET
else
   echo "Please install Keycloak first."
   exit 0
fi
