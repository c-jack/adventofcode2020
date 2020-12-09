/*
 * Copyright (c) 9/12/2020 Chris Jackson (c-jack)
 * adventofcode.Day9
 */

import static constants.Constants.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import exception.AnswerNotAvailableException;
import utils.AOCUtils;


/**
 * Advent of Code 2020
 * Day 9
 * <p>
 * --- Day 9: Encoding Error ---
 * With your neighbor happily enjoying their video game, you turn your attention to an open data port on the little
 * screen in the seat in front of you.
 * <p>
 * Though the port is non-standard, you manage to connect it to your computer through the clever use of several
 * paperclips. Upon connection, the port outputs a series of numbers (your puzzle input).
 * <p>
 * The data appears to be encrypted with the eXchange-Masking Addition System (XMAS) which, conveniently for you, is
 * an old cypher with an important weakness.
 * <p>
 * XMAS starts by transmitting a preamble of 25 numbers. After that, each number you receive should be the sum of any
 * two of the 25 immediately previous numbers. The two numbers will have different values, and there might be more
 * than one such pair.
 * <p>
 * For example, suppose your preamble consists of the numbers 1 through 25 in a random order. To be valid, the next
 * number must be the sum of two of those numbers:
 * <p>
 * 26 would be a valid next number, as it could be 1 plus 25 (or many other pairs, like 2 and 24).
 * 49 would be a valid next number, as it is the sum of 24 and 25.
 * 100 would not be valid; no two of the previous 25 numbers sum to 100.
 * 50 would also not be valid; although 25 appears in the previous 25 numbers, the two numbers in the pair must be
 * different.
 * Suppose the 26th number is 45, and the first number (no longer an option, as it is more than 25 numbers ago) was
 * 20. Now, for the next number to be valid, there needs to be some pair of numbers among 1-19, 21-25, or 45 that add
 * up to it:
 * <p>
 * 26 would still be a valid next number, as 1 and 25 are still within the previous 25 numbers.
 * 65 would not be valid, as no two of the available numbers sum to it.
 * 64 and 66 would both be valid, as they are the result of 19+45 and 21+45 respectively.
 * Here is a larger example which only considers the previous 5 numbers (and has a preamble of length 5):
 * <p>
 * 35
 * 20
 * 15
 * 25
 * 47
 * 40
 * 62
 * 55
 * 65
 * 95
 * 102
 * 117
 * 150
 * 182
 * 127
 * 219
 * 299
 * 277
 * 309
 * 576
 * In this example, after the 5-number preamble, almost every number is the sum of two of the previous 5 numbers; the
 * only number that does not follow this rule is 127.
 *
 * @author chris.jackson
 */
public class Day9
{
    /**
     * Constructor
     */
    public Day9() throws AnswerNotAvailableException
    {
        // Check the logic with the example before calculating answers
        testLogic();

        System.out.println( THE_ANSWER_IS_PT1 + part1( getData(), 25 ) );
        System.out.println( THE_ANSWER_IS_PT2 + part2( getData(), 25 ) );
    }

    /**
     * --- Part One ---
     * The first step of attacking the weakness in the XMAS data is to find the first number in the list (after the
     * preamble) which is not the sum of two of the 25 numbers before it. What is the first number that does not have
     * this property?
     * <p>
     * Answer: 15353384
     *
     * @param data     the data to process for the question
     * @param preamble the number of values proceeding it to check
     */
    private Long part1( final List<Long> data, final int preamble ) throws AnswerNotAvailableException
    {
        return findWeakness( data, preamble );
    }

    /**
     * --- Part Two ---
     * <p>
     * The final step in breaking the XMAS encryption relies on the invalid number you just found: you must find
     * a contiguous set of at least two numbers in your list which sum to the invalid number from step 1.
     * <p>
     * In this list, adding up all of the numbers from 15 through 40 produces the invalid number from step 1, 127
     * . (Of course, the contiguous set of numbers in your actual list might be much longer.)
     * <p>
     * To find the encryption weakness, add together the smallest and largest number in this contiguous range; in
     * this example, these are 15 and 47, producing 62.
     * <p>
     * What is the encryption weakness in your XMAS-encrypted list of numbers?
     * <p>
     * Answer: 2466556
     *
     * @param data     the data to process for the question
     * @param preamble the number of values proceeding it to check
     */
    private Long part2( final List<Long> data, final int preamble ) throws AnswerNotAvailableException
    {
        final long value = part1( data, preamble );
        final List<Long> contiguousList = getContiguousList( data, value );
        return Collections.min( contiguousList ) + Collections.max( contiguousList );
    }


    /**
     * Finds the weakness by looking for the number in the list that isn't the sum of two numbers in the values
     * proceeding it.  Those numbers are determined by the preamble range
     *
     * @param data     the list of Long values to use
     * @param preamble the number of values proceeding it to check
     * @return the number that doesn't have a sum of two values
     * @throws AnswerNotAvailableException if there isn't a weakness in the provided data
     */
    private Long findWeakness( final List<Long> data, final int preamble ) throws AnswerNotAvailableException
    {
        int index = preamble;
        long targetSum;
        boolean found;

        // We'll loop over every number until we reach the last
        while ( index <= data.size() )
        {
            found = false;
            targetSum = data.get( index );

            final List<Long> preambleList = new ArrayList<>();

            /*
             * Start at n back, where n is the current position minus the preamble.
             * This should get us the *preamble* number before the current
             */
            for ( int n = index - ( preamble ); n < index; n++ )
            {
                preambleList.add( data.get( n ) );
            }

            for ( final Long x : preambleList )
            {
                final List<Long> sumList = new ArrayList<>( preambleList );
                sumList.remove( x );
                for ( final Long y : sumList )
                {
                    if ( x + y == targetSum )
                    {
                        // If the sum has been found, exit the loop
                        found = true;
                        index++;
                        break;
                    }
                }

                // Also exit this loop if the sum has been found
                if ( found )
                {
                    break;
                }
            }

            // If the sum wasn't found, this is the number we're looking for
            if ( !found )
            {
                return targetSum;
            }
        }
        // There wasn't a weakness!
        throw new AnswerNotAvailableException();
    }


    /**
     * Produce a list of contiguous numbers from the provided data that are the sum of the target
     *
     * @param data   the list of Long values to use
     * @param target the value to use as the target sum
     * @return the list of numbers that sum to the target
     * @throws AnswerNotAvailableException if there isn't a weakness in the provided data
     */
    private List<Long> getContiguousList( final List<Long> data, final long target ) throws AnswerNotAvailableException
    {
        // initialise all the values
        long sumOfCumulative;
        int startingIndex = 0;
        final List<Long> contiguousList = new ArrayList<>();

        // We'll start at the startingIndex until we run out of positions to start at
        while ( startingIndex < data.size() )
        {
            contiguousList.clear();
            sumOfCumulative = 0;

            // Start adding the first number of our index
            int dataIndex = startingIndex;

            // Loop until we've gone 'bust'
            while ( sumOfCumulative < target )
            {
                contiguousList.add( data.get( dataIndex ) );

                // Check the sum of the cumulative number list
                sumOfCumulative = contiguousList.stream().mapToLong( Long::longValue ).sum();

                if ( sumOfCumulative == target )
                {
                    // We've found our list
                    return contiguousList;
                }

                // Otherwise, we'll get the next number
                dataIndex++;
            }

            // Bust! Start at the next number this time
            startingIndex++;
        }
        throw new AnswerNotAvailableException();
    }

    /**
     * Get the data for the question
     *
     * @return Long list of the data
     */
    private List<Long> getData()
    {
        return AOCUtils.getLongData( getClass().getName() );
    }

    /* *************** *
     *     TESTS       *
     * *************** */

    /**
     * Checks the logic against the examples in the question.
     */
    private void testLogic() throws AnswerNotAvailableException
    {
        final List<Long> testLongData = new ArrayList<>();
        final List<String> testData = Arrays.asList(
                "35",
                "20",
                "15",
                "25",
                "47",
                "40",
                "62",
                "55",
                "65",
                "95",
                "102",
                "117",
                "150",
                "182",
                "127",
                "219",
                "299",
                "277",
                "309",
                "576"
        );

        // Convert the strings into Longs.  This is because one the values in the challenge are too big for Ints!
        for ( final String stringValue : testData )
        {
            testLongData.add( Long.valueOf( stringValue ) );
        }

        assert part1( testLongData, 5 ) == 127 : PART_1_TEST_FAILED;
        assert part2( testLongData, 5 ) == 62 : PART_2_TEST_FAILED;
    }
}
