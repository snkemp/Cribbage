/**
 * CribbageHost.java
 * snkemp
**/

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

            /* Get our host and client */
            ServerSocket host = new ServerSocket(PORT);
            Socket client = host.accept(); // Blocking

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
