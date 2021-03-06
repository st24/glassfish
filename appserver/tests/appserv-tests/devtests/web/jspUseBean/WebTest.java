/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

import java.lang.*;
import java.io.*;
import java.net.*;

import com.sun.ejte.ccl.reporter.*;

public class WebTest {
    
    private static final String TEST_NAME = "jsp-use-bean";

    private static boolean pass = false;
    
    static SimpleReporterAdapter stat=
        new SimpleReporterAdapter("appserv-tests");

    public static void main(String args[])
    {

        // The stat reporter writes out the test info and results
        // into the top-level quicklook directory during a run.
      
        stat.addDescription("Test useBean optimization behavior");

        String host = args[0];
        String portS = args[1];
        String contextRoot = args[2];
        int port = new Integer(portS).intValue();
        
        try {
            goGet(host, port, contextRoot + "/test.jsp" );
            stat.addStatus(TEST_NAME, pass? stat.PASS: stat.FAIL);
        } catch (Throwable t) {
            System.out.println(t.getMessage());
            stat.addStatus(TEST_NAME, stat.FAIL);
        }

        stat.printSummary("UseBean optimization");
    }

    private static void goGet(String host, int port, String contextPath)
         throws Exception
    {
        Socket s = null;
        OutputStream os = null;
        InputStream is = null;
        BufferedReader bis = null;
        try {
            s = new Socket(host, port);
            os = s.getOutputStream();

            System.out.println("GET " + contextPath + " HTTP/1.0");
            os.write(("GET " + contextPath + " HTTP/1.0\n").getBytes());
            os.write("\n".getBytes());

            is = s.getInputStream();
            bis = new BufferedReader(new InputStreamReader(is));
            String line = null;

            while ((line = bis.readLine()) != null) {
                if (line.trim().length() > 0)
                    System.out.println(line);
                int index = line.indexOf("PASS");
                if (index >= 0) {
                    pass = true;
                } 
            }
        } catch( Exception ex){
            ex.printStackTrace();   
            throw new Exception("Test UNPREDICTED-FAILURE");
        } finally {
            try {
                if (os != null) os.close();
            } catch (IOException ex) {}
            try {
                if (s != null) s.close();
            } catch (IOException ex) {}
            try {
                if (is != null) is.close();
            } catch (IOException ex) {}
            try {
                if (bis != null) bis.close();
            } catch (IOException ex) {}
        }
    }
}
