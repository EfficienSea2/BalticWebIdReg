bw-idreg-ldap
==========================

BalticWeb LDAP utilities.

## Prerequisites

* Java 8
* Maven (for building)

## Building ##

    mvn clean install

## Using ldapserver
The ldap server is invoked using the following format:

**java -jar target/bw-idreg-ldap-0.1-SNAPSHOT.jar ldapserver 
  [-b *baseDN*]  [-p *port*] [-dir *directory*]**
    
| Option    | Description |
| --------- | ------------|
| b         | The base DN to use |
| p         | The port |
| dir       | The data directory |

Example:

    java -jar target/bw-idreg-ldap-0.1-SNAPSHOT.jar ldapserver \
        -dir /tmp/ldap
    
## Using ldapsearch
The ldap search command is invoked using the following format:

**java -jar target/bw-idreg-ldap-0.1-SNAPSHOT.jar ldapsearch 
  [-b *baseDN*]  [-p *port*] [-dir *directory*]
  [-h *host*] [-D *bindDN*] [-w bindPassword]
  [filter [attrs]]**
    
| Option    | Description |
| --------- | ------------|
| b         | The base DN to use |
| p         | The port |
| h         | The host |
| D         | The bind dn |
| w         | The bind password |


Example:

    java -jar target/bw-idreg-ldap-0.1-SNAPSHOT.jar ldapsearch "(objectClass=vessel)" member

