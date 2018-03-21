/**
 * Client.java
 * snkemp
**/

package socket;

import java.net.*;
import java.io.*;

public class Client {

    public static void main( String[] args ) {
        try {

            /* Connect to host. Host will handle everything */
            System.out.println("Connecting to host ...");
            Socket host = new Socket("127.0.0.1", Host.PORT );

            /* We need to read */
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader host_in = new BufferedReader(new InputStreamReader(host.getInputStream()));
            PrintWriter host_out = new PrintWriter(host.getOutputStream(), true);

            /* Write out everything from host */
            String line;
            while( (line = host_in.readLine()) != null ) {
                System.out.println(line);
                
                // Requesting input
                if( line.length() > 0 && line.charAt(0) == '>' ) {
                    String response = in.readLine();
                    host_out.println(response);
                }
            }

            host.close();
        }
        catch( Exception e ) {
            e.printStackTrace();
        }
    }
}
