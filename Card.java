/**
 * Card.java
 * snkemp
**/

package cribbage;

public class Card {

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

    /* Determine */
    public boolean equals(Card c) {
        return face == c.getFace() && suit == c.getSuit();
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
        int f = Integer.parseInt(""+card.charAt(0));
        int s = -1;
        switch( card.charAt(1) ) {
            case 'S':
            case 's':
                s = 0;
                break;

            case 'H':
            case 'h':
                s = 1;
                break;

            case 'D':
            case 'd':
                s = 2;
                break;

            case 'C':
            case 'c':
                s = 3;
                break;

            default:
                return null;
        }

        return new Card(f, s);
    }
}
