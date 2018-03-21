/**
 * Player.java
 * snkemp
**/

package cribbage;

import java.io.*;
import java.util.Scanner;

import java.util.Stack;

public class Player {

    private String name;

    private BufferedReader in;
    private PrintWriter out;

    private int score, wins;

    private Hand hand, peg;

    public Player( String name, BufferedReader in, PrintWriter out ) {

        this.name = name;

        this.in = in;
        this.out = out;

        this.score = 0;
        this.wins = 0;

        hand = new Hand();
    }

    /* UI */
    public String getName() { return this.name; }
    public String getScoring() { return getName() + ":\t " + score; }
    public String getGaming() { return getName() + ":\t" + wins; }

    /* Hand IO */
    public Hand getHand() { return this.hand; }
    public void setHand(Hand h ) { this.hand = h; }

    public void startPegging() { this.peg = hand.clone(); }
    public void endPegging() { this.peg = null; }

    public void deal( Card c ) { hand.add(c); }
    public void empty() { this.hand.clear(); }

    public Card getForCrib(String dealer) {
        write("> Select a card for the " + dealer + "'s crib: " + hand.toString() );
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
            return getForCrib(dealer);
        }

        return crib;
    }

    public Card getForPegging( Stack<Card> pile ) {
        int pegging = 0;
        String msg = "Pegging: ";

        // Print the pile
        for( int i = 0; i < pile.size(); i++ ) {
            msg += pile.get(i) + "  ";
            pegging += pile.get(i).getFaceValue();
        }
        write( msg + ": " + pegging );

        // Peg
        write("> Select a card for pegging: " + peg );
        String submission = "";

        try {
            submission = read();
        } catch( Exception err ) {
            err.printStackTrace(out);
        }

        if( submission.equals("") || submission.equals("g") || submission.equals("go") )
            return null;

        Card card = Card.fromCommandLine(submission);
        Card pegg = peg.remove(card);
        if( pegg == null ) {
            write("Illegal card");
            return getForPegging(pile);
        }

        if( pegg.getFaceValue() + pegging > 31 ) {
            peg.add(pegg);
            write("Illegal card");
            return getForPegging(pile);
        }

        return pegg;
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
