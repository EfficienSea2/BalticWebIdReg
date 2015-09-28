#!/bin/sh

#
# Starts keycloak and imports the "balticweb" realm
#

pushd `dirname $0` > /dev/null
SCRIPTPATH=`pwd`
echo "Running in folder $SCRIPTPATH"

echo "*************************************************"
echo "* Starting Keycloak - importing balticweb realm *"
echo "*************************************************"
../../keycloak-1.5.0.Final/bin/standalone.sh \
   -Dkeycloak.migration.action=import \
   -Dkeycloak.migration.provider=singleFile \
   -Dkeycloak.migration.file=$SCRIPTPATH/balticweb-realm.json \
   -Dkeycloak.migration.strategy=OVERWRITE_EXISTING

popd > /dev/null


