/*
 * Copyright (c) 10/12/2020 Chris Jackson (c-jack)
 * adventofcode.Day10
 */

import static constants.Constants.*;
import static utils.AOCUtils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import exception.AnswerNotAvailableException;


/**
 * Advent of Code 2020
 * Day 10
 * <p>
 * --- Day 10: Adapter Array ---
 * Patched into the aircraft's data port, you discover weather forecasts of a massive tropical storm. Before you can
 * figure out whether it will impact your vacation plans, however, your device suddenly turns off!
 * <p>
 * Its battery is dead.
 * <p>
 * You'll need to plug it in. There's only one problem: the charging outlet near your seat produces the wrong number
 * of jolts. Always prepared, you make a list of all of the joltage adapters in your bag.
 * <p>
 * Each of your joltage adapters is rated for a specific output joltage (your puzzle input). Any given adapter can
 * take an input 1, 2, or 3 jolts lower than its rating and still produce its rated output joltage.
 * <p>
 * In addition, your device has a built-in joltage adapter rated for 3 jolts higher than the highest-rated adapter in
 * your bag. (If your adapter list were 3, 9, and 6, your device's built-in adapter would be rated for 12 jolts.)
 * <p>
 * Treat the charging outlet near your seat as having an effective joltage rating of 0.
 * <p>
 * Since you have some time to kill, you might as well test all of your adapters. Wouldn't want to get to your resort
 * and realize you can't even charge your device!
 * <p>
 * If you use every adapter in your bag at once, what is the distribution of joltage differences between the charging
 * outlet, the adapters, and your device?
 * <p>
 * For example, suppose that in your bag, you have adapters with the following joltage ratings:
 * <p>
 * 16
 * 10
 * 15
 * 5
 * 1
 * 11
 * 7
 * 19
 * 6
 * 12
 * 4
 * With these adapters, your device's built-in joltage adapter would be rated for 19 + 3 = 22 jolts, 3 higher than
 * the highest-rated adapter.
 * <p>
 * Because adapters can only connect to a source 1-3 jolts lower than its rating, in order to use every adapter,
 * you'd need to choose them like this:
 * <p>
 * The charging outlet has an effective rating of 0 jolts, so the only adapters that could connect to it directly
 * would need to have a joltage rating of 1, 2, or 3 jolts. Of these, only one you have is an adapter rated 1 jolt
 * (difference of 1).
 * From your 1-jolt rated adapter, the only choice is your 4-jolt rated adapter (difference of 3).
 * From the 4-jolt rated adapter, the adapters rated 5, 6, or 7 are valid choices. However, in order to not skip any
 * adapters, you have to pick the adapter rated 5 jolts (difference of 1).
 * Similarly, the next choices would need to be the adapter rated 6 and then the adapter rated 7 (with difference of
 * 1 and 1).
 * The only adapter that works with the 7-jolt rated adapter is the one rated 10 jolts (difference of 3).
 * From 10, the choices are 11 or 12; choose 11 (difference of 1) and then 12 (difference of 1).
 * After 12, only valid adapter has a rating of 15 (difference of 3), then 16 (difference of 1), then 19 (difference
 * of 3).
 * Finally, your device's built-in adapter is always 3 higher than the highest adapter, so its rating is 22 jolts
 * (always a difference of 3).
 * In this example, when using every adapter, there are 7 differences of 1 jolt and 5 differences of 3 jolts.
 * <p>
 * Here is a larger example:
 * <p>
 * 28
 * 33
 * 18
 * 42
 * 31
 * 14
 * 46
 * 20
 * 48
 * 47
 * 24
 * 23
 * 49
 * 45
 * 19
 * 38
 * 39
 * 11
 * 1
 * 32
 * 25
 * 35
 * 8
 * 17
 * 7
 * 9
 * 4
 * 2
 * 34
 * 10
 * 3
 * In this larger example, in a chain that uses all of the adapters, there are 22 differences of 1 jolt and 10
 * differences of 3 jolts.
 *
 * @author chris.jackson
 */
public class Day10
{

    /**
     * For the calculative part 2, store the possible permutations in a map so we don't duplicate
     */
    private final Map<Integer, Long> permutations = new HashMap<>();

    /**
     * For storing the list of adapters
     */
    private List<Integer> adapterList = new ArrayList<>();

    /**
     * Constructor
     */
    public Day10() throws AnswerNotAvailableException
    {
        // Check the logic with the example before calculating answers
        testLogic();

        setData();
        System.out.println( THE_ANSWER_IS_PT1 + part1() );

        /*
         *
         */
        setData();
        System.out.println( THE_ANSWER_IS_PT2 + calculatePart2( 0 ) );
    }

    /**
     * --- Part One ---
     * <p>
     * Find a chain that uses all of your adapters to connect the charging outlet to your device's built-in adapter
     * and count the joltage differences between the charging outlet, the adapters, and your device. What is the
     * number of 1-jolt differences multiplied by the number of 3-jolt differences?
     * <p>
     * Answer: 1980
     */
    private int part1() throws AnswerNotAvailableException
    {
        final Map<Integer, List<Integer>> joltageMap = createJoltageMap( adapterList );

        // Calculate the qty of 1 Jolt adapters and multiply it by the qty of 3 Jolt adapters
        final int sumOfOnes = joltageMap.get( 1 ).size();
        final int sumOfThrees = joltageMap.get( 3 ).size();

        return sumOfOnes * sumOfThrees;
    }

    /**
     * --- Part Two ---
     * To completely determine whether you have enough adapters, you'll need to figure out how many different ways
     * they can be arranged. Every arrangement needs to connect the charging outlet to your device. The previous
     * rules about when adapters can successfully connect still apply.
     * <p>
     * The first example above (the one that starts with 16, 10, 15) supports the following arrangements:
     * <p>
     * (0), 1, 4, 5, 6, 7, 10, 11, 12, 15, 16, 19, (22)
     * (0), 1, 4, 5, 6, 7, 10, 12, 15, 16, 19, (22)
     * (0), 1, 4, 5, 7, 10, 11, 12, 15, 16, 19, (22)
     * (0), 1, 4, 5, 7, 10, 12, 15, 16, 19, (22)
     * (0), 1, 4, 6, 7, 10, 11, 12, 15, 16, 19, (22)
     * (0), 1, 4, 6, 7, 10, 12, 15, 16, 19, (22)
     * (0), 1, 4, 7, 10, 11, 12, 15, 16, 19, (22)
     * (0), 1, 4, 7, 10, 12, 15, 16, 19, (22)
     * (The charging outlet and your device's built-in adapter are shown in parentheses.) Given the adapters from the
     * first example, the total number of arrangements that connect the charging outlet to your device is 8.
     * <p>
     * The second example above (the one that starts with 28, 33, 18) has many arrangements. Here are a few:
     * <p>
     * (0), 1, 2, 3, 4, 7, 8, 9, 10, 11, 14, 17, 18, 19, 20, 23, 24, 25, 28, 31,
     * 32, 33, 34, 35, 38, 39, 42, 45, 46, 47, 48, 49, (52)
     * <p>
     * (0), 1, 2, 3, 4, 7, 8, 9, 10, 11, 14, 17, 18, 19, 20, 23, 24, 25, 28, 31,
     * 32, 33, 34, 35, 38, 39, 42, 45, 46, 47, 49, (52)
     * <p>
     * (0), 1, 2, 3, 4, 7, 8, 9, 10, 11, 14, 17, 18, 19, 20, 23, 24, 25, 28, 31,
     * 32, 33, 34, 35, 38, 39, 42, 45, 46, 48, 49, (52)
     * <p>
     * (0), 1, 2, 3, 4, 7, 8, 9, 10, 11, 14, 17, 18, 19, 20, 23, 24, 25, 28, 31,
     * 32, 33, 34, 35, 38, 39, 42, 45, 46, 49, (52)
     * <p>
     * (0), 1, 2, 3, 4, 7, 8, 9, 10, 11, 14, 17, 18, 19, 20, 23, 24, 25, 28, 31,
     * 32, 33, 34, 35, 38, 39, 42, 45, 47, 48, 49, (52)
     * <p>
     * (0), 3, 4, 7, 10, 11, 14, 17, 20, 23, 25, 28, 31, 34, 35, 38, 39, 42, 45,
     * 46, 48, 49, (52)
     * <p>
     * (0), 3, 4, 7, 10, 11, 14, 17, 20, 23, 25, 28, 31, 34, 35, 38, 39, 42, 45,
     * 46, 49, (52)
     * <p>
     * (0), 3, 4, 7, 10, 11, 14, 17, 20, 23, 25, 28, 31, 34, 35, 38, 39, 42, 45,
     * 47, 48, 49, (52)
     * <p>
     * (0), 3, 4, 7, 10, 11, 14, 17, 20, 23, 25, 28, 31, 34, 35, 38, 39, 42, 45,
     * 47, 49, (52)
     * <p>
     * (0), 3, 4, 7, 10, 11, 14, 17, 20, 23, 25, 28, 31, 34, 35, 38, 39, 42, 45,
     * 48, 49, (52)
     * In total, this set of adapters can connect the charging outlet to your device in 19208 distinct arrangements.
     * <p>
     * You glance back down at your bag and try to remember why you brought so many adapters; there must be more than
     * a trillion valid ways to arrange them! Surely, there must be an efficient way to count the arrangements.
     * <p>
     * What is the total number of distinct ways you can arrange the adapters to connect the charging outlet to your
     * device?
     * <p>
     * Answer: 4628074479616
     */
    private long part2() throws AnswerNotAvailableException
    {
        /*
         * NOTE:  I was quite stubborn with this one, and wanted to 'process' the answer, rather than just calculate
         * it with an algorithm or formula.
         * As a result, this one takes a while to process.  It's unusable for the proper data for part 2.
         *
         * The permutations are stored as Strings of comma-separated values, because Integer Lists were just taking
         * forever to process.
         * It's still quite a lethargic class, but learns towards exercising some development principles, rather than
         * resulting in being an elaborate mathematics question.
         */

        // Make a Set to store the permutations.
        final Set<String> permutations = new HashSet<>();

        // Calculate the maximum value and create the list of adapter jolt values
        final Integer maxJoltage = calculateMaxJoltage( adapterList );
        final List<Integer> adapters = createSortedAdapterListWithMax( adapterList, maxJoltage );


        // Create the most verbose permutation that uses all the values
        final String fullList = adapters.stream().map( String::valueOf ).collect( Collectors.joining( COMMA ) );

        // Add this to the list as the first value
        permutations.add( fullList );


        /*
         * Permutations will vary where 'gaps' exist that other adapters can be used between - e.g. where the gap is
         * greater than 1 jolt.
         */
        final Map<Integer, List<Integer>> joltageMap = createJoltageMap( adapterList );

        // Return the list of gaps that are in jumps of 3
        final List<Integer> gaps = joltageMap.get( 3 );

        // Calculate all the numbers that are potentially variable in presence
        final List<Integer> variableNumbers = calculateAllVariableNumbers( adapters, joltageMap, gaps );

        // Reduce the list to distinct, sorted numbers
        final SortedSet<Integer> sortedVariables = new TreeSet<>( variableNumbers );

        // Loop over each variable number
        for ( int i = 0; i < sortedVariables.size(); i++ )
        {
            // We need to iterate through a copy of the permutations to avoid concurrency issues
            final ArrayList<String> iteratedPermutations = new ArrayList<>( permutations );

            for ( final String permutation : iteratedPermutations )
            {
                // We now need to remove all the permutations of these singular gaps
                permutations.addAll( removeEach( sortedVariables, permutation ) );

                // Create a version with all removed (where possible)
                final String allRemoved = removeAll( sortedVariables, permutation );

                // Add the 'all removed' version
                permutations.add( allRemoved );

                // Convert that list into a list of ints
                final List<Integer> allRemovedVariablesList =
                        convertStringListToIntList( Arrays.asList( allRemoved.split( COMMA ) ) );

                // Add a version where only one of each is added to the all removed list
                permutations.addAll( addEachToAllRemoved( sortedVariables, allRemovedVariablesList ) );
            }
        }
        // Return the size
        return permutations.size();
    }


    /**
     * This solution to Part 2 is the only way we can calculate the larger datasets due to the amount of memory
     * required to store 4,628,074,479,616 list items!
     * <p>
     * In this version, we step over each position in the data (pre-sorted).
     * From this position we'll iterate again until we get to a point where the number in the following position is
     * 3 or less away,
     * determine the variations that can exist.
     * <p>
     * Answer: 4628074479616
     *
     * @param dataPosition the position in the data
     * @return number of variations in this iteration
     */
    private long calculatePart2( final int dataPosition )
    {
        // We'll count the possible permutations from this position
        long possiblePermutations = 0;

        // If this iteration is the last one, there's only 1 possible variation
        if ( dataPosition == ( adapterList.size() - 1 ) )
        {
            return 1;
        }

        // If we've already calculated this iteration, just return the answer
        if ( permutations.containsKey( dataPosition ) )
        {
            return permutations.get( dataPosition );
        }


        // Loop through every permutation from this position onwards...
        for ( int j = dataPosition + 1; j < adapterList.size(); j++ )
        {
            // ...until the next number i more than 3 away
            if ( adapterList.get( j ) - adapterList.get( dataPosition ) > 3 )
            {
                break;
            }

            // otherwise, loop over the next
            possiblePermutations += calculatePart2( j );
        }

        // add the permutation to the map
        permutations.put( dataPosition, possiblePermutations );
        return possiblePermutations;
    }

    /**
     * Adds each value to the list that has all of them removed
     *
     * @param sortedVariables the variables to add
     * @param allRemovedList  the list
     * @return the new permutations
     */
    private List<String> addEachToAllRemoved( final SortedSet<Integer> sortedVariables,
                                              final List<Integer> allRemovedList )
    {
        final List<String> newPermutations = new ArrayList<>();

        // Add a version with each removed
        for ( final Integer variable : sortedVariables )
        {
            final SortedSet<Integer> strings = new TreeSet<>( allRemovedList );
            strings.add( variable );

            newPermutations.add( strings.stream().map( String::valueOf ).collect( Collectors.joining( COMMA ) ) );
        }
        return newPermutations;
    }

    /**
     * Removes all the provided variables from the permutation, if/where valid to do so
     *
     * @param sortedVariables the list of variables to remove
     * @param permutation     the permutation as a String pattern
     * @return the modified permutation as a String pattern
     */
    private String removeAll( final SortedSet<Integer> sortedVariables,
                              final String permutation )
    {
        List<Integer> integerList = convertStringListToIntList( Arrays.asList( permutation.split( COMMA ) ) );

        // Remove all the values where valid
        for ( final Integer variable : sortedVariables )
        {
            integerList = removeIfValid( integerList, variable );
        }

        // Add this permutation
        return integerList.stream().map( String::valueOf ).collect( Collectors.joining( COMMA ) );
    }

    /**
     * Removes each of the provided variables from the permutation, creating distinct permutations, if/where valid to
     * do so
     *
     * @param sortedVariables the list of variables to remove
     * @param permutation     the permutation as a String pattern
     * @return the new permutations
     */
    private List<String> removeEach( final SortedSet<Integer> sortedVariables,
                                     final String permutation )
    {
        final List<String> newPermutations = new ArrayList<>();

        // Convert the String list to an Integer list
        final List<Integer> permutationAsIntegerList =
                convertStringListToIntList( Arrays.asList( permutation.split( COMMA ) ) );

        // Loop through each variable and remove it from a copy of the permutation, if valid
        for ( final Integer variable : sortedVariables )
        {
            final ArrayList<Integer> patternList = removeIfValid( permutationAsIntegerList, variable );

            final String collect = patternList.stream().map( String::valueOf ).collect( Collectors.joining( COMMA ) );

            newPermutations.add( collect );
        }

        return newPermutations;
    }

    /**
     * Removes the provided value if it is valid to do so.
     * Validity is judged by whether removal of the value would create a gap between the value before and the value
     * after of 3 or less.
     *
     * @param permutations  the existing list of permutations
     * @param valueToRemove the value to remove (if valid)
     * @return the amended list
     */
    private ArrayList<Integer> removeIfValid( final List<Integer> permutations, final Integer valueToRemove )
    {
        final ArrayList<Integer> integerList = new ArrayList<>( permutations );

        // check the gap between this number and the next
        final int xPos = integerList.indexOf( valueToRemove );
        if ( xPos > 0 && xPos < integerList.size() - 1 )
        {
            final int gapIfRemoved = integerList.get( xPos + 1 ) - integerList.get( xPos - 1 );

            if ( gapIfRemoved < 4 )
            {
                // remove the value
                integerList.remove( valueToRemove );
            }
        }
        return integerList;
    }

    /**
     * Get all of the numbers in the adapter list which could potentially be omitted from the list in various
     * permutations
     *
     * @param adapters   the list of adapters
     * @param joltageMap the map of Joltage increments
     * @param gaps       the list of 'gaps' represented by each jump of 3
     * @return list of all numbers that exist between the increments of 3
     */
    private List<Integer> calculateAllVariableNumbers( final List<Integer> adapters,
                                                       final Map<Integer, List<Integer>> joltageMap,
                                                       final List<Integer> gaps )
    {
        final List<Integer> oneGaps = new ArrayList<>();

        for ( final Integer gap : gaps )
        {
            if ( gaps.indexOf( gap ) < gaps.indexOf( Collections.max( gaps ) ) )
            {
                final Integer upper = gaps.get( gaps.indexOf( gap ) + 1 );

                oneGaps.addAll( getAdaptersBetweenGaps( gap, upper, adapters ) );

            }
        }
        oneGaps.addAll( getSingleIncrements( adapters, joltageMap ) );
        return oneGaps;
    }


    /**
     * Gets a list of values that exist between the two provided values
     *
     * @param lower    the lower value to use
     * @param upper    the upper value to use
     * @param adapters the list of Integer Joltage adapters
     * @return Joltage adapters between the two values
     */
    private List<Integer> getAdaptersBetweenGaps( final Integer lower, final Integer upper,
                                                  final List<Integer> adapters )
    {
        final int lowerIndex = adapters.indexOf( lower ) + 1;
        final int upperIndex = adapters.indexOf( upper ) - 1;

        final List<Integer> values = new ArrayList<>();

        for ( int i = lowerIndex; i < upperIndex; i++ )
        {
            values.add( adapters.get( i ) );
        }
        return values;
    }


    /**
     * Create and return a list of variable that have single increments
     *
     * @param adapters   the list of Integer Joltage adapters
     * @param joltageMap the map of Joltage increments
     * @return list of variable that have single increments
     */
    private List<Integer> getSingleIncrements( final List<Integer> adapters,
                                               final Map<Integer, List<Integer>> joltageMap )
    {
        // Get the list of Joltages that make jumps of 1
        final List<Integer> listOfOnes = joltageMap.get( 1 );

        final List<Integer> singleIncrementVariables = new ArrayList<>();

        for ( final Integer gap : listOfOnes )
        {
            // get the value for this in the list
            final int thisGap = adapters.indexOf( gap );

            if ( thisGap < adapters.size() )
            {
                final int previousVal = adapters.get( adapters.indexOf( gap ) - 1 );
                final int nextVal = adapters.get( adapters.indexOf( gap ) + 1 );
                if ( ( nextVal - previousVal ) <= 3 )
                {
                    singleIncrementVariables.add( gap );
                }
            }
        }
        return singleIncrementVariables;
    }


    /**
     * Creates a Map of Joltage increments and the associate values
     *
     * @param data the data to use to create the map
     * @return the Map of Joltages
     * @throws AnswerNotAvailableException if something is wrong with the logic
     */
    private Map<Integer, List<Integer>> createJoltageMap( final List<Integer> data ) throws AnswerNotAvailableException
    {
        // Define the starting and the maximum Joltage
        final int startingJoltage = 0;
        final int maxJoltage = calculateMaxJoltage( data );

        final List<Integer> adapterList = createSortedAdapterListWithMax( data, maxJoltage );

        // Create a Map to track the Joltage jumps and adapters
        final Map<Integer, List<Integer>> joltageMap = new HashMap<>();

        /*
         * We'll now loop through our list of adapters, starting at the starting Joltage of 0.
         * The loop should continue until we reach the penultimate adapter (-3 from the last, which is the max we added)
         */
        int currentJoltage = startingJoltage;

        while ( currentJoltage <= ( maxJoltage - 3 ) )
        {
            // Look at all the adapters to find which ones can be used
            final Map<Integer, List<Integer>> compatibleAdapters = findUsableAdapters( adapterList, currentJoltage );

            // We should have at least one
            if ( !compatibleAdapters.isEmpty() )
            {
                int connectingJoltage = 0;

                // Start looking for 1 Joltage jump adapters
                for ( int j = 1; j <= 3; j++ )
                {
                    final List<Integer> adapters = compatibleAdapters.get( j );

                    if ( adapters != null && !adapters.isEmpty() )
                    {
                        // We've found one that's compatible, so get it stop checking
                        connectingJoltage = adapters.get( 0 );

                        // Add it to the Joltage Map that stores the list of adapters
                        joltageMap.put( j, addAdapterToMapList( joltageMap, connectingJoltage, j ) );
                        break;
                    }
                }

                // Just in case we didn't find any in the loop (which will drop to the exception
                if ( connectingJoltage > 0 )
                {
                    // Set the 'currentJoltage' to the adapter's rating and continue the loop
                    currentJoltage = connectingJoltage;
                    continue;
                }
            }
            // If we can't find any compatible adapters, something is wrong with the logic
            throw new AnswerNotAvailableException();
        }
        return joltageMap;
    }

    /**
     * Creates a sorted list of the data, and adds the 'max' value to the list (list max+3)
     *
     * @param data       the data to sort and append
     * @param maxJoltage the target maximum joltage to append
     * @return the sorted, appended list
     */
    private List<Integer> createSortedAdapterListWithMax( final List<Integer> data, final int maxJoltage )
    {
        /*
         * Create a new list from the data and add the maximum Joltage to it.
         * The max Joltage isn't in our list but counts as the last '3' jump
         */
        final List<Integer> adapterList = new ArrayList<>( data );

        // add the '0' value in
        adapterList.add( 0 );

        // Sort the list in order to optimise the processing
        Collections.sort( adapterList );
        adapterList.add( maxJoltage );
        return adapterList;
    }

    /**
     * Finds a usable adapter.
     * This will be an adapter within the 0-4 joltage range of the current Joltage
     *
     * @param adapterList    the list of adapters we can use
     * @param currentJoltage the current Joltage we're at
     * @return a Map containing the usable adapters, with the Joltage difference as the key
     */
    private Map<Integer, List<Integer>> findUsableAdapters( final List<Integer> adapterList, final int currentJoltage )
    {
        final Map<Integer, List<Integer>> candidates = new HashMap<>();
        for ( final Integer adapter : adapterList )
        {
            // Calculate the different between this adapter's Joltage and the current Joltage value
            final int joltDiff = adapter - currentJoltage;

            // It can only be used if it's within 3 Jolts inclusive
            if ( joltDiff > 0 && joltDiff <= 3 )
            {
                candidates.put( joltDiff, addAdapterToMapList( candidates, adapter, joltDiff ) );
            }
        }
        return candidates;
    }

    /**
     * Add the adapter to the cumulative Joltage map by appending the adapter to the list for this 'jump' (diff)
     *
     * @param joltageMap the list of existing Joltage values used for this joltDiff
     * @param adapter    the Adapter to add to the map
     * @param joltDiff   the difference in Joltage to the last adapter
     * @return the modified list
     */
    private List<Integer> addAdapterToMapList( final Map<Integer, List<Integer>> joltageMap,
                                               final Integer adapter,
                                               final int joltDiff )
    {
        final List<Integer> adapters = new ArrayList<>();
        final List<Integer> existingValueList = joltageMap.get( joltDiff );
        if ( existingValueList != null )
        {
            adapters.addAll( existingValueList );
        }
        adapters.add( adapter );
        return adapters;
    }


    /**
     * Get the data for the question.
     * This is set to the {@link #adapterList} value
     */
    private void setData()
    {
        final List<Integer> adapters = new ArrayList<>();

        adapters.add( 0 );
        adapters.addAll( getIntegerData( getClass().getName() ) );

        // In this solution, the ordering is important
        Collections.sort( adapters );

        adapterList = adapters;
    }


    /**
     * Calculates the final Joltage step
     * This is the largest adapter in our set, plus 3
     *
     * @param adapterList the list of int adapter Joltage ratings
     * @return the maximum Joltage rating from the available adapters
     */
    private Integer calculateMaxJoltage( final List<Integer> adapterList )
    {
        return Collections.max( adapterList ) + 3;
    }

    /* *************** *
     *     TESTS       *
     * *************** */

    /**
     * Checks the logic against the examples in the question.
     */
    private void testLogic() throws AnswerNotAvailableException
    {
        final List<Integer> testData = Arrays.asList( 16,
                10,
                15,
                5,
                1,
                11,
                7,
                19,
                6,
                12,
                4 );
        final List<Integer> largerTestData = Arrays.asList( 28,
                33,
                18,
                42,
                31,
                14,
                46,
                20,
                48,
                47,
                24,
                23,
                49,
                45,
                19,
                38,
                39,
                11,
                1,
                32,
                25,
                35,
                8,
                17,
                7,
                9,
                4,
                2,
                34,
                10,
                3 );
        assert calculateMaxJoltage( testData ) == 22 : PART_1_TEST_FAILED;

        adapterList = largerTestData;
        assert part1() == 220 : PART_1_TEST_FAILED;

        adapterList = testData;
        assert part2() == 8 : PART_2_TEST_FAILED;

        adapterList = largerTestData;
        assert part2() == 19208 : PART_2_TEST_FAILED;
    }
}
