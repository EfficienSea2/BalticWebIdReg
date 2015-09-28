# BalticWeb ID Registry Demo Client

This web application is a very simple demonstration of how to integrate with the BalticWeb ID Registry
using the bw-idreg-client OpenID Connect client library.

## Prerequisites

* Java JDK 1.8
* Maven 3.x

## Building

    mvn clean install

## Running

Start the web application by running the net.e2.bw.idreg.Application.main() method, or by running the command:
    
    java -jar target/bw-idreg-web-0.1-SNAPSHOT.war

You can access the application at http://localhost:9000

Make sure that src/main/resources/keycloak.json is a valid Keycloak client configuration file. By default, it assumes
that you are running the Keycloak server configured in the bw-idreg-authsvr project.

You can also start the demo client with the VM argument: -Dkeycloak.json=/path/to/keycloak.json




