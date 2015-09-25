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
package net.e2.bw.idreg.ldap;

import java.util.Arrays;

/**
 * Bootstrap class used for running ldapserver or ldapsearch
 */
public class Main {

    /**
     * Main method
     * @param args runtime arguments
     */
    public static void main(String[] args) throws Exception {

        String command = args.length > 0 ? args[0] : "";
        String[] cmdArgs = args.length > 0 ? Arrays.copyOfRange(args, 1, args.length) : null;
        switch (command.toLowerCase()) {

            case "ldapserver":
                LdapServer.main(cmdArgs);
                break;

            case "ldapsearch":
                LdapSearch.main(cmdArgs);
                break;

            default:
                System.out.println(
                    "Usage: java -jar bw-idreg-ldap.jar ldapserver params\n" +
                    "       java -jar bw-idreg-ldap.jar ldapsearch params");
        }


    }

}
