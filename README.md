# BalticWebIdReg
Mock-up BalticWeb ID Registry

## Prerequisites

Java 8
Maven 3
MySQL


## Build The Project

    mvn clean install

## Start LDAP

    java -jar bw-idreg-ldap/target/bw-idreg-ldap-0.1-SNAPSHOT.jar ldapserver -dir /tmp/ldap

## Create keycloak DB

    mysql -u root -p < bw-idreg-authsvr/create-database.sql

## Install and configure Keycloak (in parent directory)

   ./bw-idreg-authsvr/install-keycloak.sh

## Run keycloak

    ./keycloak-1.5.0.Final/bin/standalone.sh



