/*
 * Copyright (c) 11/12/2020 Chris Jackson (c-jack)
 * adventofcode.Day11
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
 * Day 11
 * <p>
 * --- Day 11: Seating System ---
 * Your plane lands with plenty of time to spare. The final leg of your journey is a ferry that goes directly to the
 * tropical island where you can finally start your vacation. As you reach the waiting area to board the ferry, you
 * realize you're so early, nobody else has even arrived yet!
 * <p>
 * By modeling the process people use to choose (or abandon) their seat in the waiting area, you're pretty sure you
 * can predict the best place to sit. You make a quick map of the seat layout (your puzzle input).
 * <p>
 * The seat layout fits neatly on a grid. Each position is either floor (.), an empty seat (L), or an occupied seat
 * (#). For example, the initial seat layout might look like this:
 * <p>
 * L.LL.LL.LL
 * LLLLLLL.LL
 * L.L.L..L..
 * LLLL.LL.LL
 * L.LL.LL.LL
 * L.LLLLL.LL
 * ..L.L.....
 * LLLLLLLLLL
 * L.LLLLLL.L
 * L.LLLLL.LL
 * Now, you just need to model the people who will be arriving shortly. Fortunately, people are entirely predictable
 * and always follow a simple set of rules. All decisions are based on the number of occupied seats adjacent to a
 * given seat (one of the eight positions immediately up, down, left, right, or diagonal from the seat). The
 * following rules are applied to every seat simultaneously:
 * <p>
 * If a seat is empty (L) and there are no occupied seats adjacent to it, the seat becomes occupied.
 * If a seat is occupied (#) and four or more seats adjacent to it are also occupied, the seat becomes empty.
 * Otherwise, the seat's state does not change.
 * Floor (.) never changes; seats don't move, and nobody sits on the floor.
 * <p>
 * After one round of these rules, every seat in the example layout becomes occupied:
 * <p>
 * #.##.##.##
 * #######.##
 * #.#.#..#..
 * ####.##.##
 * #.##.##.##
 * #.#####.##
 * ..#.#.....
 * ##########
 * #.######.#
 * #.#####.##
 * After a second round, the seats with four or more occupied adjacent seats become empty again:
 * <p>
 * #.LL.L#.##
 * #LLLLLL.L#
 * L.L.L..L..
 * #LLL.LL.L#
 * #.LL.LL.LL
 * #.LLLL#.##
 * ..L.L.....
 * #LLLLLLLL#
 * #.LLLLLL.L
 * #.#LLLL.##
 * This process continues for three more rounds:
 * <p>
 * #.##.L#.##
 * #L###LL.L#
 * L.#.#..#..
 * #L##.##.L#
 * #.##.LL.LL
 * #.###L#.##
 * ..#.#.....
 * #L######L#
 * #.LL###L.L
 * #.#L###.##
 * #.#L.L#.##
 * #LLL#LL.L#
 * L.L.L..#..
 * #LLL.##.L#
 * #.LL.LL.LL
 * #.LL#L#.##
 * ..L.L.....
 * #L#LLLL#L#
 * #.LLLLLL.L
 * #.#L#L#.##
 * #.#L.L#.##
 * #LLL#LL.L#
 * L.#.L..#..
 * #L##.##.L#
 * #.#L.LL.LL
 * #.#L#L#.##
 * ..L.L.....
 * #L#L##L#L#
 * #.LLLLLL.L
 * #.#L#L#.##
 * At this point, something interesting happens: the chaos stabilizes and further applications of these rules cause
 * no seats to change state! Once people stop moving around, you count 37 occupied seats.
 *
 * @author chris.jackson
 */
public class Day11
{

    public static final char OCCUPIED_SEAT = '#';
    public static final char UNOCCUPIED_SEAT = 'L';
    public static final char FLOOR = '.';
    public static final char SPACE_CHAR = ' ';
    private Map<Integer, char[]> lastMap = new HashMap<>();

    /**
     * Constructor
     */
    public Day11() throws AnswerNotAvailableException
    {
        // Check the logic with the example before calculating answers
        testLogic();

        System.out.println( THE_ANSWER_IS_PT1 + part1( getData() ) );
        System.out.println( THE_ANSWER_IS_PT2 + part2( getData() ) );
    }

    /**
     * --- Part One ---
     * Simulate your seating area by applying the seating rules repeatedly until no seats change state. How many
     * seats end up occupied?
     * <p>
     * Answer: 2273
     *
     * @param seatMap the seat map to process for the question
     */
    private int part1( final SeatMap seatMap )
    {
        return calculateOccupiedSeatsWhenSettled( seatMap, 1 );
    }


    /**
     * Calculates how many seats are occupied when the pattern has settled
     * @param seatMap the {@link SeatMap} to use
     * @param part the solution part (triggers different rules)
     * @return number of occupied seats when the pattern has settled
     */
    private int calculateOccupiedSeatsWhenSettled( final SeatMap seatMap, final int part )
    {
        // Set the part to determined the rules to use
        seatMap.setRulesPart( part );

        // Should the seats be shifted?
        boolean reCalculateSeats = true;

        // While seats should be shifted
        while ( reCalculateSeats )
        {
            seatMap.calculate();

            // This will break the loop when set in the SeatMap
            reCalculateSeats = seatMap.isModified();
        }

        // Return the count of the last position
        return seatMap.countOccupiedSeats();
    }

    /**
     * --- Part Two ---
     * As soon as people start to arrive, you realize your mistake. People don't just care about adjacent seats -
     * they care about the first seat they can see in each of those eight directions!
     * <p>
     * Now, instead of considering just the eight immediately adjacent seats, consider the first seat in each of
     * those eight directions. For example, the empty seat below would see eight occupied seats:
     * <p>
     * .......#.
     * ...#.....
     * .#.......
     * .........
     * ..#L....#
     * ....#....
     * .........
     * #........
     * ...#.....
     * The leftmost empty seat below would only see one empty seat, but cannot see any of the occupied ones:
     * <p>
     * .............
     * .L.L.#.#.#.#.
     * .............
     * The empty seat below would see no occupied seats:
     * <p>
     * .##.##.
     * #.#.#.#
     * ##...##
     * ...L...
     * ##...##
     * #.#.#.#
     * .##.##.
     * Also, people seem to be more tolerant than you expected: it now takes five or more visible occupied seats for
     * an occupied seat to become empty (rather than four or more from the previous rules). The other rules still
     * apply: empty seats that see no occupied seats become occupied, seats matching no rule don't change, and floor
     * never changes.
     * <p>
     * Given the same starting layout as above, these new rules cause the seating area to shift around as follows:
     * <p>
     * L.LL.LL.LL
     * LLLLLLL.LL
     * L.L.L..L..
     * LLLL.LL.LL
     * L.LL.LL.LL
     * L.LLLLL.LL
     * ..L.L.....
     * LLLLLLLLLL
     * L.LLLLLL.L
     * L.LLLLL.LL
     * #.##.##.##
     * #######.##
     * #.#.#..#..
     * ####.##.##
     * #.##.##.##
     * #.#####.##
     * ..#.#.....
     * ##########
     * #.######.#
     * #.#####.##
     * #.LL.LL.L#
     * #LLLLLL.LL
     * L.L.L..L..
     * LLLL.LL.LL
     * L.LL.LL.LL
     * L.LLLLL.LL
     * ..L.L.....
     * LLLLLLLLL#
     * #.LLLLLL.L
     * #.LLLLL.L#
     * #.L#.##.L#
     * #L#####.LL
     * L.#.#..#..
     * ##L#.##.##
     * #.##.#L.##
     * #.#####.#L
     * ..#.#.....
     * LLL####LL#
     * #.L#####.L
     * #.L####.L#
     * #.L#.L#.L#
     * #LLLLLL.LL
     * L.L.L..#..
     * ##LL.LL.L#
     * L.LL.LL.L#
     * #.LLLLL.LL
     * ..L.L.....
     * LLLLLLLLL#
     * #.LLLLL#.L
     * #.L#LL#.L#
     * #.L#.L#.L#
     * #LLLLLL.LL
     * L.L.L..#..
     * ##L#.#L.L#
     * L.L#.#L.L#
     * #.L####.LL
     * ..#.#.....
     * LLL###LLL#
     * #.LLLLL#.L
     * #.L#LL#.L#
     * #.L#.L#.L#
     * #LLLLLL.LL
     * L.L.L..#..
     * ##L#.#L.L#
     * L.L#.LL.L#
     * #.LLLL#.LL
     * ..#.L.....
     * LLL###LLL#
     * #.LLLLL#.L
     * #.L#LL#.L#
     * Again, at this point, people stop shifting around and the seating area reaches equilibrium. Once this occurs,
     * you count 26 occupied seats.
     * <p>
     * Given the new visibility method and the rule change for occupied seats becoming empty, once equilibrium is
     * reached, how many seats end up occupied?
     * <p>
     * Answer: 2064
     *
     * @param seatMap the seatMap to process for the question
     */
    private long part2( final SeatMap seatMap )
    {
        // Set the part to determined the rules to use
        return calculateOccupiedSeatsWhenSettled( seatMap, 2 );
    }

    /**
     * Get the data for the question
     *
     * @return string list of the data
     */
    private SeatMap getData()
    {
        return new SeatMap( AOCUtils.getData( getClass().getName() ) );
    }


    /* *************** *
     *  SeatMap Class  *
     * *************** */

    /**
     * The SeatMap class stores all of the information for the seat layout
     */
    class SeatMap
    {
        private final Map<Integer, char[]> seatMap = new HashMap<>();
        private boolean isModified;
        private int rulesPart;

        /**
         * @param rulesPart The rulesPart to set.
         */
        public void setRulesPart( final int rulesPart )
        {
            this.rulesPart = rulesPart;
        }

        /**
         * Constructor - turns a string seat layout into a Map
         *
         * @param seatData the seat data in a list of rows
         */
        public SeatMap( final List<String> seatData )
        {
            // Set each line to a char array with the row number
            for ( int i = 0, seatDataSize = seatData.size(); i < seatDataSize; i++ )
            {
                final String seatRowData = seatData.get( i );
                final char[] seatRow = seatRowData.toCharArray();
                seatMap.put( i, seatRow );
            }
        }

        /**
         * Looks at the state of the seat at the given co-ordinates in the last layout.
         *
         * @param seatNumber the 'x' co-ordinate (number of the seat in the row)
         * @param rowNumber  the 'y' co-ordinate (number of the row)
         * @return the seat char
         */
        private char getSeatFromLastMap( final int seatNumber, final int rowNumber )
        {
            return getSeat( seatNumber, rowNumber, lastMap );
        }

        /**
         * Looks at the state of the seat at the given co-ordinates in the current layout.
         *
         * @param seatNumber the 'x' co-ordinate (number of the seat in the row)
         * @param rowNumber  the 'y' co-ordinate (number of the row)
         * @return the seat char
         */
        private char getCurrentSeat( final int seatNumber, final int rowNumber )
        {
            return getSeat( seatNumber, rowNumber, seatMap );
        }

        /**
         * Looks at the state of the seat at the given co-ordinates in the provided layout.
         * This is because the rules do not take into account the seat of any seat changed in this iterative switch
         *
         * @param seatNumber the 'x' co-ordinate (number of the seat in the row)
         * @param rowNumber  the 'y' co-ordinate (number of the row)
         * @param seatMap    the map to use
         * @return the seat char
         */
        private char getSeat( final int seatNumber, final int rowNumber, final Map<Integer, char[]> seatMap )
        {
            final int maxRow = seatMap.size();

            // Check the Y co-ordinates are valid first
            if ( seatNumber >= 0 && rowNumber >= 0 && rowNumber < maxRow )
            {
                final int rowLength = seatMap.get( rowNumber ).length;

                // Check the X co-ordinates are valid
                if ( seatNumber < rowLength )
                {
                    return seatMap.get( rowNumber )[ seatNumber ];
                }
            }
            return SPACE_CHAR;
        }

        /**
         * Counts the number of seats that are 'occupied' in the current amp
         *
         * @return the number of seats marked as '#'
         */
        private int countOccupiedSeats()
        {
            int occupiedSeats = 0;
            int row = 0;

            // Iterate each row
            for ( final Map.Entry<Integer, char[]> entry : seatMap.entrySet() )
            {
                final char[] rowSeats = entry.getValue();

                // each seat in the row
                for ( int seatPos = 0; seatPos < rowSeats.length; seatPos++ )
                {
                    if ( getCurrentSeat( seatPos, row ) == OCCUPIED_SEAT )
                    {
                        occupiedSeats++;
                    }
                }
                row++;
            }

            return occupiedSeats;
        }

        /**
         * Sets the value of the provided seat to the given value
         *
         * @param seatNumber the 'x' co-ordinate (number of the seat in the row)
         * @param rowNumber  the 'y' co-ordinate (number of the row)
         * @param value      char value to set (# or L)
         */
        private void setSeatValue( final int seatNumber, final int rowNumber, final char value )
        {
            final char[] rowData = seatMap.get( rowNumber );

            // Change the seat and add the modified entry
            rowData[ seatNumber ] = value;
            seatMap.put( rowNumber, rowData );

            // Set the seat map to 'modified'
            isModified = true;
        }

        /**
         * @param seatNumber the 'x' co-ordinate (number of the seat in the row)
         * @param rowNumber  the 'y' co-ordinate (number of the row)
         */
        private void setSeat( final int seatNumber, final int rowNumber )
        {
            final char currentSeatStatus = getSeatFromLastMap( seatNumber, rowNumber );

            // Don't process the positions where there is no seat ('.')
            if ( currentSeatStatus != FLOOR )
            {
                checkChangeSeat( seatNumber, rowNumber, currentSeatStatus );
            }
        }

        /**
         * Checks if a seat should be changed by counting the number of occupied seats around the current one.
         * This is where the rules are enforced.
         *
         * @param seatNumber        the 'x' co-ordinate (number of the seat in the row)
         * @param rowNumber         the 'y' co-ordinate (number of the row)
         * @param currentSeatStatus the current status of the seat
         */
        private void checkChangeSeat( final int seatNumber, final int rowNumber, final char currentSeatStatus )
        {
            // Part two modifies this to 5
            final int numberOccupiedSeatsToUnoccupy = 4 + ( rulesPart - 1 );
            final int occupiedSeats = getOccupiedSeats( seatNumber, rowNumber );

            if ( currentSeatStatus == UNOCCUPIED_SEAT )
            {
                if ( occupiedSeats == 0 )
                {
                    setSeatValue( seatNumber, rowNumber, OCCUPIED_SEAT );
                }
            }
            if ( currentSeatStatus == OCCUPIED_SEAT )
            {
                if ( occupiedSeats >= numberOccupiedSeatsToUnoccupy )
                {
                    setSeatValue( seatNumber, rowNumber, UNOCCUPIED_SEAT );
                }
            }
        }

        /**
         * Calculates the surrounding occupied seats using the simpler proximity rules for part 1
         *
         * @param seatNumber the 'x' co-ordinate (number of the seat in the row)
         * @param rowNumber  the 'y' co-ordinate (number of the row)
         * @return the total number of occupied sear in proximity to the current seat
         */
        private int getOccupiedSeats( final int seatNumber, final int rowNumber )
        {
            int occupiedSeats = 0;

            occupiedSeats += getSeatStatus( seatNumber, rowNumber, -1, -1 );    // Top Left
            occupiedSeats += getSeatStatus( seatNumber, rowNumber, 0, -1 );     // Above
            occupiedSeats += getSeatStatus( seatNumber, rowNumber, 1, -1 );     // Top Right
            occupiedSeats += getSeatStatus( seatNumber, rowNumber, -1, 0 );     // Left
            occupiedSeats += getSeatStatus( seatNumber, rowNumber, +1, 0 );     // Right
            occupiedSeats += getSeatStatus( seatNumber, rowNumber, -1, +1 );    // Bottom Left
            occupiedSeats += getSeatStatus( seatNumber, rowNumber, 0, +1 );     // Below
            occupiedSeats += getSeatStatus( seatNumber, rowNumber, 1, +1 );     // Bottom Right

            return occupiedSeats;
        }


        /**
         * Get the status of the seat with the given offset.
         * For part 1, this is literal position and includes floor (.)
         * For part 2, this is the nearest seat in that direction
         *
         * @param relativeToSeatNumber the seat number to look relative to
         * @param relativeToSeatRow    the seat row to look relative to
         * @param xOffset              the offset in the x direction
         * @param yOffset              the offset in the y direction
         * @return 1 if the seat is occupied, or 0 if it isn't
         */
        private int getSeatStatus( final int relativeToSeatNumber,
                                   final int relativeToSeatRow,
                                   final int xOffset,
                                   final int yOffset )
        {
            char seatFromLastMap = getSeatFromLastMap( relativeToSeatNumber - xOffset, relativeToSeatRow - yOffset );

            /*
             * If part 2, check if it's "floor" (.).
             * If it is, we'll need to keep going in that direction until we find the first seat
             */
            if ( rulesPart == 2 )
            {
                int multiplier = 2;

                // Loop until we find a seat
                while ( seatFromLastMap == FLOOR )
                {
                    seatFromLastMap = getSeatFromLastMap( relativeToSeatNumber - ( xOffset * multiplier ),
                            relativeToSeatRow - ( yOffset * multiplier ) );
                    multiplier++;
                }
            }

            return seatFromLastMap == OCCUPIED_SEAT ? 1 : 0;
        }


        /**
         * This is set to 'false' when the calculation starts, and will be triggered to 'true' if any sears are changed.
         * This is done in {@link #setSeatValue(int, int, char)}
         *
         * @return The isModified flag
         */
        public boolean isModified()
        {
            return isModified;
        }

        /**
         * Calculate all seat positions and change any as required using the rules for Part 1
         */
        public void calculate()
        {
            setLastMap( seatMap );
            isModified = false;
            int row = 0;

            // Iterate each row
            for ( final Map.Entry<Integer, char[]> entry : seatMap.entrySet() )
            {
                final char[] rowSeats = entry.getValue();

                // Iterate each seat
                for ( int seatPos = 0; seatPos < rowSeats.length; seatPos++ )
                {
                    setSeat( seatPos, row );
                }
                row++;
            }
        }
    }

    /**
     * Sets the last seat map so it can be compared to the current when calculating changes
     *
     * @param seatMap the current map to copy
     */
    private void setLastMap( final Map<Integer, char[]> seatMap )
    {
        lastMap = new HashMap<>();

        for ( final Map.Entry<Integer, char[]> entry : seatMap.entrySet() )
        {
            lastMap.put( entry.getKey(), entry.getValue().clone() );
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
        final List<String> seatData = Arrays.asList( "L.LL.LL.LL",
                "LLLLLLL.LL",
                "L.L.L..L..",
                "LLLL.LL.LL",
                "L.LL.LL.LL",
                "L.LLLLL.LL",
                "..L.L.....",
                "LLLLLLLLLL",
                "L.LLLLLL.L",
                "L.LLLLL.LL" );
        assert part1( new SeatMap( seatData ) ) == 37 : PART_1_TEST_FAILED;
        assert part2( new SeatMap( seatData ) ) == 26 : PART_1_TEST_FAILED;
    }
}
