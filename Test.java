// Test.java
// snkemp

package test;

import cribbage.*;

import java.util.Stack;

class Test {

    public static void main( String[] args ) {

        Hand h;
        Card f;
        Stack<Card> s;

        /** Score hands **/
        /* 15 */
        h = Hand.fromCommandLine("ac,4d,5h,10s");
        f = Card.fromCommandLine("jc");
        test( h, f, 8, "15" );  
        
        /* Run */
        h = Hand.fromCommandLine("2c,jc,qd,kh");
        f = Card.fromCommandLine("ah");
        test( h, f, 3, "Run" );  
        

        /* Double Run */
        h = Hand.fromCommandLine("2c,jc,qd,kh");
        f = Card.fromCommandLine("jh");
        test( h, f, 8, "DblRun" );

        /* Triple Run */
        h = Hand.fromCommandLine("jc,qd,qh,qc");
        f = Card.fromCommandLine("kh");
        test( h, f, 15, "TplRun" );

        /* Double Double Run */
        h = Hand.fromCommandLine("jc,jd,qd,qh");
        f = Card.fromCommandLine("kh");
        test( h, f, 16, "DDRun" );

        /* Pairs */
        h = Hand.fromCommandLine("ac,ad,10h,10s");
        f = Card.fromCommandLine("10c");
        test( h, f, 8, "Pairs" );  
        
        /* Flush */
        h = Hand.fromCommandLine("ac,3c,6c,7c");
        f = Card.fromCommandLine("jc");
        test( h, f, 5, "Flush" );  
        
        /* Nibs */
        h = Hand.fromCommandLine("ah,2h,9c,jc");
        f = Card.fromCommandLine("8c");
        test( h, f, 1, "Nibs" );  
    
        /** Score Cribs **/
        /* No flush */
        h = Hand.fromCommandLine("ac,6c,10c,jc");
        f = Card.fromCommandLine("kh");
        testCrib( h, f, 0, "No flush" );

        /** Score Pegging **/
        s = new Stack<Card>();
        /* 15 */
        s.clear();
        s.push( Card.fromCommandLine("10c") );
        s.push( Card.fromCommandLine("5h") );
        test( s, 2, "15" );

        /* 31 */
        s.clear();
        s.push( Card.fromCommandLine("10c") );
        s.push( Card.fromCommandLine("5h") );
        s.push( Card.fromCommandLine("jd") );
        s.push( Card.fromCommandLine("6h") );
        test( s, 2, "31" );

        /* Pair */
        s.clear();
        s.push( Card.fromCommandLine("10c") );
        s.push( Card.fromCommandLine("10d") );
        test( s, 2, "Pair" );

        /* Trips */
        s.clear();
        s.push( Card.fromCommandLine("10c") );
        s.push( Card.fromCommandLine("10d") );
        s.push( Card.fromCommandLine("10s") );
        test( s, 6, "Trips" );

        /* Quads */
        s.clear();
        s.push( Card.fromCommandLine("10c") );
        s.push( Card.fromCommandLine("10d") );
        s.push( Card.fromCommandLine("10h") );
        s.push( Card.fromCommandLine("10s") );
        test( s, 12, "Quads" );

        /* Run 3 */
        s.clear();
        s.push( Card.fromCommandLine("as") );
        s.push( Card.fromCommandLine("4c") );
        s.push( Card.fromCommandLine("5h") );
        s.push( Card.fromCommandLine("6s") );
        test( s, 3, "Run 3" );

        /* Run 4 */
        s.clear();
        s.push( Card.fromCommandLine("ac") );
        s.push( Card.fromCommandLine("5h") );
        s.push( Card.fromCommandLine("8s") );
        s.push( Card.fromCommandLine("6c") );
        s.push( Card.fromCommandLine("7s") );
        test( s, 4, "Run 4" );

        /* Run 5 */
        s.clear();
        s.push( Card.fromCommandLine("ah") );
        s.push( Card.fromCommandLine("5h") );
        s.push( Card.fromCommandLine("4c") );
        s.push( Card.fromCommandLine("2s") );
        s.push( Card.fromCommandLine("3d") );
        test( s, 7, "Run 5 & 15" );
    }
    
    public static void testCrib( Hand hand, Card flip, int score, String error ) {
        int act_score = Cribbage.crib( hand, flip );
        if( act_score != score )
            System.out.println( error + ":\t" + act_score + " != " + score );
    }

    public static void test( Hand hand, Card flip, int score, String error ) {
        int act_score = Cribbage.score( hand, flip );
        if( act_score != score )
            System.out.println( error + ":\t" + act_score + " != " + score );
    }

    public static void test( Stack<Card> stack, int score, String error ) {
        int act_score = Cribbage.score( stack );
        if( act_score != score )
            System.out.println( error + ":\t" + act_score + " != " + score );
    }
}
