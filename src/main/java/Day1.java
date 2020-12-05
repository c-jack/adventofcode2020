/*
 * Copyright (c) 5/12/2020 Chris Jackson (c-jack)
 * adventofcode.Day1
 */

import static constants.Constants.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import exception.AnswerNotAvailableException;
import utils.AOCUtils;

/**
 * Advent of Code 2020
 * Day 1
 * <p>
 * --- Day 1: Report Repair ---
 * After saving Christmas five years in a row, you've decided to take a vacation at a nice resort on a tropical
 * island. Surely, Christmas will go on without you.
 * <p>
 * The tropical island has its own currency and is entirely cash-only. The gold coins used there have a little
 * picture of a starfish; the locals just call them stars. None of the currency exchanges seem to have heard of them,
 * but somehow, you'll need to find fifty of these coins by the time you arrive so you can pay the deposit on your room.
 * <p>
 * To save your vacation, you need to get all fifty stars by December 25th.
 * <p>
 * Collect stars by solving puzzles. Two puzzles will be made available on each day in the Advent calendar; the
 * second puzzle is unlocked when you complete the first. Each puzzle grants one star. Good luck!
 * <p>
 * Before you leave, the Elves in accounting just need you to fix your expense report (your puzzle input);
 * apparently, something isn't quite adding up.
 * <p>
 * Specifically, they need you to find the two entries that sum to 2020 and then multiply those two numbers together.
 * <p>
 * For example, suppose your expense report contained the following:
 * <p>
 * 1721
 * 979
 * 366
 * 299
 * 675
 * 1456
 * In this list, the two entries that sum to 2020 are 1721 and 299. Multiplying them together produces 1721 * 299 =
 * 514579, so the correct answer is 514579.
 * <p>
 * Of course, your expense report is much larger. Find the two entries that sum to 2020; what do you get if you
 * multiply them together?
 *
 * @author chris.jackson
 */
public class Day1
{

    /**
     * Constructor
     */
    public Day1() throws AnswerNotAvailableException
    {
        System.out.println( THE_ANSWER_IS_PT1 + part1() );
        System.out.println( THE_ANSWER_IS_PT2 + part2() );
    }

    /**
     * --- Part One ---
     * Find the two entries that sum to 2020; what do you get if you
     * multiply them together?
     *
     * Answer: 974304
     */
    private int part1() throws AnswerNotAvailableException
    {
        return getAnswer( 1 );
    }


    /**
     * --- Part Two ---
     * The Elves in accounting are thankful for your help; one of them even offers you a starfish coin they
     * had left
     * over
     * from a past vacation. They offer you a second one if you can find three numbers in your expense report
     * that meet
     * the same criteria.
     * <p>
     * Using the above example again, the three entries that sum to 2020 are 979, 366, and 675. Multiplying them
     * together
     * produces the answer, 241861950.
     * <p>
     * In your expense report, what is the product of the three entries that sum to 2020?
     *
     * Answer: 236430480
     */
    private int part2() throws AnswerNotAvailableException
    {
        return getAnswer( 2 );
    }

    /**
     * Return the answer to the given part
     *
     * @param part the criteria switch depending on which question part
     */
    private int getAnswer( final int part ) throws AnswerNotAvailableException
    {
        // Get the values from the file
        final List<Integer> integerList = getData();

        // Loop through all the values
        for ( final int i : integerList )
        {
            // Remove this value from the list (dodgy Java 'remove int from list' scenario!)
            final List<Integer> others = new ArrayList<>( integerList );
            others.removeAll( Collections.singletonList( i ) );

            // Then loop through that list
            for ( final int x : others )
            {

                if ( part == 1 )
                {
                    // If sum matches, return the product
                    if ( i + x == 2020 )
                    {
                        return i * x;
                    }
                }
                else if ( part == 2 )
                {
                    // Remove this value from the list
                    final List<Integer> others2 = new ArrayList<>( integerList );
                    others.removeAll( Collections.singletonList( i ) );

                    // Then loop through the next list
                    for ( final int y : others2 )
                    {
                        // If sum matches, return the product
                        if ( i + x + y == 2020 )
                        {
                            return i * x * y;
                        }
                    }
                }
            }
        }
        throw new AnswerNotAvailableException();
    }


    /**
     * Get the data for the question
     *
     * @return list of numbers
     */
    private List<Integer> getData()
    {
        return AOCUtils.getIntegerData( getClass().getName() );
    }

}
