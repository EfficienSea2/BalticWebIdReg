#!/bin/sh

#
# Starts keycloak and exports the "balticweb" realm
#

pushd `dirname $0` > /dev/null
SCRIPTPATH=`pwd`
echo "Running in folder $SCRIPTPATH"

echo "**********************************************"
echo "* Starting Keycloak - when started ctrl-C it *"
echo "**********************************************"
../../keycloak-1.5.0.Final/bin/standalone.sh \
   -Dkeycloak.migration.action=export \
   -Dkeycloak.migration.provider=singleFile \
   -Dkeycloak.migration.file=$SCRIPTPATH/balticweb-realm.json \
   -Dkeycloak.migration.realmName=balticweb \
   -Dkeycloak.migration.usersExportStrategy=SKIP


popd > /dev/null


