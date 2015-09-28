bw-idreg-ldap
==============

The BalticWeb ApacheDS LDAP Server is used as the main repository for user-related data.

The project contains an embedded ApacheDS LDAP server along with custom LDAP schemas and sample data.

It also implements the ldapsearch command, in case your computer does not support this out of the box.


## Prerequisites

* Java 8
* Maven 3

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

    java -jar target/bw-idreg-ldap-0.1-SNAPSHOT.jar \
        ldapserver \
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

    java -jar target/bw-idreg-ldap-0.1-SNAPSHOT.jar \
        ldapsearch \
        "(objectClass=vessel)" member

