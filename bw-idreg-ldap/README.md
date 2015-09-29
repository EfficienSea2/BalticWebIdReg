bw-idreg-ldap
==============

The BalticWeb ApacheDS LDAP Server is used as the main repository for user-related data.

The project contains an embedded ApacheDS LDAP server along with custom LDAP schemas and sample data.

It also implements the ldapsearch command, in case your computer does not support this out of the box.

You can turn on LDAPS (LDAP over SSL) by specifying a keystore and keystore password on the server side,
and corresponding truststore and truststore password parameters for the ldapsearch command.

For test purposes, there is a keystore and a truststore available in the "ldaps" folder with a
self-signed certificate that answers to the password "changeit".


## Prerequisites

* Java 8
* Maven 3

## Building ##

    mvn clean install

## Using ldapserver

The ldap server is invoked using the following format:

**java -jar target/bw-idreg-ldap-0.1-SNAPSHOT.jar ldapserver 
  [-b *baseDN*]  [-p *port*] [-dir *directory*]
  [-ks *keystore*] [-ksp *keystorePassword*]**
    
| Option    | Description |
| --------- | ------------|
| b         | The base DN to use |
| p         | The port |
| dir       | The data directory |
| ks        | The path to a keystore used for LDAPS |
| ksp       | The password for the keystore used for LDAPS |

Example:

    java -jar target/bw-idreg-ldap-0.1-SNAPSHOT.jar \
        ldapserver \
        -dir /tmp/ldap \
        -ks bw-idreg-ldap/ldaps/ldap-keystore.jks \
        -ksp changeit
    
## Using ldapsearch
The ldap search command is invoked using the following format:

**java -jar target/bw-idreg-ldap-0.1-SNAPSHOT.jar ldapsearch 
  [-ts *keystore*] [-tsp *keystorePassword*]
  [-b *baseDN*]  [-p *port*] 
  [-h *host*] [-D *bindDN*] [-w bindPassword]
  [filter [attrs]]**
    
| Option    | Description |
| --------- | ------------|
| b         | The base DN to use |
| p         | The port |
| h         | The host |
| D         | The bind dn |
| w         | The bind password |
| ts        | The path to a truststore used for LDAPS |
| tsp       | The password for the truststore used for LDAPS |


Example:

    java -jar target/bw-idreg-ldap-0.1-SNAPSHOT.jar \
        ldapsearch \
        "(objectClass=vessel)" member

