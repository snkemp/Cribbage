/**
 * Player.java
 * snkemp
**/

package cribbage;

import java.io.*;
import java.util.Scanner;

public class Player {

    private BufferedReader in;
    private PrintWriter out;

    private int score, wins;

    private Hand hand, peg;

    public Player( BufferedReader in, PrintWriter out ) {
        this.in = in;
        this.out = out;

        this.score = 0;
        this.wins = 0;

        hand = new Hand();
    }

    /* Hand IO */
    public Hand getHand() { return this.hand; }
    public void setHand(Hand h ) { this.hand = h; }

    public void startPegging() { this.peg = hand.clone(); }
    public void endPegging() { this.peg = null; }

    public void deal( Card c ) { hand.add(c); }

    public Card getForCrib() {
        write("> Select a card for the crib: " + hand.toString() );
        String submission = "";
        
        try {
            submission = read();
        } catch( Exception err ) {
            err.printStackTrace(out);
        }

        Card card = Card.fromCommandLine(submission);
        Card crib = hand.remove(card);
        
        if( crib == null ) {
            write("Illegal card");
            return getForCrib();
        }

        return crib;
    }

    public Card getForPegging() {

        return null;
    }

    /* Points */
    public void add( int points ) { this.score += points; }
    public void win() { this.wins++; }

    public boolean won() { return this.score >= 121; }
    public int score() { return this.score; }
    public int wins() { return this.wins; }
    

    /* IO */
    public void write( String message ) {
        out.println(message);
    }

    public String read() throws Exception {
        return in.readLine();
    }

    public void printHand() {
        write(hand.toString());
    }

    public void printPeg() {
        write(peg.toString());
    }
}
