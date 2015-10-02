#!/bin/bash

# Set up the wildfly env 
DIR=`dirname $0`
source $DIR/../wildfly-conf/wildfly-env.sh

KEYCLOAK_VERSION=1.5.0.Final
KEYCLOAK=keycloak-overlay-$KEYCLOAK_VERSION

FILE=$WILDFLY_PATH/standalone/configuration/keycloak-server.json

if [ -f $FILE ];
then
   echo "Keycloak already installed."
else
   echo "Installing Keycloak at $WILDFLY_PATH."
   curl -L https://downloads.jboss.org/keycloak/$KEYCLOAK_VERSION/$KEYCLOAK.tar.gz | tar zx -C $WILDFLY_PATH 
   $WILDFLY_PATH/bin/jboss-cli.sh --file=$DIR/check-configure-keycloak.cli
   cp -r $DIR/balticweb $WILDFLY_PATH/standalone/configuration/themes/
fi

