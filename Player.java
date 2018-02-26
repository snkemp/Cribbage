/**
 * Player.java
 * snkemp
**/

package cribbage;

import java.io.*;

public class Player {

    private InputStream in;
    private OutputStream out;

    private int score, wins;

    private Hand hand, peg;

    public Player( InputStream in, OutputStream out ) {
        this.in = in;
        this.out = out;

        this.score = 0;
        this.wins = 0;
    }

    /* Hand IO */
    public Hand getHand() { return this.hand; }
    public void setHand(Hand h ) { this.hand = h; }

    public void startPegging() { this.peg = hand.clone(); }
    public void endPegging() { this.peg = null; }

    public void deal( Card c ) { hand.add(c); }

    public Card getForCrib() {
        write("Select a card for the crib: " + hand.toString() );
        String submission = read();

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
        PrintWriter o = new PrintWriter(out);
        o.println(message);
        o.flush();
    }

    public String read() {
        try {
            BufferedReader i = new BufferedReader( new InputStreamReader(in) );
            return i.readLine();
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void printHand() {
        write(hand.toString());
    }

    public void printPeg() {
        write(peg.toString());
    }
}
