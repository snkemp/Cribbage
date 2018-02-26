/**
 * CribbageHost.java
 * snkemp
**/

package socket; 

import java.net.*;
import java.io.*;

import cribbage.*;

public class CribbageHost {

    public static int PORT = 6969;
    
    public static void main( String[] args ) {
        try{

            /* Create server at given port (default to 6969 lol) */
            if( args.length == 1 )
                PORT = Integer.parseInt(args[0]);

            /* Create Server */
            System.out.println("Loading Server ...");
            ServerSocket host = new ServerSocket(PORT);

            /* Wait for client to connect */
            System.out.println("Waiting for client to connect ...");
            Socket client = host.accept(); // Blocking
            System.out.println("Client has connected.");

            /* Get our players */
            Player p1 = new Player( System.in, System.out );
            Player p2 = new Player( client.getInputStream(), client.getOutputStream() );

            /* Start a new cribbage game */
            Cribbage game = new Cribbage(p1, p2);
            game.run();

            /* After game(s) close the client */
            client.close();
        } 
        catch( Exception e ) {
            e.printStackTrace();
        }
    }
}
