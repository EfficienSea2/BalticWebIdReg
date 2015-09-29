/* Copyright (c) 2011 Danish Maritime Authority.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.e2.bw.idreg.db2ldif;


import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Bootstrap class used for converting a Teamwork.com DB to LDIF
 */
public class Db2Ldif {

    public static final String NL = System.lineSeparator();

    @Parameter(names = "-u", description = "The DB user")
    String dbUser = "e2";

    @Parameter(names = "-p", description = "The DB password")
    String dbPassword = "e2";

    @Parameter(names = "-db", description = "The DB URL")
    String dbDatabase = "jdbc:mysql://localhost:3306/e2";

    @Parameter(names = "-peopleDN", description = "The DN for the People LDAP entry")
    String peopleDN = "ou=people,dc=balticweb,dc=net";

    @Parameter(names = "-groupsDN", description = "The DN for the Groups LDAP entry")
    String groupsDN = "ou=groups,dc=balticweb,dc=net";

    @Parameter(names = "-o", description = "The output file")
    String out = null;

    /**
     * Main method
     * @param args runtime arguments
     */
    public static void main(String[] args) throws Exception {
        Db2Ldif db2ldif = new Db2Ldif();
        new JCommander(db2ldif, args);
        db2ldif.execute();
    }

    /**
     * Executes the DB-to-LDIF conversion
     */
    private void execute() {

        // You may just specify the DB name
        if (!dbDatabase.startsWith("jdbc:mysql")) {
           dbDatabase = "jdbc:mysql://localhost:3306/" + dbDatabase;
        }

        // Instantiate a DB
        TeamworkDB db = new TeamworkDB(dbDatabase, dbUser, dbPassword);

        StringBuilder ldif = new StringBuilder();
        Map<String, String> userNames = new HashMap<>();

        importUsers(db, ldif, userNames);

        importGroups(db, ldif, userNames);

        if (out == null) {
            System.out.println(ldif);
        } else {
            try {
                Files.write(Paths.get(out), ldif.toString().getBytes("UTF-8"), StandardOpenOption.CREATE);
                System.out.println("Wrote LDIF to " + out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Generate LDIF for all users
     * @param db the database
     * @param ldif the LDIF file to append to
     */
    @SuppressWarnings("all")
    private void importUsers(TeamworkDB db, StringBuilder ldif, Map<String, String> userNames) {
        long t0 = System.currentTimeMillis();
        String sql = loadResourceText("/users.sql");

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = db.getConnection();

            stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            int index = 0;
            while (rs.next()) {
                String userName = TeamworkDB.getString(rs, "userName");
                String userId = TeamworkDB.getString(rs, "userId");
                String userFirstName = TeamworkDB.getString(rs, "userFirstName");
                String userLastName = TeamworkDB.getString(rs, "userLastName");
                String userEmail = TeamworkDB.getString(rs, "userEmail");

                if (userNames.containsValue(userName)) {
                    userName = userId;
                }
                userName = userName.replaceAll("\\.", "_");
                userNames.put(userId, userName);

                ldif.append("dn: uid=").append(userName).append(",").append(peopleDN).append(NL);
                ldif.append("objectclass: top").append(NL);
                ldif.append("objectclass: organizationalPerson").append(NL);
                ldif.append("objectclass: inetOrgPerson").append(NL);
                ldif.append("ou: people").append(NL);
                ldif.append("uid: ").append(userName).append(NL);
                ldif.append("cn: ").append(userFirstName).append(" ").append(userLastName).append(NL);
                ldif.append("givenName: ").append(userFirstName).append(NL);
                ldif.append("sn: ").append(userLastName).append(NL);
                ldif.append("mail: ").append(userEmail).append(NL);
                ldif.append("userpassword:: ").append("e1NTSEF9QTM3TkF4K0l1Z25UZS8vTHJPbWFOczdZeGVNSk4xeVQ=").append(NL);
                ldif.append(NL);

                index++;
            }
            rs.close();
            System.out.println(String.format("Fetched %d users in %d ms", index, System.currentTimeMillis() - t0));

        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception ex) { }
            try { if (conn != null) conn.close(); } catch (Exception ex) { }
        }
    }

    /**
     * Generate LDIF for all groups
     * @param db the database
     * @param ldif the LDIF file to append to
     */
    @SuppressWarnings("all")
    private void importGroups(TeamworkDB db, StringBuilder ldif, Map<String, String> userNames) {
        long t0 = System.currentTimeMillis();
        String sql = loadResourceText("/groups.sql");

        Map<String, StringBuilder> groups = new LinkedHashMap<>();

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = db.getConnection();

            stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            int index = 0;
            while (rs.next()) {
                String userId = TeamworkDB.getString(rs, "userId");
                String companyName = TeamworkDB.getString(rs, "companyName");

                StringBuilder g = groups.get(companyName);
                if (!groups.containsKey(companyName)) {
                    g = new StringBuilder();
                    groups.put(companyName, g);

                    g.append("dn: cn=").append(companyName).append(",").append(groupsDN).append(NL);
                    g.append("objectClass: top").append(NL);
                    g.append("objectClass: groupOfUniqueNames").append(NL);
                    g.append("cn: ").append(companyName).append(NL);
                }

                String userName = userNames.get(userId);
                g.append("uniqueMember: uid=").append(userName).append(",").append(peopleDN).append(NL);

                index++;
            }
            rs.close();

            groups.values().stream()
                    .forEach(g -> ldif.append(g).append(NL));

            System.out.println(String.format("Fetched %d groups in %d ms", index, System.currentTimeMillis() - t0));

        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception ex) { }
            try { if (conn != null) conn.close(); } catch (Exception ex) { }
        }

    }

    /**
     * Loads and returns the text of the given file. The file must be placed
     * in the same package as the class.
     *
     * @param resourceName the name of the file
     * @return the text content of the file
     */
    public static String loadResourceText(String resourceName) {
        try (BufferedReader r = new BufferedReader(new InputStreamReader(Db2Ldif.class.getResourceAsStream(resourceName), "UTF-8"))) {
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                result.append(line).append(System.lineSeparator());
            }
            return result.toString();
        } catch (Exception ex) {
            throw new IllegalArgumentException("Undefined resource " + resourceName, ex);
        }
    }

}
