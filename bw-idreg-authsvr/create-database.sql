
CREATE DATABASE keycloak CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE USER 'keycloak'@'localhost' IDENTIFIED BY 'keycloak';
GRANT ALL PRIVILEGES ON *.* TO 'keycloak'@'localhost' WITH GRANT OPTION;
CREATE USER 'keycloak'@'%' IDENTIFIED BY 'keycloak';
GRANT ALL PRIVILEGES ON *.* TO 'keycloak'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;


