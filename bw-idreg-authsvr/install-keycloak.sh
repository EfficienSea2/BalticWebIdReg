#!/bin/sh

#
# Installs a Keycloak instance 
#

pushd `dirname $0` > /dev/null
SCRIPTPATH=`pwd`
echo "Running in folder $SCRIPTPATH"


KEYCLOAK_VERSION=1.5.0.Final
KEYCLOAK=keycloak-$KEYCLOAK_VERSION
KEYCLOAK_PATH=../$KEYCLOAK

echo "Installing $KEYCLOAK"
rm -rf $KEYCLOAK_PATH
curl -L https://downloads.jboss.org/keycloak/$KEYCLOAK_VERSION/keycloak-$KEYCLOAK_VERSION.tar.gz | tar zx
mv $KEYCLOAK $KEYCLOAK_PATH

echo "Configuring keycloak to use MySQL"
java -jar saxon9he.jar -s:$KEYCLOAK_PATH/standalone/configuration/standalone.xml -xsl:changeDatabase.xsl -o:$KEYCLOAK_PATH/standalone/configuration/standalone.xml

echo "Installing MySQL driver"
mkdir -p $KEYCLOAK_PATH/modules/system/layers/base/com/mysql/jdbc/main
cp module.xml $KEYCLOAK_PATH/modules/system/layers/base/com/mysql/jdbc/main/
curl -o $KEYCLOAK_PATH/modules/system/layers/base/com/mysql/jdbc/main/mysql-connector-java-5.1.18.jar http://central.maven.org/maven2/mysql/mysql-connector-java/5.1.18/mysql-connector-java-5.1.18.jar

echo "Copying balticweb theme to keycloak"
cp -r balticweb $KEYCLOAK_PATH/standalone/configuration/themes/

popd > /dev/null

