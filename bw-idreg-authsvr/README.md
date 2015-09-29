# BalticWeb ID Registry Server

This project contains the resources needed for installing and configuring a JBoss keycloak server, such
that it serves as a BalticWeb ID Registry server.

Follow the steps below to install, configure and run the server.

## Prerequisites

* Java 8
* Maven 3
* MySQL

## Start LDAP

Make sure that the LDAP server of the bw-idreg-ldap project is running.
    
## Create keycloak DB

    mysql -u root -p < create-database.sql

## Install and configure Keycloak (in parent directory)

    ./install-keycloak.sh

## Run keycloak (in parent directory)

    # The first time around, use this command to import a balticweb realm:
    ./bw-idreg-authsvr/realm/import.sh
    
    # Subsequently, start Keycloak normally
    ./keycloak-1.5.0.Final/bin/standalone.sh

# Run keycloak against LDAPS 

    ./keycloak-1.5.0.Final/bin/standalone.sh \
        -Djavax.net.ssl.trustStore=bw-idreg-ldap/ldaps/ldap-truststore.jks \
        -Djavax.net.ssl.trustStorePassword=changeit
