#!/bin/sh

# Set up the wildfly env
DIR=`dirname $0`
source $DIR/../../wildfly-conf/wildfly-env.sh

#
# Starts keycloak and imports the "balticweb" realm
#

echo "*************************************************"
echo "* Starting Keycloak - importing balticweb realm *"
echo "*************************************************"
$WILDFLY_PATH/bin/standalone.sh \
   -Dkeycloak.migration.action=import \
   -Dkeycloak.migration.provider=singleFile \
   -Dkeycloak.migration.file=$DIR/balticweb-realm.json \
   -Dkeycloak.migration.strategy=OVERWRITE_EXISTING

