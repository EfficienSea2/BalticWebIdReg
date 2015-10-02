#!/bin/sh

# Set up the wildfly env
DIR=`dirname $0`
source $DIR/../../wildfly-conf/wildfly-env.sh

#
# Starts keycloak and exports the "balticweb" realm
#

echo "**********************************************"
echo "* Starting Keycloak - when started ctrl-C it *"
echo "**********************************************"
$WILDFLY_PATH/bin/standalone.sh \
   -Dkeycloak.migration.action=export \
   -Dkeycloak.migration.provider=singleFile \
   -Dkeycloak.migration.file=$DIR/balticweb-realm.json \
   -Dkeycloak.migration.realmName=balticweb \
   -Dkeycloak.migration.usersExportStrategy=SKIP

