/**
 * Cribbage.java
 * snkemp
**/

package cribbage;

import java.util.Stack;

public class Cribbage {

    private Player[] players;

    public Cribbage( Player p1, Player p2 ) {
        players = new Player[]{p1, p2};
    }

    public void run() {
        write("\t\t\t- Cribbage -\n");

        int n = 0; // Keep track of number of games
        
        /* Assume we want to keep playing games */
        while( true ) {

            /* Print new game message */
            String message = "\tNew game: " + n + "\n";
            for( int i = 0; i < players.length; i++ )
                message += "\t\tplayer"+i+": "+players[i].score()+"\n";
            write(message);
           
            /* Deck and who deals */
            Deck deck = new Deck();
            int dealer = n & 1;

            /* While no one has won */
            while( !won() ) {

                /* Reset deck */
                deck.reset();
                deck.shuffle();
                deck.cut();

                /* Deal */
                for( int i = 0; i < 6; i++ )
                    for( Player p : players )
                        p.deal( deck.pop() );

                /* Display Cards for Players */
                for( Player p : players )
                    p.printHand();

                /* Build Crib
                 * unfortunately p1 always goes first
                 * maybe we will implement with threads 
                 */
                Hand crib = new Hand();
                for( Player p : players ) {
                    crib.add( p.getForCrib() );
                    crib.add( p.getForCrib() );
                }

                /* Flip card */
                Card flip = deck.pop();
                if( flip.getFace() == 10 ) { // Nobs
                    players[dealer].add(2);
                    if( players[dealer].won() )
                        break;
                }

                /* Pegging TODO */
                for( Player p : players ) // Start pegging prep
                    p.startPegging();

                int count = 0, pegging = 0, turn = (dealer+1)%2;
                Stack<Card> pile = new Stack<Card>();
                boolean go = false;

                while( count < 8 ) {
                    Card c = players[turn].getForPegging();

                    turn = (turn+1)%2;
                    count ++;
                }

                for( Player p : players ) 
                    p.endPegging();

                /* Non dealer counts */
                int n_dealer = (dealer+1) % 2;
                score( players[n_dealer] );
                if( players[n_dealer].won() )
                    break;

                /* Dealer counts */
                score( players[dealer] );
                if( players[dealer].won() )
                    break;

                /* Dealer counts crib */
                players[dealer].setHand(crib);
                score( players[dealer] );
                if( players[dealer].won() )
                    break;
            }

            // We finished a game
            n++;
            for( Player p : players )
                if( p.won() )
                    p.win();
        }
    }

    /* Score a players hand */
    public void score( Player p ) {
        write( "Hand: " + p.getHand().toString() );
        // TODO
        p.add(19); // LOL
    }

    public int score( Stack<Card> peg ) {

        // TODO
        return 0;
    }

    /* Write message to both players */
    public void write(String message) {
        for( Player p : players )
            p.write(message);
    }


    /* Test for win */
    boolean won() { 
        boolean w = false;
        for( Player p : players )
            w = w || p.won();
        return w;
    }
}
