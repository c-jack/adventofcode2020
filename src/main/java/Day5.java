/*
 * Copyright (c) 5/12/2020 Chris Jackson (c-jack)
 * adventofcode.Day5
 */

import static constants.Constants.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import exception.AnswerNotAvailableException;
import utils.AOCUtils;


/**
 * Advent of Code 2020
 * Day 5
 * <p>
 * --- Day 5: Binary Boarding ---
 * You board your plane only to discover a new problem: you dropped your boarding pass! You aren't sure which seat is
 * yours, and all of the flight attendants are busy with the flood of people that suddenly made it through passport
 * control.
 * <p>
 * You write a quick program to use your phone's camera to scan all of the nearby boarding passes (your puzzle input);
 * perhaps you can find your seat through process of elimination.
 * <p>
 * Instead of zones or groups, this airline uses binary space partitioning to seat people. A seat might be specified
 * like FBFBBFFRLR, where F means "front", B means "back", L means "left", and R means "right".
 * <p>
 * The first 7 characters will either be F or B; these specify exactly one of the 128 rows on the plane (numbered 0
 * through 127). Each letter tells you which half of a region the given seat is in. Start with the whole list of rows;
 * the first letter indicates whether the seat is in the front (0 through 63) or the back (64 through 127). The next
 * letter indicates which half of that region the seat is in, and so on until you're left with exactly one row.
 * <p>
 * For example, consider just the first seven characters of FBFBBFFRLR:
 * <p>
 * Start by considering the whole range, rows 0 through 127.
 * F means to take the lower half, keeping rows 0 through 63.
 * B means to take the upper half, keeping rows 32 through 63.
 * F means to take the lower half, keeping rows 32 through 47.
 * B means to take the upper half, keeping rows 40 through 47.
 * B keeps rows 44 through 47.
 * F keeps rows 44 through 45.
 * The final F keeps the lower of the two, row 44.
 * The last three characters will be either L or R; these specify exactly one of the 8 columns of seats on the plane
 * (numbered 0 through 7). The same process as above proceeds again, this time with only three steps. L means to keep
 * the lower half, while R means to keep the upper half.
 * <p>
 * For example, consider just the last 3 characters of FBFBBFFRLR:
 * <p>
 * Start by considering the whole range, columns 0 through 7.
 * R means to take the upper half, keeping columns 4 through 7.
 * L means to take the lower half, keeping columns 4 through 5.
 * The final R keeps the upper of the two, column 5.
 * So, decoding FBFBBFFRLR reveals that it is the seat at row 44, column 5.
 * <p>
 * Every seat also has a unique seat ID: multiply the row by 8, then add the column. In this example, the seat has ID
 * 44 * 8 + 5 = 357.
 * <p>
 * Here are some other boarding passes:
 * <p>
 * BFFFBBFRRR: row 70, column 7, seat ID 567.
 * FFFBBBFRRR: row 14, column 7, seat ID 119.
 * BBFFBBFRLL: row 102, column 4, seat ID 820.
 * As a sanity check, look through your list of boarding passes. What is the highest seat ID on a boarding pass?
 *
 * @author chris.jackson
 */
public class Day5
{

    private final List<Integer> seatIDs = new ArrayList<>();
    private List<Integer> row = new ArrayList<>();
    private List<Integer> aisle = new ArrayList<>();

    /**
     * Constructor
     */
    public Day5() throws AnswerNotAvailableException
    {
        // Check against examole
        final int example = calculateSeatID( "FBFBBFFRLR" );
        assert example == 357;

        System.out.println( THE_ANSWER_IS_PT1 + part1() );
        System.out.println( THE_ANSWER_IS_PT2 + part2() );
    }

    /**
     * --- Part One ---
     * What is the highest seat ID on a boarding pass?
     * <p>
     * Answer: 980
     */
    private int part1() throws AnswerNotAvailableException
    {
        calculateSeatIDs();
        return Collections.max( seatIDs );
    }


    /**
     * --- Part Two ---
     * Ding! The "fasten seat belt" signs have turned on. Time to find your seat.
     * <p>
     * It's a completely full flight, so your seat should be the only missing boarding pass in your list. However,
     * there's a catch: some of the seats at the very front and back of the plane don't exist on this aircraft, so
     * they'll be missing from your list as well.
     * <p>
     * Your seat wasn't at the very front or back, though; the seats with IDs +1 and -1 from yours will be in your list.
     * <p>
     * What is the ID of your seat?
     * <p>
     * Answer: 607
     */
    private long part2() throws AnswerNotAvailableException
    {
        calculateSeatIDs();

        /*
         * Simplistically, the spare seat will be the one that is neither at the start or end,
         * and not in the list of other tickets
         */
        final List<Integer> numberList = createNumberList( Collections.min( seatIDs ), Collections.max( seatIDs ) );
        numberList.removeAll( seatIDs );

        // If it has worked, there should only be one left
        assert numberList.size() == 1;

        return numberList.get( 0 );
    }

    /**
     * Calculate all the seat IDs from the {@link #seatIDs} list
     *
     * @throws AnswerNotAvailableException if an invalid ticket pattern is supplied
     */
    private void calculateSeatIDs() throws AnswerNotAvailableException
    {
        final List<String> data = getData();

        // Loop through all the lines - each one representing a ticket
        for ( final String ticket : data )
        {
            seatIDs.add( calculateSeatID( ticket ) );
        }
    }

    /**
     * Calculates the Seat ID from the ticket string
     *
     * @param ticket the String representing the ticket
     * @return the int Seat ID
     * @throws AnswerNotAvailableException if an invalid ticket pattern is supplied
     */
    private int calculateSeatID( final String ticket ) throws AnswerNotAvailableException
    {
        final char[] ticketData = ticket.toCharArray();

        // Create the initial lists of rows (0-127) and aisle seats (0-7)
        row = createNumberList( 0, 127 );
        aisle = createNumberList( 0, 7 );

        for ( final char location : ticketData )
        {
            switch ( location )
            {
                case 'F':
                    // F = Front
                    reduceRow( Strategy.LOWER );
                    break;
                case 'B':
                    // B = Back
                    reduceRow( Strategy.UPPER );
                    break;
                case 'L':
                    // L = Left in the aisle
                    reduceAisle( Strategy.LOWER );
                    break;
                case 'R':
                    // R = Right in the aisle
                    reduceAisle( Strategy.UPPER );
                    break;
                default:
                    throw new AnswerNotAvailableException();
            }
        }

        return row.get( 0 ) * 8 + aisle.get( 0 );
    }

    /**
     * Keeps the lower half of the row list
     */
    private void reduceRow( final Strategy strategy )
    {
        row = reduceList( row, strategy );
    }

    /**
     * Keeps the lower half of the aisle list
     */
    private void reduceAisle( final Strategy strategy )
    {
        aisle = reduceList( aisle, strategy );
    }

    private List<Integer> reduceList( final List<Integer> integerList, final Strategy strategy )
    {
        // Find the min, max, and range
        final Integer max = Collections.max( integerList );
        final Integer min = Collections.min( integerList );
        final double range = max - min;

        // Find the mid point of the upper and lower limits
        final int halfRange = ( int ) Math.floor( range / 2 );

        final int upper;
        final int lower;

        if ( strategy.equals( Strategy.LOWER ) )
        {
            // The upper limit will be one below the mid point
            upper = ( max - halfRange ) - 1;

            // The lower limit is just whatever the lowest number already is
            lower = min;
        }
        else
        {
            // The upper limit is just whatever the highest number already is
            upper = max;

            // The lower limit will be one above the mid point
            lower = min + halfRange + 1;
        }

        // Set the reduced list
        return createNumberList( lower, upper );
    }

    /**
     * Creates a number list between the min and max (inclusive)
     *
     * @param min the number to start from
     * @param max the highest number to include
     * @return the list containing numbers between the min and max (inclusive)
     */
    private List<Integer> createNumberList( final int min, final int max )
    {
        final List<Integer> rows = new ArrayList<>();

        for ( int n = min; n <= max; n++ )
        {
            rows.add( n );
        }

        return rows;
    }

    /**
     * Get the data for the question
     *
     * @return string list of the data
     */
    private List<String> getData()
    {
        return AOCUtils.getData( getClass().getName() );
    }


    /**
     * Enum for the list reduction strategy
     */
    enum Strategy
    {
        UPPER,
        LOWER
    }
}
