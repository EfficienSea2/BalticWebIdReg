/* Copyright (c) 2011 Danish Maritime Authority
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.e2.bw.idreg.db2ldif;

import java.sql.*;
import java.util.Date;
import java.util.Objects;

/**
 * Defines the interface to the database
 */
public class TeamworkDB {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    String dbUrl;
    String dbUser;
    String dbPassword;

    /**
     * Constructor
     */
    public TeamworkDB(String dbUrl, String dbUser, String dbPassword) {

        this.dbUrl = Objects.requireNonNull(dbUrl);
        this.dbUser = Objects.requireNonNull(dbUser);
        this.dbPassword = Objects.requireNonNull(dbPassword);

        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            // Should never happen
            e.printStackTrace();
        }
    }

    /**
     * Returns a new connection to the legacy database.
     * Important to close the connection afterwards.
     * @return a new connection to the legacy database.
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                dbUrl,
                dbUser,
                dbPassword);
    }

    public static String getString(ResultSet rs, String key) throws SQLException {
        String val = rs.getString(key);
        return rs.wasNull() ? null : val;
    }

    public static Integer getInt(ResultSet rs, String key) throws SQLException {
        Integer val = rs.getInt(key);
        return rs.wasNull() ? null : val;
    }

    public static Double getDouble(ResultSet rs, String key) throws SQLException {
        Double val = rs.getDouble(key);
        return rs.wasNull() ? null : val;
    }

    public static Date getDate(ResultSet rs, String key) throws SQLException {
        Timestamp val = rs.getTimestamp(key);
        return rs.wasNull() ? null : val;
    }

    public static Boolean getBoolean(ResultSet rs, String key) throws SQLException {
        boolean val = rs.getBoolean(key);
        return rs.wasNull() ? null : val;
    }

}
