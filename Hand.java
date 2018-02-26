/**
 * Hand.java
 * snkemp
**/

package cribbage;

import java.util.Vector;

public class Hand {

    private Vector<Card> cards;

    public Hand() {
        cards = new Vector<Card>();
    }

    public void add(Card c) {
        cards.add(c);
    }

    public Card remove(Card c) {
        for( int i = 0; i < cards.size(); i++ )
            if( c.equals(cards.get(i)) )
                return cards.remove(i);

        return null;
    }

    @Override
    public Hand clone() {
        Hand h = new Hand();
        for( int i = 0; i < cards.size(); i++ )
            h.add( cards.get(i).clone() );

        return h;
    }

    public String toString() {
        String str = "[ ";
        for( int i = 0; i < cards.size(); i++ )
            str += cards.get(i).toString() + " ";
        str += "]";
        return str;
    }
}
