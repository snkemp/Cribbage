/**
 * Deck.java
 * snkemp
**/

package cribbage;

import java.util.Vector;

public class Deck {

    /* A Deck of cards */
    private Vector<Card> cards;


    /* Constructors */
    public Deck() { this.reset(); }
    public void reset() {
        cards.clear();
        for( int suit = 0; suit < 4; suit++ )
            for( int face = 0; face < 13; face++ )
                cards.add( new Card(face, suit) );
    }
    

    /* Shufflers */
    public void shuffle() {
        Vector<Card> deck = new Vector<Card>(this.cards.capacity());
        while( !this.cards.isEmpty() ) {
            int rem = (int) Math.floor(Math.random() * this.cards.size());
            Card card = this.cards.remove(rem);
            deck.add(card);
        }
        this.cards = deck;
    }

    public void cut() {
        Vector<Card> deck = new Vector<Card>(this.cards.capacity());
        int mid = (int) Math.floor(Math.random() * this.cards.size());
        
        for( int i = mid; i < this.cards.size(); i++ )
            deck.add( this.cards.get(i) );
        for( int i = 0; i < mid; i++ )
            deck.add( this.cards.get(i) );

        this.cards = deck;
    }

    /* Deal */
    public Card pop() {
        return cards.remove(0);
    }
}
