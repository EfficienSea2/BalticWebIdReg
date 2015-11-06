#!/bin/bash

DIR=`dirname $0`

LDAP_SERVER=$DIR/bw-idreg-ldap/target/bw-idreg-ldap-0.1-SNAPSHOT.jar
LDAP_DB=$HOME/.bw-ldap
LDAP_FILE_ARG=

# Check if we need to initialize the lDAP DB
if [[ $1 == -init ]]
then
  echo "Deleting old LDAP DB $LDAP_DB"
  rm -rf $LDAP_DB

  if [ -f "$2" ]
  then
    LDAP_FILE_ARG="-f $2"
  fi
fi


# Start the LDAP server
if [ -f $LDAP_SERVER ];
then
   echo "Starting ldapserver with arguments: -dir $LDAP_DB $LDAP_FILE_ARG"
   java -jar $LDAP_SERVER \
      ldapserver \
      $LDAP_FILE_ARG \
      -dir $LDAP_DB &
else
   echo "Please build the project using 'mvn package' first."
   exit 0
fi


