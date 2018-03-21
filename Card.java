/**
 * Card.java
 * snkemp
**/

package cribbage;

public class Card {

    public static final int CLUBS = 0, DIAMONDS = 1, HEARTS = 2, SPADES = 3;

    /* Each card has a face value and suit */
    private int face, suit;


    /* Constructors */
    public Card() {
        this(0,0);
    }

    public Card( int face, int suit ) {
        this.face = face;
        this.suit = suit;
    }


    /* Getters */
    public int getFace() { return this.face; }
    public int getSuit() { return this.suit; }

    public int getFaceValue() { return Math.min(getFace() + 1, 10); }

    /* Determine */
    public boolean equals(Card c) {
        return face == c.getFace() && suit == c.getSuit();
    }

    /* Helpers */
    public int priority() {
        return 4*face + suit;
    }

    public void swap( Card c ) {
        Card d = clone();
        set(c);
        c.set(d);
    }

    public void set( Card c ) {
        face = c.getFace();
        suit = c.getSuit();
    }

    /* Format */
    public String toString() {
        return Card.faceChar(this.face) +""+ Card.suitChar(this.suit);
    }

    public String toLongString() {
        return Card.faceString(this.face) + " of " + Card.suitString(this.suit);
    }

    public static String faceChar(int face) {
        String[] faces = new String[] { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };
        return faces[face];
    }

    public static String suitChar(int suit) {
        String[] suits = new String[] { "\u2664", "\u2661", "\u2662", "\u2667" };
        return suits[suit];
    }

    public static String faceString(int face) {
        String[] faces = new String[] { "Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King" };
        return faces[face];
    }

    public static String suitString(int suit) {
        String[] suits = new String[] { "Spades", "Hearts", "Diamond", "Clubs" };
        return suits[suit];
    }

    @Override
    public Card clone() {
        return new Card( face, suit );
    }

    public static Card fromCommandLine(String card) {
        card.trim();

        if( card.length() != 2 && card.length() != 3 )
            return null;

        String sFace = card.substring(0, card.length()-1);
        char sSuit = card.charAt(card.length() -1);

        if( sFace.equals("a") || sFace.equals("A") )
            sFace = "1";
        else
        if( sFace.equals("x") || sFace.equals("X") )
            sFace = "10";
        if( sFace.equals("j") || sFace.equals("J") )
            sFace = "11";
        else
        if( sFace.equals("q") || sFace.equals("q") )
            sFace = "12";
        else
        if( sFace.equals("k") || sFace.equals("K") )
            sFace = "13";
        
        int face = Integer.parseInt(sFace) - 1;
        int suit = -1;
        switch( sSuit ) {
            case 's': case 'S':
                suit = SPADES;
                break;

            case 'h': case 'H':
                suit = HEARTS;
                break;

            case 'd': case 'D':
                suit = DIAMONDS;
                break;

            case 'c': case 'C':
                suit = CLUBS;
                break;

            default: return null;
        }
        return new Card(face, suit);
    }
}
