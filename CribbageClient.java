/**
 * CribbageClient.java
 * snkemp
**/

package socket;

import java.net.*;
import java.io.*;

public class CribbageClient {

    public static void main( String[] args ) {
        try {

            /* Connect to host. Host will handle everything */
            System.out.println("Connecting to host ...");
            Socket host = new Socket("127.0.0.1", CribbageHost.PORT );
        }
        catch( Exception e ) {
            e.printStackTrace();
        }
    }
}
