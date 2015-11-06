#!/bin/sh

#
# Starts keycloak and exports the "balticweb" realm
#

DIR=`dirname $0`
source $DIR/../keycloak-env.sh

REALM_DIR=$KEYCLOAK_CONF_DIR/realm
REALM_FILE=$REALM_DIR/balticweb-realm.json

# Check if a realm file has been specified
if [ -n "$1" ]
then
   REALM_FILE="$1"
fi

echo "*************************************************"
echo "* Starting Keycloak - exporting $REALM_FILE     *"
echo "* When started, stop it again using Ctrl-C      *"
echo "*************************************************"
$KEYCLOAK_PATH/bin/standalone.sh \
   -Djboss.socket.binding.port-offset=$KEYCLOAK_PORT_OFFSET \
   -Dkeycloak.migration.action=export \
   -Dkeycloak.migration.provider=singleFile \
   -Dkeycloak.migration.file=$REALM_FILE \
   -Dkeycloak.migration.realmName=balticweb \
   -Dkeycloak.migration.usersExportStrategy=SKIP


popd > /dev/null


