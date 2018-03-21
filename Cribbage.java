/**
 * Cribbage.java
 * snkemp
**/

package cribbage;

import java.util.Stack;
import java.util.Queue;
import java.util.LinkedList;

public class Cribbage {

    private Player[] players;

    public Cribbage( Player p1, Player p2 ) {
        players = new Player[]{p1, p2};
    }

    public void run() {
        write("\t\t\t- Cribbage -\n");

        int n = 0; // Keep track of number of games
        
        /* Assume we want to keep playing games */
        playing:
        while( true ) {

            /* Print new game message */
            String message = "";
            for( Player p : players )
                message += "\t\t" + p.getGaming();
            write(message);
           
            /* Deck and who deals */
            Deck deck = new Deck();
            int dealer = n & 1;

            /* While no one has won */
            game:
            while( !won() ) {

                /* Reset deck */
                deck.reset();
                deck.shuffle();
                deck.cut();

                /* Deal */

                // Clear hands first
                for( Player p : players )
                    p.empty();

                for( int i = 0; i < 6; i++ )
                    for( Player p : players )
                        p.deal( deck.pop() );

                /* Build Crib
                 * unfortunately p1 always goes first
                 * maybe we will implement with threads 
                 */
                Hand crib = new Hand();
                for( Player p : players ) {
                    crib.add( p.getForCrib(players[dealer].getName()) );
                    crib.add( p.getForCrib(players[dealer].getName()) );
                }

                /* Flip card */
                Card flip = deck.pop();
                if( flip.getFace() == 10 ) { // Nobs
                    players[dealer].add(2);
                    if( players[dealer].won() )
                        break game;
                }

                /* Pegging TODO */
                for( Player p : players ) // Start pegging prep
                    p.startPegging();

                int count = 0, turn = dealer^1;
                Stack<Card> pile = new Stack<Card>();
                boolean go = false;

                // While we haven't played 8 cards
                while( count < 8 ) {

                    // Get the next card
                    Card c = players[turn].getForPegging(pile);

                    // Says go
                    if( c == null ) {

                        // Go was already said, goto next series
                        if( go ) {
                            go = false;
                            pile.clear();
                        }
                        else {

                            // Change turns, player gets a point, check for win
                            turn ^= 1;
                            players[turn].add(1);
                            if( players[turn].won() )
                                break game;

                            // Go was said
                            go = true;
                        }
                    }
                    else {

                        // Add card to pile
                        pile.add(c);
                        count++;

                        // Score the pile
                        int v = score( pile );
                        players[turn].add(v);
                        if( players[turn].won() )
                            break game;

                        // If not go, switch turn
                        if( !go )
                            turn ^= 1;
                    }
                }

                /* End pegging */
                for( Player p : players ) 
                    p.endPegging();

                int score;
                /* Non dealer counts */

                score = score( players[dealer^1].getHand(), flip );
                write( players[dealer^1].getName() + ":\t" + players[dealer^1].getHand() + " " + flip + " >> " + score );
                players[dealer^1].add(score);
                if( players[dealer ^ 1].won() )
                    break game;

                /* Dealer counts */
                score = score(players[dealer].getHand(), flip);
                write( players[dealer].getName() + ":\t" + players[dealer].getHand() + " " + flip + " >> " + score );
                players[dealer].add(score);
                if( players[dealer].won() )
                    break game;

                /* Dealer counts crib */
                players[dealer].setHand(crib);
                score = crib(players[dealer].getHand(), flip);
                write( players[dealer].getName() + ":\t" + players[dealer].getHand() + " " + flip + " >> " + score );
                players[dealer].add(score);
                if( players[dealer].won() )
                    break game;

                /* Print scores */
                String scores = "";
                for( Player p : players )
                    scores += p.getScoring() + "\t";
                write(scores);

                // Dealer changes
                dealer ^= 1;
            }

            // We finished a game
            n++;
            for( Player p : players )
                if( p.won() )
                    p.win();
        }
    }

    /* Score a hand */
    public static int score( Hand h, Card flip ) {
        int score = 0;
        
        /* Flush */
        int flush = 15; // 1 | 2 | 4 | 8
        for( int i = 0; i < h.size(); i++ )
            flush &= 1 << h.get(i).getSuit();

        if( flush > 0 )
            score += 4;
        if( (flush & (1<<flip.getSuit())) > 0 )
            score ++;

        /* Nibs */
        for( int i = 0; i < h.size(); i++ ) {
            if( h.get(i).getFace() == 10 && h.get(i).getSuit() == flip.getSuit() ) {
                score += 1;
                break;
            }
        }
        
        // Helps with 15, pair, runs
        h.add(flip);

        /* 15s */
        int sum = 0;
        for( int i = 0; i < h.size(); i++ )
            sum += h.get(i).getFaceValue();

        // Base case
        if( sum == 15 )
            score += 2;

        // Handle removals (1,2,3 cards. 4,5 removals will never produce 15)
        else if( sum > 15 ) {

            for( int i = 0; i < h.size(); i++ ) {

                if( sum - h.get(i).getFaceValue() == 15 )
                    score += 2;

                for( int j = i+1; j < h.size(); j++ ) {
                    if( sum - h.get(i).getFaceValue() - h.get(j).getFaceValue() == 15 )
                        score += 2;

                    for( int k = j+1; k < h.size(); k++ ) {
                        if( sum - h.get(i).getFaceValue() - h.get(j).getFaceValue() - h.get(k).getFaceValue() == 15 )
                            score += 2;
                    }
                }
            }
        }

        /* Pairs */
        for( int i = 0; i < h.size() -1; i++ )
            for( int j = i+1; j < h.size(); j++ )
                if( h.get(i).getFace() == h.get(j).getFace() )
                    score += 2;

        /* Runs */
        int i = 0;
        while( i < h.size()-2 ) {
            int k = i;
            int[] cards = new int[13];
            cards[ h.get(k).getFace() ]++;

            // While we have cards to eval
            while( k+1 < h.size() ) {

                // If 1 or less away
                if( h.get(k+1).getFace() - h.get(k).getFace() <= 1 )
                    cards[ h.get(k+1).getFace() ]++;
                else
                    break;

                k++;
            }

            // Count unique cards (length of run)
            int unique = 0;
            for( int c : cards )
                if( c > 0 )
                    unique++;

            // If we have 3 or more unique cards
            if( unique >= 3 ) {

                // Get faces of start and end
                int start = h.get(i).getFace(),
                    end = h.get(k).getFace();

                int multiplier = 1;
                for( int c : cards )
                    if( c > 0 )
                        multiplier *= c;

                score += unique * multiplier;
            }

            i = k+1;
        }

        return score;
    }

    /* Score a crib */
    public static int crib( Hand h, Card flip ) {
        int score = 0;

        /* Nibs */
        for( int i = 0; i < h.size(); i++ ) {
            if( h.get(i).getFace() == 10 && h.get(i).getSuit() == flip.getSuit() ) {
                score += 1;
                break;
            }
        }
        
        // Helps with 15, pair, runs; flush requires all 5
        h.add(flip);

        /* Flush */
        int flush = 15; // 1 | 2 | 4 | 8
        for( int i = 0; i < h.size(); i++ )
            flush &= 1 << h.get(i).getSuit();

        if( flush > 0 )
            score += 5;

        /* 15s */
        int sum = 0;
        for( int i = 0; i < h.size(); i++ )
            sum += h.get(i).getFaceValue();

        // Base case
        if( sum == 15 )
            score += 2;

        // Remove some cards
        else if( sum > 15 ) {
            for( int i = 0; i < h.size(); i++ ) {
                if( sum - h.get(i).getFaceValue() == 15 )
                    score += 2;
    
                for( int j = i+1; j < h.size(); j++ ) {
                    if( sum - h.get(i).getFaceValue() - h.get(j).getFaceValue() == 15 )
                        score += 2;
    
                    for( int k = j+1; k < h.size(); k++ ) {
                        if( sum - h.get(i).getFaceValue() - h.get(j).getFaceValue() - h.get(k).getFaceValue() == 15 )
                            score += 2;
                    }
                }
            }
        }

        /* Pairs */
        for( int i = 0; i < h.size() -1; i++ )
            for( int j = i+1; j < h.size(); j++ )
                if( h.get(i).getFace() == h.get(j).getFace() )
                    score += 2;

         /* Runs */
        int i = 0;
        while( i < h.size()-2 ) {
            int k = i;
            int[] cards = new int[13];
            cards[ h.get(k).getFace() ]++;

            // While we have cards to eval
            while( k+1 < h.size() ) {

                // If 1 or less away
                if( h.get(k+1).getFace() - h.get(k).getFace() <= 1 )
                    cards[ h.get(k+1).getFace() ]++;
                else
                    break;

                k++;
            }

            // Count unique cards (length of run)
            int unique = 0;
            for( int c : cards )
                if( c > 0 )
                    unique++;

            // If we have 3 or more unique cards
            if( unique >= 3 ) {

                // Get faces of start and end
                int start = h.get(i).getFace(),
                    end = h.get(k).getFace();

                int multiplier = 1;
                for( int c : cards )
                    if( c > 0 )
                        multiplier *= c;

                score += unique * multiplier;
            }

            i = k+1;
        }

        return score;
    }

    public static int score( Stack<Card> peg ) {
        int score = 0;

        /* 15, 31s */
        int count = 0;
        for( int i = 0; i < peg.size(); i++ )
            count += peg.get(i).getFaceValue();

        if( count == 15 || count == 31 )
            score += 2;

        /* Runs */
        int min = peg.peek().getFace(),
            max = min,
            num = 1,
            potential = 0;

        int[] played = new int[13];

        for( int i = 0; i < peg.size(); i++ ) {
            int face = peg.get(peg.size()-i-1).getFace();

            // Find the range
            if( face < min )
                min = face;
            if( face > max )
                max = face;

            // Double runs stop everything
            played[ face ]++;
            if( played[ face ] > 1 )
                break;

            // If we have a range equal to num of cards, we score that many points
            if( max - min == i )
                potential = i;

        }

        // Score potential+1 (0-index)
        if( potential >= 2 )
            score += potential+1;
        

        /* Pairs. Trips. Quads */
        int pairs = 1;
        while( pairs < peg.size() && peg.peek().getFace() == peg.get(peg.size()-1-pairs).getFace() )
            pairs++;
        
        // 2 => 1 => 1*0 = 2
        // 2 2 => 2 => 2*1 = 2
        // 2 2 2 => 3 => 3*2 = 6
        // 2 2 2 2 => 4 => 4*3 = 12
        score += pairs * (pairs-1);

        return score;
    }

    /* Write message to both players */
    public void write(String message) {
        for( Player p : players )
            p.write(message);
    }


    /* Test for win */
    boolean won() { 
        for( Player p : players )
            if( p.won() )
                return true;

        return false;
    }
}
