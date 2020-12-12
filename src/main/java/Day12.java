/*
 * Copyright (c) 12/12/2020 Chris Jackson (c-jack)
 * adventofcode.Day12
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
 * Day 12
 * <p>
 * --- Day 12: Rain Risk ---
 * Your ferry made decent progress toward the island, but the storm came in faster than anyone expected. The ferry
 * needs to take evasive actions!
 * <p>
 * Unfortunately, the ship's navigation computer seems to be malfunctioning; rather than giving a route directly to
 * safety, it produced extremely circuitous instructions. When the captain uses the PA system to ask if anyone can
 * help, you quickly volunteer.
 * <p>
 * The navigation instructions (your puzzle input) consists of a sequence of single-character actions paired with
 * integer input values. After staring at them for a few minutes, you work out what they probably mean:
 * <p>
 * Action N means to move north by the given value.
 * Action S means to move south by the given value.
 * Action E means to move east by the given value.
 * Action W means to move west by the given value.
 * Action L means to turn left the given number of degrees.
 * Action R means to turn right the given number of degrees.
 * Action F means to move forward by the given value in the direction the ship is currently facing.
 * The ship starts by facing east. Only the L and R actions change the direction the ship is facing. (That is, if the
 * ship is facing east and the next instruction is N10, the ship would move north 10 units, but would still move east
 * if the following action were F.)
 * <p>
 * For example:
 * <p>
 * F10
 * N3
 * F7
 * R90
 * F11
 * These instructions would be handled as follows:
 * <p>
 * F10 would move the ship 10 units east (because the ship starts by facing east) to east 10, north 0.
 * N3 would move the ship 3 units north to east 10, north 3.
 * F7 would move the ship another 7 units east (because the ship is still facing east) to east 17, north 3.
 * R90 would cause the ship to turn right by 90 degrees and face south; it remains at east 17, north 3.
 * F11 would move the ship 11 units south to east 17, south 8.
 * At the end of these instructions, the ship's Manhattan distance (sum of the absolute values of its east/west
 * position and its north/south position) from its starting position is 17 + 8 = 25.
 *
 * @author chris.jackson
 */
public class Day12
{

    private Direction currentDirection;
    private final Map<Direction, Integer> manhattanMap = new HashMap<>();
    private final Map<Direction, Integer> wayPointMap = new HashMap<>();

    /**
     * Constructor
     */
    public Day12() throws AnswerNotAvailableException
    {
        // Check the logic with the example before calculating answers
        testLogic();

        System.out.println( THE_ANSWER_IS_PT1 + part1( getData() ) );
        System.out.println( THE_ANSWER_IS_PT2 + part2( getData() ) );
    }

    /**
     * --- Part One ---
     * Figure out where the navigation instructions lead. What is the Manhattan distance between that location and
     * the ship's starting position?
     * <p>
     * Answer: 1482
     *
     * @param data the data to process for the question
     */
    private int part1( final List<String> data ) throws AnswerNotAvailableException
    {
        // Make sure the ship is reset
        reset();

        for ( final String instruction : data )
        {
            processInstruction( instruction, 1 );
        }
        return calculateManhattanDistance();
    }

    /**
     * --- Part Two ---
     * Before you can give the destination to the captain, you realize that the actual action meanings were printed
     * on the back of the instructions the whole time.
     * <p>
     * Almost all of the actions indicate how to move a waypoint which is relative to the ship's position:
     * <p>
     * Action N means to move the waypoint north by the given value.
     * Action S means to move the waypoint south by the given value.
     * Action E means to move the waypoint east by the given value.
     * Action W means to move the waypoint west by the given value.
     * Action L means to rotate the waypoint around the ship left (counter-clockwise) the given number of degrees.
     * Action R means to rotate the waypoint around the ship right (clockwise) the given number of degrees.
     * Action F means to move forward to the waypoint a number of times equal to the given value.
     * The waypoint starts 10 units east and 1 unit north relative to the ship. The waypoint is relative to the ship;
     * that is, if the ship moves, the waypoint moves with it.
     * <p>
     * For example, using the same instructions as above:
     * <p>
     * F10 moves the ship to the waypoint 10 times (a total of 100 units east and 10 units north), leaving the ship
     * at east 100, north 10. The waypoint stays 10 units east and 1 unit north of the ship.
     * N3 moves the waypoint 3 units north to 10 units east and 4 units north of the ship. The ship remains at east
     * 100, north 10.
     * F7 moves the ship to the waypoint 7 times (a total of 70 units east and 28 units north), leaving the ship at
     * east 170, north 38. The waypoint stays 10 units east and 4 units north of the ship.
     * R90 rotates the waypoint around the ship clockwise 90 degrees, moving it to 4 units east and 10 units south of
     * the ship. The ship remains at east 170, north 38.
     * F11 moves the ship to the waypoint 11 times (a total of 44 units east and 110 units south), leaving the ship
     * at east 214, south 72. The waypoint stays 4 units east and 10 units south of the ship.
     * After these operations, the ship's Manhattan distance from its starting position is 214 + 72 = 286.
     * <p>
     * Figure out where the navigation instructions actually lead. What is the Manhattan distance between that
     * location and the ship's starting position?
     * <p>
     * Answer: 48739
     *
     * @param data the data to process for the question
     */
    private long part2( final List<String> data ) throws AnswerNotAvailableException
    {
        // Make sure the ship is reset
        reset();

        for ( final String instruction : data )
        {
            processInstruction( instruction, 2 );
        }
        return calculateManhattanDistance();
    }


    /**
     * Calculate the ManhattanDistance by making a sum of the absolute values of the North and South values, and the
     * absolute values of the East and West values.
     * There's little to worry about here, as the insertion methods pretty much take care of this, but I've left it
     * verbose for clarity.
     *
     * @return the calculated ManhattanDistance
     */
    private int calculateManhattanDistance()
    {

        final int north = manhattanMap.get( Direction.N );
        final int south = manhattanMap.get( Direction.S );
        final int east = manhattanMap.get( Direction.E );
        final int west = manhattanMap.get( Direction.W );

        final int lateral = north + south;
        final int longitudinal = east + west;
        return lateral + longitudinal;
    }

    /**
     * Processes the movement instruction
     *
     * @param instruction the instruction
     * @param rulesPart   the solution part, to determine the rules to use
     * @throws AnswerNotAvailableException if something is wrong with the logic
     */
    private void processInstruction( final String instruction, final int rulesPart ) throws AnswerNotAvailableException
    {
        final Command command = Command.valueOf( instruction.substring( 0, 1 ) );
        final int value = Integer.parseInt( instruction.substring( 1 ) );

        /*
         * If we're travelling (Direction)
         */
        if ( command instanceof Direction )
        {
            travel( rulesPart, ( Direction ) command, value );
        }
        /*
         * If we're turning (Movement)
         */
        else if ( command instanceof Movement )
        {
            moveShip( rulesPart, ( Movement ) command, value );
        }
    }


    /**
     * Calls {@link #calculateMovement(int, Direction, Map)} using:
     * - ManhattanMap, if it's Part 1
     * - wayPointMap, if it's Part 2
     *
     * @param questionPart the part of the question the method call is for
     * @param direction    the {@link Direction} we're moving in
     * @param distance     the distance we're moving in that direction
     * @throws AnswerNotAvailableException if something is wrong with the solution
     */
    private void travel( final int questionPart,
                         final Direction direction,
                         final int distance )
            throws AnswerNotAvailableException
    {
        if ( questionPart == 1 )
        {
            calculateMovement( distance, direction, manhattanMap );
        }
        else if ( questionPart == 2 )
        {
            calculateMovement( distance, direction, wayPointMap );
        }
    }

    /**
     * Calculates the movement of the ship
     *
     * @param distance  the distance to add
     * @param direction the direction to add it in
     * @param map       the modify.  In Part 1 this will be the ManhattanMap, but in Part 2 this can also be the
     *                  wayPoint.
     * @throws AnswerNotAvailableException if something is wrong with the logic
     */
    private void calculateMovement( final int distance,
                                    final Direction direction,
                                    final Map<Direction, Integer> map )
            throws AnswerNotAvailableException
    {
        // Get the opposite direction (180 degrees in either direction)
        Direction oppositeDirection = null;
        final int degreesInTotal = direction.calculateRotation( Movement.R, 180 );

        for ( final Direction directionValue : Direction.values() )
        {
            if ( degreesInTotal == directionValue.degrees )
            {
                oppositeDirection = directionValue;
                break;
            }
        }

        if ( oppositeDirection != null )
        {
            // Get the current value for the opposite direction
            final Integer oppositeDirectionValue = map.get( oppositeDirection );

            // Set a mutable value for the distance
            int distanceInGivenDirection = distance;

            /*
             * If the opposite direction has a value, but it's smaller than our movement, reduce our movement by the
             * opposite direction's value, then set the opposite direction to zero and move the ship the remainder in
             *  the
             * nominated direction
             */
            if ( oppositeDirectionValue > 0 && oppositeDirectionValue < distance )
            {
                distanceInGivenDirection -= oppositeDirectionValue;
                map.put( direction, distanceInGivenDirection );
                map.put( oppositeDirection, 0 );
            }
            /*
             * If the opposite direction has a value, and it's greater than our movement, reduce our that direction's
             *  value
             * by the value of the nominated direction
             */
            else if ( oppositeDirectionValue > 0 )
            {
                map.put( oppositeDirection, oppositeDirectionValue - distanceInGivenDirection );
            }
            /*
             * Otherwise, the opposite direction is zero so just add the the current value of this direction
             */
            else
            {
                map.put( direction, map.get( direction ) + distance );
            }
        }
        else
        {
            // Something went wrong!
            throw new AnswerNotAvailableException();
        }

    }

    /**
     * Moves the ship according to the {@link Movement}
     * - if it's L or R, the ship will be rotated
     * - if it's F, the ship will move forward
     *
     * @param questionPart the part of the question the method call is for
     * @param movement     the {@link Movement} to execute
     * @param distance     the distance we're moving or rotating
     * @throws AnswerNotAvailableException if something is wrong with the solution
     */
    private void moveShip( final int questionPart, final Movement movement, final int distance )
            throws AnswerNotAvailableException
    {
        if ( movement.equals( Day12.Movement.L ) || movement.equals( Day12.Movement.R ) )
        {
            if ( questionPart == 1 )
            {
                this.currentDirection = currentDirection.rotateShip( movement, distance );
            }
            else if ( questionPart == 2 )
            {
                turnRelativeToWayPoint( movement, distance );
            }
        }
        else if ( movement.equals( Day12.Movement.F ) )
        {
            if ( questionPart == 1 )
            {
                calculateMovement( distance, currentDirection, manhattanMap );
            }
            else if ( questionPart == 2 )
            {
                // In Part 2, we need to move in each Way Point direction
                for ( final Map.Entry<Direction, Integer> directionEntry : wayPointMap.entrySet() )
                {
                    final Direction direction = directionEntry.getKey();
                    final int totalMovement = distance * directionEntry.getValue();
                    calculateMovement( totalMovement, direction, manhattanMap );
                }
            }
        }
    }


    /**
     * Modifies the ship's direction relative to the wayPointMap
     *
     * @param movement      the {@link Movement} to make
     * @param degreesToTurn the number of degrees to turn
     */
    private void turnRelativeToWayPoint( final Movement movement,
                                         final int degreesToTurn )
    {
        final Map<Direction, Integer> newWayPoint = new HashMap<>();

        for ( final Map.Entry<Direction, Integer> direction : wayPointMap.entrySet() )
        {

            // Rotate starting from the current Waypoint direction degrees
            final Direction thisDirection = direction.getKey();
            final int degreesInTotal = thisDirection.calculateRotation( movement, degreesToTurn );

            for ( final Direction directionValue : Direction.values() )
            {
                if ( degreesInTotal == directionValue.degrees )
                {
                    newWayPoint.put( directionValue, direction.getValue() );
                    break;
                }
            }
        }
        // Clear and replace the wayPointMap values with the new ones
        wayPointMap.clear();
        wayPointMap.putAll( newWayPoint );
    }

    /**
     * A 'Movement' modifies the existing direction of travel
     * <p>
     * Action L means to turn left the given number of degrees.
     * Action R means to turn right the given number of degrees.
     * Action F means to move forward by the given value in the direction the ship is currently facing.
     */
    enum Movement implements Command
    {
        L,
        R,
        F
    }

    /**
     * A 'Direction' moves the ship in a particular direction.
     * <p>
     * Action N means to move north by the given value.
     * Action S means to move south by the given value.
     * Action E means to move east by the given value.
     * Action W means to move west by the given value.
     */
    enum Direction implements Command
    {
        N( 0 ),
        E( 90 ),
        S( 180 ),
        W( 270 );

        private final int degrees;

        /**
         * Constructor
         */
        Direction( final int degrees )
        {
            this.degrees = degrees;
        }

        /**
         * Moves by the given value.
         * The value is first corrected to be the movement relative to North, giving rotationAngle
         *
         * @param movement       the {@link Movement}
         * @param turningDegrees the instructed number of degrees to turn
         * @return The {@link Direction} the ship is now facing
         */
        public Direction rotateShip( final Movement movement,
                                     final int turningDegrees )
                throws AnswerNotAvailableException
        {
            // Calculate the desired rotation relative to North
            final int rotationAngle = calculateRotation( movement, turningDegrees );

            for ( final Direction direction : Direction.values() )
            {
                if ( rotationAngle == direction.degrees )
                {
                    return direction;
                }
            }
            // If we reach here, something went wrong!
            throw new AnswerNotAvailableException();
        }


        /**
         * Calculates the ship's rotation
         * This could have been done in several ways, but I thought it would be interesting to compensate for the
         * 'overflow' of the 360 issue, plus the 0=360 problem.
         *
         * @param movement      the direction to move in ({@link Movement#L} = anti-clockwise)
         * @param degreesToTurn the degrees to rotate by
         * @return the 'corrected' rotation value
         */
        private int calculateRotation( final Movement movement, final int degreesToTurn )
        {
            int degreesInTotal = this.degrees;
            if ( movement.equals( Movement.L ) )
            {
                degreesInTotal -= degreesToTurn;
            }
            else if ( movement.equals( Movement.R ) )
            {
                degreesInTotal += degreesToTurn;
            }

            if ( degreesInTotal >= 360 )
            {
                degreesInTotal -= 360;
            }
            else if ( degreesInTotal < 0 )
            {
                degreesInTotal = 360 + degreesInTotal;
            }
            return degreesInTotal;
        }
    }

    /**
     * Interface to classify the collective of either a {@link Movement} or {@link Direction}
     */
    interface Command
    {
        /**
         * Implements the 'valueOf' command for either a {@link Movement} or {@link Direction}
         *
         * @param value the value to match
         * @return a matching {@link Movement} or {@link Direction}
         */
        static Command valueOf( final String value )
        {
            try
            {
                return Direction.valueOf( value );
            }
            catch ( final IllegalArgumentException e )
            {
                return Movement.valueOf( value );
            }
        }
    }

    /* *************** *
     *    Helpers      *
     * *************** */

    /**
     * Resets all the class variables ahead of the solution
     */
    private void reset()
    {
        currentDirection = Direction.E;
        manhattanMap.clear();
        manhattanMap.put( Direction.N, 0 );
        manhattanMap.put( Direction.E, 0 );
        manhattanMap.put( Direction.S, 0 );
        manhattanMap.put( Direction.W, 0 );
        wayPointMap.clear();
        wayPointMap.put( Direction.N, 1 );
        wayPointMap.put( Direction.E, 10 );
        wayPointMap.put( Direction.S, 0 );
        wayPointMap.put( Direction.W, 0 );
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

    /* *************** *
     *     TESTS       *
     * *************** */

    /**
     * Checks the logic against the examples in the question.
     */
    private void testLogic() throws AnswerNotAvailableException
    {
        final List<String> testData = Arrays.asList( "F10",
                "N3",
                "F7",
                "R90",
                "F11" );
        assert part1( testData ) == 25 : PART_1_TEST_FAILED;
        assert part2( testData ) == 286 : PART_2_TEST_FAILED;
    }
}
