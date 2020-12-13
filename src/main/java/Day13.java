/*
 * Copyright (c) 13/12/2020 Chris Jackson (c-jack)
 * adventofcode.Day13
 */

import static constants.Constants.*;
import static java.lang.Long.*;
import static java.util.Arrays.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import exception.AnswerNotAvailableException;
import utils.AOCUtils;


/**
 * Advent of Code 2020
 * Day 13
 * <p>
 * --- Day 13: Shuttle Search ---
 * Your ferry can make it safely to a nearby port, but it won't get much further. When you call to book another ship,
 * you discover that no ships embark from that port to your vacation island. You'll need to get from the port to the
 * nearest airport.
 * <p>
 * Fortunately, a shuttle bus service is available to bring you from the sea port to the airport! Each bus has an ID
 * number that also indicates how often the bus leaves for the airport.
 * <p>
 * Bus schedules are defined based on a timestamp that measures the number of minutes since some fixed reference
 * point in the past. At timestamp 0, every bus simultaneously departed from the sea port. After that, each bus
 * travels to the airport, then various other locations, and finally returns to the sea port to repeat its journey
 * forever.
 * <p>
 * The time this loop takes a particular bus is also its ID number: the bus with ID 5 departs from the sea port at
 * timestamps 0, 5, 10, 15, and so on. The bus with ID 11 departs at 0, 11, 22, 33, and so on. If you are there when
 * the bus departs, you can ride that bus to the airport!
 * <p>
 * Your notes (your puzzle input) consist of two lines. The first line is your estimate of the earliest timestamp you
 * could depart on a bus. The second line lists the bus IDs that are in service according to the shuttle company;
 * entries that show x must be out of service, so you decide to ignore them.
 * <p>
 * To save time once you arrive, your goal is to figure out the earliest bus you can take to the airport. (There will
 * be exactly one such bus.)
 * <p>
 * For example, suppose you have the following notes:
 * <p>
 * 939
 * 7,13,x,x,59,x,31,19
 * Here, the earliest timestamp you could depart is 939, and the bus IDs in service are 7, 13, 59, 31, and 19. Near
 * timestamp 939, these bus IDs depart at the times marked D:
 * <p>
 * time   bus 7   bus 13  bus 59  bus 31  bus 19
 * 929      .       .       .       .       .
 * 930      .       .       .       D       .
 * 931      D       .       .       .       D
 * 932      .       .       .       .       .
 * 933      .       .       .       .       .
 * 934      .       .       .       .       .
 * 935      .       .       .       .       .
 * 936      .       D       .       .       .
 * 937      .       .       .       .       .
 * 938      D       .       .       .       .
 * 939      .       .       .       .       .
 * 940      .       .       .       .       .
 * 941      .       .       .       .       .
 * 942      .       .       .       .       .
 * 943      .       .       .       .       .
 * 944      .       .       D       .       .
 * 945      D       .       .       .       .
 * 946      .       .       .       .       .
 * 947      .       .       .       .       .
 * 948      .       .       .       .       .
 * 949      .       D       .       .       .
 * The earliest bus you could take is bus ID 59. It doesn't depart until timestamp 944, so you would need to wait 944
 * - 939 = 5 minutes before it departs. Multiplying the bus ID by the number of minutes you'd need to wait gives 295.
 *
 * @author chris.jackson
 */
public class Day13
{

    public static final String INVALID_BUS_ID = "x";

    /**
     * Constructor
     */
    public Day13() throws AnswerNotAvailableException
    {
        // Check the logic with the example before calculating answers
        testLogic();

        System.out.println( THE_ANSWER_IS_PT1 + part1( getData() ) );
        System.out.println( THE_ANSWER_IS_PT2 + part2( getData() ) );
    }

    /**
     * --- Part One ---
     * What is the ID of the earliest bus you can take to the airport multiplied by the number of minutes you'll need
     * to wait for that bus?
     * <p>
     * Answer: 246
     *
     * @param data the data to process for the question
     */
    private int part1( final List<String> data ) throws AnswerNotAvailableException
    {
        // The first row is the earliest timestamp for departing
        final int earliestDepartTimestamp = Integer.parseInt( data.get( 0 ) );

        // The next row is the Bus IDs
        final String[] allBuses = data.get( 1 ).split( COMMA );

        final List<Integer> validBuses = getValidBusesAsIntegers( allBuses );

        // Find the earliest bus time that follows the earliest departure
        final Integer[] busDetails = findTime( earliestDepartTimestamp, validBuses );

        // Determine how long we would have to wait
        final int wait = busDetails[ 1 ] - earliestDepartTimestamp;

        // Run the answer (Bus ID * wait)
        return wait * busDetails[ 0 ];
    }

    /**
     * --- Part Two ---
     * t.
     * <p>
     * In this example, the earliest timestamp at which this occurs is 1068781:
     * <p>
     * time     bus 7   bus 13  bus 59  bus 31  bus 19
     * 1068773    .       .       .       .       .
     * 1068774    D       .       .       .       .
     * 1068775    .       .       .       .       .
     * 1068776    .       .       .       .       .
     * 1068777    .       .       .       .       .
     * 1068778    .       .       .       .       .
     * 1068779    .       .       .       .       .
     * 1068780    .       .       .       .       .
     * 1068781    D       .       .       .       .
     * 1068782    .       D       .       .       .
     * 1068783    .       .       .       .       .
     * 1068784    .       .       .       .       .
     * 1068785    .       .       D       .       .
     * 1068786    .       .       .       .       .
     * 1068787    .       .       .       D       .
     * 1068788    D       .       .       .       D
     * 1068789    .       .       .       .       .
     * 1068790    .       .       .       .       .
     * 1068791    .       .       .       .       .
     * 1068792    .       .       .       .       .
     * 1068793    .       .       .       .       .
     * 1068794    .       .       .       .       .
     * 1068795    D       D       .       .       .
     * 1068796    .       .       .       .       .
     * 1068797    .       .       .       .       .
     * In the above example, bus ID 7 departs at timestamp 1068788 (seven minutes after t). This is fine; the only
     * requirement on that minute is that bus ID 19 departs then, and it does.
     * <p>
     * Here are some other examples:
     * <p>
     * The earliest timestamp that matches the list 17,x,13,19 is 3417.
     * 67,7,59,61 first occurs at timestamp 754018.
     * 67,x,7,59,61 first occurs at timestamp 779210.
     * 67,7,x,59,61 first occurs at timestamp 1261476.
     * 1789,37,47,1889 first occurs at timestamp 1202161486.
     * However, with so many bus IDs in your list, surely the actual earliest timestamp will be larger than
     * 100000000000000!
     * <p>
     * What is the earliest timestamp such that all of the listed bus IDs depart at offsets matching their positions
     * in the list?
     * <p>
     * Answer: 939490236001473
     *
     * @param data the data to process for the question
     * @return the answer as a {@link Long}
     */
    private Long part2( final List<String> data )
    {
        /*
         * This calculation can be 'worked out', but the number is, again, so high that it's too much to process.
         * Thus, the advent of 'mathematics' again.  I'm not keen on this as it's more about maths than it is design
         * patterns or code, which is, frankly, attempting to make a maths degree a prerequisite of development.
         */

        final List<String> allBuses = asList( data.get( data.size() - 1 ).split( COMMA ) );

        // Turn the bus data into a long array, with { order, busId }
        final long[][] busData = getBusData( allBuses );

        // Get the product of all the BusIDs
        long product = 1;
        for ( final long[] bus : busData )
        {
            product *= bus[ 1 ];
        }
        final long finalProduct = product;

        // Calculate the cumulative sum
        long sum = 0L;
        for ( final long[] a : busData )
        {
            final long busOrder = a[ 0 ];
            final long busId = a[ 1 ];
            final long remainder = finalProduct / busId;

            // Calculate the sum using the extended Euclidean Algorithm
            sum += busOrder * remainder * invert( remainder, busId );
        }

        // Return the smallest timestamp
        return product - sum % product;
    }


    /**
     * Turns the String array into a List of Integers
     *
     * @param allBuses the String array of buses
     * @return list of busIDs as a Integer List
     */
    private List<Integer> getValidBusesAsIntegers( final String[] allBuses )
    {
        // Disregard any with 'x' and create an Integer list from the valid values
        final List<Integer> validBuses = new ArrayList<>();

        for ( final String bus : allBuses )
        {
            if ( !bus.equalsIgnoreCase( INVALID_BUS_ID ) )
            {
                validBuses.add( Integer.parseInt( bus ) );
            }
        }
        return validBuses;
    }

    /**
     * Find the earliest departure time
     *
     * @param earliestDepartureTimestamp the earliest departure time (answer must be after this)
     * @param validBuses                 the list of valid Bus IDs
     * @return an array with the Bus ID and the departure time of that bus
     */
    private Integer[] findTime( final int earliestDepartureTimestamp, final List<Integer> validBuses )
            throws AnswerNotAvailableException
    {
        /*
         * Find the smallest number of iterations we would need.  This is trivial and just skips a few iterations
         */
        final int smallest =
                ( int ) Math.floor( ( double ) earliestDepartureTimestamp / Collections.max( validBuses ) );

        Integer[] earliestBus = null;

        // Loop until we find the time
        for ( int i = smallest; i < earliestDepartureTimestamp; i++ )
        {
            // Loop through each bus on this iteration
            for ( final Integer bus : validBuses )
            {
                final int time = bus * i;

                /*
                 * If the time for this Bus is after our earliest time, check if it's the earliest out of this set
                 */
                if ( time >= earliestDepartureTimestamp && ( earliestBus == null || time < earliestBus[ 1 ] ) )
                {
                    earliestBus = new Integer[]{ bus, time };
                }
            }
        }
        if ( earliestBus != null )
        {
            // If we've set the 'earliestBus', we have our answer
            return earliestBus;
        }
        throw new AnswerNotAvailableException();
    }


    /**
     * Invert the remainder using the extended euclid algorithm
     *
     * @param remainder the remainder (can be recursive)
     * @param busId     the ID of the bus (how often it repeats)
     * @return the inverted value
     */
    long invert( final long remainder, final long busId )
    {
        if ( remainder != 0 )
        {
            final long modulus = busId % remainder;

            if ( modulus > 0 )
            {
                // Apply extended Euclid Algorithm
                return busId - invert( modulus, remainder ) * busId / remainder;
            }
            return 1;
        }
        return 0;
    }

    /**
     * Turn the bus data into a long array, with { order, busId }
     *
     * @param allBuses the String list of busIDs
     * @return a long array of busIDs and their orders
     */
    private long[][] getBusData( final List<String> allBuses )
    {
        final List<long[]> list = new ArrayList<>();

        for ( final String bus : allBuses )
        {
            if ( !bus.equals( INVALID_BUS_ID ) )
            {
                final long[] buses = new long[]{ allBuses.indexOf( bus ), parseLong( bus ) };
                list.add( buses );
            }
        }
        return list.toArray( new long[ 0 ][] );
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
        final List<String> testData = Arrays.asList( "939", "7,13,x,x,59,x,31,19" );
        assert part1( testData ) == 295 : PART_1_TEST_FAILED;

        /*
         * I've ran these tests with an alternation of both methods
         */
        assert part2methodical( testData ) == 1068781 : PART_2_TEST_FAILED;
        assert part2( Collections.singletonList( "17,x,13,19" ) ) == 3417 : PART_2_TEST_FAILED;
        assert part2methodical( Collections.singletonList( "67,7,59,61" ) ) == 754018 : PART_2_TEST_FAILED;
        assert part2( Collections.singletonList( "67,x,7,59,61" ) ) == 779210 : PART_2_TEST_FAILED;
        assert part2methodical( Collections.singletonList( "67,7,x,59,61" ) ) == 1261476 : PART_2_TEST_FAILED;
        assert part2( Collections.singletonList( "1789,37,47,1889" ) ) == 1202161486 : PART_2_TEST_FAILED;
    }


    /* *************** *
     *  OTHER SOLUTION *
     * *************** */

    /**
     * I've kept this solution because it is more procedural.
     * It isn't the most efficient, but it is the method that people can probably understand and maintain without a
     * background in advanced Maths
     *
     * @param data the data to calculate
     * @return the answer
     */
    private Long part2methodical( final List<String> data )
    {

        final String[] allBuses = data.get( data.size() - 1 ).split( COMMA );
        final SortedMap<Integer, Integer> busMapByOrder = new TreeMap<>();

        for ( int b = 0; b < allBuses.length; b++ )
        {
            if ( !allBuses[ b ].equalsIgnoreCase( INVALID_BUS_ID ) )
            {
                busMapByOrder.put( b, Integer.valueOf( allBuses[ b ] ) );
            }
        }

        // The shortest route to the answer (fewest iterations) is to get calculate the largest Bus ID first
        final Integer largestBusId = Collections.max( getValidBusesAsIntegers( allBuses ) );

        final SortedMap<Integer, Integer> busMap = new TreeMap<>();

        busMap.put( 0, largestBusId );
        int largestBusIdOrder = 0;

        // Find the order ofr the largest Bus ID in the original list
        for ( int n = 0; n < allBuses.length; n++ )
        {
            if ( allBuses[ n ].equalsIgnoreCase( String.valueOf( largestBusId ) ) )
            {
                largestBusIdOrder = n;
                break;
            }
        }


        // Create a new map with the busIDs and their offset from the largest ID
        for ( final Map.Entry<Integer, Integer> bus : busMapByOrder.entrySet() )
        {
            if ( !bus.getValue().equals( largestBusId ) )
            {
                final int offset = bus.getKey() - largestBusIdOrder;
                busMap.put( offset, bus.getValue() );
            }
        }

        long time;
        long earliestBus = 0;

        boolean find = true;

        // Keep looping through iterations of the least frequent bus until we find the pattern
        for ( int i = 1; find; i++ )
        {
            final Integer maxBusId = busMap.get( 0 );
            time = ( ( long ) maxBusId * i );
            earliestBus = time;

            boolean found = true;

            // Loop through each BusID
            for ( final Map.Entry<Integer, Integer> bus : busMap.entrySet() )
            {
                // Offset the bus time for each BusID to get the relative time for that bus
                final long thisBusTime = time + bus.getKey();

                // If the bus time's modulus isn't zero, it isn't a match
                if ( ( thisBusTime % bus.getValue() ) != 0 )
                {
                    found = false;
                    break;
                }
                else
                {
                    // This could be a match, so update the earliest time
                    if ( thisBusTime < earliestBus )
                    {
                        earliestBus = thisBusTime;
                    }
                }
            }

            if ( found )
            {
                // We've found our answer - exit the loop
                find = false;
            }
        }
        return earliestBus;
    }
}
