# BalticWebIdReg
Mock-up BalticWeb ID Registry. 

The ID Registry project is comprised of the following sub-projects:

* bw-idreg-client: A client library that may be used by applications that wish to integrated with the ID Registry.
* bw-idreg-ldap: An embedded ApacheDS LDAP server including custom schema and sample data.
* bw-idreg-authsrv: Scripts to download, configure and run the JBoss Keycloak server.

## Prerequisites

* Java 8
* Maven 3
* MySQL

## Build The Project

    mvn clean install

## Start LDAP

    java -jar bw-idreg-ldap/target/bw-idreg-ldap-0.1-SNAPSHOT.jar \ 
        ldapserver \
        -dir /tmp/ldap

## Create keycloak DB

    mysql -u root -p < bw-idreg-authsvr/create-database.sql

## Install and configure Keycloak (in parent directory)

    ./bw-idreg-authsvr/install-keycloak.sh

## Run keycloak

    # The first time around, use this command to import a balticweb realm:
    ./bw-idreg-authsvr/realm/import.sh
    
    # Subsequently, start Keycloak normally
    ./keycloak-1.5.0.Final/bin/standalone.sh

## Using LDAPS
  
    ./keycloak-1.5.0.Final/bin/standalone.sh \
        -Djavax.net.ssl.trustStore=bw-idreg-ldap/ldaps/ldap-truststore.jks \
        -Djavax.net.ssl.trustStorePassword=changeit
