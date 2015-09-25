

## Create keycloak DB

    mysql -u root -p < create-database.sql

## Install and configure Keycloak (in parent directory)

   ./install-keycloak.sh


## Run keycloak

    ./keycloak-1.5.0.Final/bin/standalone.sh

