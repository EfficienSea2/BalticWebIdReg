


keytool -keystore ldap-keystore.jks -genkey -v -keyalg RSA -alias ldap -storepass changeit -keypass changeit -validity 3650 -dname "CN=ldap, O=BalticWeb, C=DK"
keytool -keystore ldap-keystore.jks -exportcert -v -alias ldap -storepass changeit -file ldap.der
keytool -keystore ldap-truststore.jks -importcert -v -noprompt -trustcacerts -storepass changeit -alias ldap -file ldap.der
rm ldap.der

