# DB2LDIF Project

This project can take a mysql-backup of the EfficienSea 2 Teamwork community site at
https://efficiensea2.teamwork.com/projects/84702/tasks, and convert it to an LDIF file
that may be imported in the ID Registry LDAP server.

## Prerequisites

* Java 8
* Maven 3
* MySQL

## Create e2 DB

    mysql -u root -p < db/create-database.sql

Next, in Teamwork, as an administrator go to the "Export" section and generate a new export.
Import the exported data into the e2 database (pwd is e2):

    mysql -u e2 -p e2 < ~/Downloads/231616_TeamworkPM_Backup_20151106.sql 

## Build the DB2LDIF project

    mvn clean install

## Generate LDIF from DB

    java -jar target/bw-idreg-db2ldif-0.1-SNAPSHOT.jar -o data.ldif -photos

# Import the LDIF in LDAP

The easiest way to import the LDIF file, is to start the LDAP server with the "-init" parameter. 
In the parent directory, run:

    ./run-ldap-server.sh -init data.ldif
    
   