/*
 * Copyright (c) 15/12/2020 Chris Jackson (c-jack)
 * adventofcode.Day15
 */

import static constants.Constants.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exception.AnswerNotAvailableException;
import utils.AOCUtils;


/**
 * Advent of Code 2020
 * Day 15
 * <p>
 * --- Day 15: Rambunctious Recitation ---
 * You catch the airport shuttle and try to book a new flight to your vacation island. Due to the storm, all direct
 * flights have been cancelled, but a route is available to get around the storm. You take it.
 * <p>
 * While you wait for your flight, you decide to check in with the Elves back at the North Pole. They're playing a
 * memory game and are ever so excited to explain the rules!
 * <p>
 * In this game, the players take turns saying numbers. They begin by taking turns reading from a list of starting
 * numbers (your puzzle input). Then, each turn consists of considering the most recently spoken number:
 * <p>
 * If that was the first time the number has been spoken, the current player says 0.
 * Otherwise, the number had been spoken before; the current player announces how many turns apart the number is from
 * when it was previously spoken.
 * So, after the starting numbers, each turn results in that player speaking aloud either 0 (if the last number is
 * new) or an age (if the last number is a repeat).
 * <p>
 * For example, suppose the starting numbers are 0,3,6:
 * <p>
 * Turn 1: The 1st number spoken is a starting number, 0.
 * Turn 2: The 2nd number spoken is a starting number, 3.
 * Turn 3: The 3rd number spoken is a starting number, 6.
 * Turn 4: Now, consider the last number spoken, 6. Since that was the first time the number had been spoken, the 4th
 * number spoken is 0.
 * Turn 5: Next, again consider the last number spoken, 0. Since it had been spoken before, the next number to speak
 * is the difference between the turn number when it was last spoken (the previous turn, 4) and the turn number of
 * the time it was most recently spoken before then (turn 1). Thus, the 5th number spoken is 4 - 1, 3.
 * Turn 6: The last number spoken, 3 had also been spoken before, most recently on turns 5 and 2. So, the 6th number
 * spoken is 5 - 2, 3.
 * Turn 7: Since 3 was just spoken twice in a row, and the last two turns are 1 turn apart, the 7th number spoken is 1.
 * Turn 8: Since 1 is new, the 8th number spoken is 0.
 * Turn 9: 0 was last spoken on turns 8 and 4, so the 9th number spoken is the difference between them, 4.
 * Turn 10: 4 is new, so the 10th number spoken is 0.
 * (The game ends when the Elves get sick of playing or dinner is ready, whichever comes first.)
 * <p>
 * Their question for you is: what will be the 2020th number spoken? In the example above, the 2020th number spoken
 * will be 436.
 * <p>
 * Here are a few more examples:
 * <p>
 * Given the starting numbers 1,3,2, the 2020th number spoken is 1.
 * Given the starting numbers 2,1,3, the 2020th number spoken is 10.
 * Given the starting numbers 1,2,3, the 2020th number spoken is 27.
 * Given the starting numbers 2,3,1, the 2020th number spoken is 78.
 * Given the starting numbers 3,2,1, the 2020th number spoken is 438.
 * Given the starting numbers 3,1,2, the 2020th number spoken is 1836.
 *
 * @author chris.jackson
 */
public class Day15
{
    /**
     * Constructor
     */
    public Day15() throws AnswerNotAvailableException
    {
        // Check the logic with the example before calculating answers
        testLogic();

        System.out.println( THE_ANSWER_IS_PT1 + part1( getData() ) );
        System.out.println( THE_ANSWER_IS_PT2 + part2( getData() ) );
    }

    /**
     * --- Part One ---
     * Given your starting numbers, what will be the 2020th number spoken?
     * <p>
     * Answer: 253
     *
     * @param startingNumbers the startingNumbers to process for the question
     */
    private long part1( final List<String> startingNumbers )
    {
        return getSpokenNumberOnTurn( new MemoryGame( startingNumbers ), 2020 );
    }

    /**
     * --- Part Two ---
     * Impressed, the Elves issue you a challenge: determine the 30000000th number spoken. For example, given the
     * same starting numbers as above:
     * <p>
     * Given 0,3,6, the 30000000th number spoken is 175594.
     * Given 1,3,2, the 30000000th number spoken is 2578.
     * Given 2,1,3, the 30000000th number spoken is 3544142.
     * Given 1,2,3, the 30000000th number spoken is 261214.
     * Given 2,3,1, the 30000000th number spoken is 6895259.
     * Given 3,2,1, the 30000000th number spoken is 18.
     * Given 3,1,2, the 30000000th number spoken is 362.
     * Given your starting numbers, what will be the 30000000th number spoken?
     * <p>
     * Answer: 13710
     *
     * @param startingNumbers the startingNumbers to process for the question
     */
    private long part2( final List<String> startingNumbers )
    {
        return getSpokenNumberOnTurn( new MemoryGame( startingNumbers ), 30000000 );
    }


    /**
     * Returns the number spoken at the given turn
     *
     * @param memoryGame   the memory game to play
     * @param turnToStopOn the turn to stop on
     * @return the number spoken on the final turn
     */
    private long getSpokenNumberOnTurn( final MemoryGame memoryGame, final int turnToStopOn )
    {
        long number = 0;
        for ( int i = 1; i <= turnToStopOn; i++ )
        {
            number = memoryGame.takeTurn( i );
        }

        return number;
    }

    /**
     * Get the data for the question
     *
     * @return string list of the data
     */
    private List<String> getData()
    {
        final List<String> data = AOCUtils.getData( getClass().getName() );

        // There's only one line in the data this time, and it's comma separated
        return Arrays.asList( data.get( 0 ).split( COMMA ) );
    }

    /**
     * A Class to define the rules of the memory game and methods to play it
     */
    static class MemoryGame
    {
        // The numbers to start from
        final List<String> startingNumbers;

        // A Map containing each distinct number (key) and the last turn it was called on (value)
        final Map<Long, Long> numberLastCalled;

        long lastCalledNumber = 0;

        /**
         * @param startingNumbers the numbers to start the game
         */
        public MemoryGame( final List<String> startingNumbers )
        {
            this.startingNumbers = startingNumbers;
            numberLastCalled = new HashMap<>();
        }

        /**
         * Takes the provided turn
         * @param turn the turn number to take
         * @return the number spoken at the end of the turn
         */
        public Long takeTurn( final int turn )
        {
            // This is the number that will be spoken (returned)
            final long spokenNumber;

            // The number of the previous turn
            final int lastTurn = turn - 1;

            // First iteratino of the starting numbers return the previous number
            if ( turn <= startingNumbers.size() )
            {
                lastCalledNumber = Integer.parseInt( startingNumbers.get( lastTurn ) );
                spokenNumber = lastCalledNumber;

                // If its the last of the starting numbers, don't add it to the 'last called' list yet
                if ( turn < startingNumbers.size() )
                {
                    numberLastCalled.put( spokenNumber, ( long ) turn );
                }
            }
            else
            {
                // The first time a number (lastCalled) has been spoken
                if ( !numberLastCalled.containsKey( lastCalledNumber ) )
                {
                    // Add the last number in now it's the first time we've heard it
                    numberLastCalled.put( lastCalledNumber, ( long ) lastTurn );

                    // The spoken number should be 0
                    spokenNumber = 0;
                }
                else
                {
                    // Get the last turn minus the previous time the number was called
                    spokenNumber = lastTurn - numberLastCalled.get( lastCalledNumber );

                    /*
                     * Add the last number in now we've figured out the calculation.
                     * This is a bit of a trick to avoid having to calculate the time before last
                     */
                    numberLastCalled.put( lastCalledNumber, ( long ) lastTurn );
                }

                // Update this number as the last called number
                lastCalledNumber = spokenNumber;
            }
            return spokenNumber;
        }
    }

    /* *************** *
     *     TESTS       *
     * *************** */

    /**
     * Checks the logic against the examples in the question.
     */
    private void testLogic()
    {
        // Create out example test data
        final List<String> testData = Arrays.asList( "0", "3", "6" );
        final List<String> testData2 = Arrays.asList( "1", "3", "2" );
        final List<String> testData3 = Arrays.asList( "2", "1", "3" );
        final List<String> testData4 = Arrays.asList( "1", "2", "3" );
        final List<String> testData5 = Arrays.asList( "2", "3", "1" );
        final List<String> testData6 = Arrays.asList( "3", "2", "1" );
        final List<String> testData7 = Arrays.asList( "3", "1", "2" );

        // Run the tests for Part 1
        assert part1( testData ) == 436 : PART_1_TEST_FAILED;
        assert part1( testData2 ) == 1 : PART_1_TEST_FAILED;
        assert part1( testData3 ) == 10 : PART_1_TEST_FAILED;
        assert part1( testData4 ) == 27 : PART_1_TEST_FAILED;
        assert part1( testData5 ) == 78 : PART_1_TEST_FAILED;
        assert part1( testData6 ) == 438 : PART_1_TEST_FAILED;
        assert part1( testData7 ) == 1836 : PART_1_TEST_FAILED;

        // Run the tests for Part 2
        assert part2( testData ) == 175594 : PART_2_TEST_FAILED;
        assert part2( testData2 ) == 2578 : PART_2_TEST_FAILED;
        assert part2( testData3 ) == 3544142 : PART_2_TEST_FAILED;
        assert part2( testData4 ) == 261214 : PART_2_TEST_FAILED;
        assert part2( testData5 ) == 6895259 : PART_2_TEST_FAILED;
        assert part2( testData6 ) == 18 : PART_2_TEST_FAILED;
        assert part2( testData7 ) == 362 : PART_2_TEST_FAILED;
    }
}
