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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

/**
 * Bootstrap class used for converting a Teamwork.com DB to LDIF
 */
public class Db2Ldif {

    public static final String NL = System.lineSeparator();

    Map<String, Company> companyData = new HashMap<>();

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

    @Parameter(names = "-photos", description = "Whether to include photos or not")
    boolean includePhotos = false;

    @Parameter(names = "-o", description = "The output file")
    String out = null;

    /**
     * Main method
     * @param args runtime arguments
     */
    public static void main(String[] args) throws Exception {
        Db2Ldif db2ldif = new Db2Ldif();

        // Preload company data from json file
        ObjectMapper mapper = new ObjectMapper();
        List<Company> companies = mapper.readValue(Db2Ldif.class.getResource("/companies.json"), new TypeReference<List<Company>>() {});
        companies.forEach(c -> db2ldif.companyData.put(c.uid, c));

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
                String entryUUID = TeamworkDB.getString(rs, "userId");
                String userFirstName = TeamworkDB.getString(rs, "userFirstName");
                String userLastName = TeamworkDB.getString(rs, "userLastName");
                String userEmail = TeamworkDB.getString(rs, "userEmail");
                String companyId = TeamworkDB.getString(rs, "companyName");
                String userImage = TeamworkDB.getString(rs, "userImage");

                // Normalize user and company names
                userName = userName.replace('.', '_');
                companyId = companyId(companyId);

                // Make sure the name is unique
                if (userNames.containsValue(userName)) {
                    int suffix = 2;
                    while (userNames.containsValue(userName + suffix)) {
                        suffix++;
                    }
                    userName = userName + suffix;
                }
                userNames.put(entryUUID, userName);

                ldif.append("dn: uid=").append(userName).append(",").append(peopleDN).append(NL);
                ldif.append("objectclass: top").append(NL);
                ldif.append("objectclass: organizationalPerson").append(NL);
                ldif.append("objectclass: inetOrgPerson").append(NL);
                ldif.append("objectclass: maritimeResource").append(NL);
                ldif.append("ou: people").append(NL);
                ldif.append("mrn: ").append("urn:mrn:mc:user:").append(companyId).append(":").append(userName).append(NL);
                ldif.append("uid: ").append(userName).append(NL);
                ldif.append("cn: ").append(userFirstName).append(" ").append(userLastName).append(NL);
                ldif.append("givenName: ").append(userFirstName).append(NL);
                ldif.append("sn: ").append(userLastName).append(NL);
                ldif.append("mail: ").append(userEmail).append(NL);
                ldif.append("userpassword:: ").append("e1NTSEF9QTM3TkF4K0l1Z25UZS8vTHJPbWFOczdZeGVNSk4xeVQ=").append(NL);
                ldif.append("entryUUID: ").append(new UUID(Long.parseLong(entryUUID), 0L).toString()).append(NL);

                if (includePhotos) {
                    byte[] jpg = fetchJPEG(userImage);
                    if (jpg != null) {
                        wrapLine(ldif, "jpegPhoto:: ", Base64.getEncoder().encodeToString(jpg));
                    }
                }

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

    /** Utility method used wrapping an LDIF attribute into lines of at most 80 characters */
    private void wrapLine(StringBuilder ldif, String attr, String value) {
        int len = 80;
        String prefix = attr;
        while (value.length() > 0) {
            ldif.append(prefix);
            String v = value.substring(0, Math.min(value.length(), len - prefix.length()));
            ldif.append(v).append(NL);
            value = value.substring(v.length());
            prefix = " ";
        }
    }

    /** Utility method used for fetching a photo from a URL */
    private byte[] fetchJPEG(String url) {
        if (url != null && url.length() > 0) {
            try (InputStream in = new URL(url).openStream()) {
                return fetchPhoto(in, "jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /** Utility method used for fetching a photo from an input stream */
    private byte[] fetchPhoto(InputStream in, String format) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Is the format specified or not
        if (format == null) {
            IOUtils.copy(in, baos);
        } else {
            BufferedImage img = ImageIO.read(in);
            ImageIO.write(img, format, baos);
        }
        return baos.toByteArray();
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
                String entryUUID = TeamworkDB.getString(rs, "companyId");

                String companyId = companyId(companyName);

                StringBuilder g = groups.get(companyName);
                if (!groups.containsKey(companyName)) {
                    g = new StringBuilder();
                    groups.put(companyName, g);

                    Company company = companyData.get(companyId);

                    g.append("dn: cn=").append(companyName).append(",").append(groupsDN).append(NL);
                    g.append("objectClass: top").append(NL);
                    g.append("objectClass: groupOfUniqueNames").append(NL);
                    g.append("objectclass: maritimeResource").append(NL);
                    g.append("objectclass: maritimeOrganization").append(NL);
                    g.append("cn: ").append(companyName).append(NL);
                    g.append("uid: ").append(companyId).append(NL);
                    g.append("mrn: ").append("urn:mrn:mc:org:").append(companyId.toLowerCase().replace(' ', '_')).append(NL);
                    g.append("entryUUID: ").append(new UUID(Long.parseLong(entryUUID), 0L).toString()).append(NL);

                    if (company != null) {
                        if (company.country.length() == 2) {
                            g.append("c: ").append(company.country).append(NL);
                        }
                        g.append("labeledURI: ").append(company.www).append(NL);
                        g.append("description: ").append(company.description).append(NL);
                        if (includePhotos && company.logo != null) {
                            byte[] img = fetchPhoto(getClass().getResourceAsStream(company.logo), null);
                            if (img != null) {
                                wrapLine(g, "photo:: ", Base64.getEncoder().encodeToString(img));
                            }
                        }
                    }
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

    /** Generates a ID for the company based on the company name */
    private String companyId(String companyName) {
        return companyName.toLowerCase().replace('&', '-').replace(' ', '_');
    }

    /**
     * Used internally to read in data from teh companies.json file
     */
    public static class Company {
        public String uid, name, description, logo, country, www;
    }
}
