# BalticWeb OpenID Connect client library #

The mock-up BalticWeb ID Registry supports authentication via the OpenID Connect protocol using the 
JBoss Keycloak server.

Any client application that wishes to integrate with the ID Registry must thus implement the client side
of the OpenID Connect protocol. 

One option is to integrate one of the supported Keycloak client adapters (as described in the Keycloak documentation).
Alternatively, use the client API of this project which is very thin and non-intrusive.

## Prerequisites

* Java JDK 1.8
* Maven 3.x

## Building

    mvn clean install

## Using the client library

In your client application, include a dependency on the OpenID Connect client library:

    <dependency>
        <groupId>net.e2.bw.idreg</groupId>
        <artifactId>bw-idreg-client</artifactId>
        <version>${bw-idreg-client.version}</version>
    </dependency>

You may also have to add the DMA repositories to your POM file:

    <repositories>
        <repository>
            <id>dma-releases</id>
            <name>Dma Release Repository</name>
            <url>http://repository-dma.forge.cloudbees.com/release/</url>
        </repository>
        <repository>
            <id>dma-snapshots</id>
            <name>Dma Snapshot Repository</name>
            <url>http://repository-dma.forge.cloudbees.com/snapshot/</url>
        </repository>
    </repositories>

The OpenID Connect client library must be initialized with a keycloak.json file. This file is generated in the 
Keycloak server after setting up your Keycloak client (please refer to the Keycloak documentation).

Example:

    try (Reader configFile = 
         new InputStreamReader(getClass().getResourceAsStream("/keycloak.json"))) {
        oidcClient = OIDCClient.newBuilder()
            .configuration(configFile)
            .customClaims("mmsi")
            .build();
    }

Next, you can use the oidcClient to redirect to the Keycloak server for authentication requests, and for
handling the Keycloak server callbacks.

The sandbox/br-idreg-web project contains a very simple web application that demonstrates how to use of the OpenID 
Connect library.
