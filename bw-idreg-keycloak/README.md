# BalticWeb Keycloak Authentication Server

This project contains the resources needed for configuring a JBoss Keycloak server, such
that it serves as a BalticWeb ID Registry authentication server.

Follow the steps below to install, configure and run the server.

## Prerequisites

* Java 8
* Maven 3
* MySQL

### Start LDAP

Make sure that the LDAP server of the bw-idreg-ldap project is running. Example:

    java -jar ../bw-idreg-ldap/target/bw-idreg-ldap-0.1-SNAPSHOT.jar \ 
        ldapserver \
        -dir /tmp/ldap

### Create keycloak DB

    mysql -u root -p < create-database.sql

### Install Wildfly
    
    ../wildfly-config/install-wildfly.sh
    
## Configure Keycloak

    ./check-install-keycloak.sh

## Start Wildfly (in parent directory)

    # The first time around, use this command to import a balticweb realm:
    ./bw-idreg-keycloak/realm/import.sh
    
    # Subsequently, start Wildfly normally
    ./wildfly-9.0.1.Final/bin/standalone.sh

# Run Wildfly against LDAPS (in parent directory)

    ./wildfly-9.0.1.Final/bin/standalone.sh \
        -Djavax.net.ssl.trustStore=bw-idreg-ldap/ldaps/ldap-truststore.jks \
        -Djavax.net.ssl.trustStorePassword=changeit
