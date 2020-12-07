/*
 * Copyright (c) 5/12/2020 Chris Jackson (c-jack)
 * adventofcode.Template
 */

import static constants.Constants.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import exception.AnswerNotAvailableException;
import utils.AOCUtils;


/**
 * Advent of Code 2020
 * Day 7
 * <p>
 * --- Day 7: Handy Haversacks ---
 * You land at the regional airport in time for your next flight. In fact, it looks like you'll even have time to
 * grab some food: all flights are currently delayed due to issues in luggage processing.
 * <p>
 * Due to recent aviation regulations, many rules (your puzzle input) are being enforced about bags and their
 * contents; bags must be color-coded and must contain specific quantities of other color-coded bags. Apparently,
 * nobody responsible for these regulations considered how long they would take to enforce!
 * <p>
 * For example, consider the following rules:
 * <p>
 * light red bags contain 1 bright white bag, 2 muted yellow bags.
 * dark orange bags contain 3 bright white bags, 4 muted yellow bags.
 * bright white bags contain 1 shiny gold bag.
 * muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.
 * shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.
 * dark olive bags contain 3 faded blue bags, 4 dotted black bags.
 * vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.
 * faded blue bags contain no other bags.
 * dotted black bags contain no other bags.
 * These rules specify the required contents for 9 bag types. In this example, every faded blue bag is empty, every
 * vibrant plum bag contains 11 bags (5 faded blue and 6 dotted black), and so on.
 * <p>
 * You have a shiny gold bag. If you wanted to carry it in at least one other bag, how many different bag colors
 * would be valid for the outermost bag? (In other words: how many colors can, eventually, contain at least one shiny
 * gold bag?)
 * <p>
 * In the above rules, the following options would be available to you:
 * <p>
 * A bright white bag, which can hold your shiny gold bag directly.
 * A muted yellow bag, which can hold your shiny gold bag directly, plus some other bags.
 * A dark orange bag, which can hold bright white and muted yellow bags, either of which could then hold your shiny
 * gold bag.
 * A light red bag, which can hold bright white and muted yellow bags, either of which could then hold your shiny
 * gold bag.
 * So, in this example, the number of bag colors that can eventually contain at least one shiny gold bag is 4.
 *
 * @author chris.jackson
 */
public class Day7
{

    public static final String TARGET_COLOUR = "shiny gold";

    /**
     * Constructor
     */
    public Day7() throws AnswerNotAvailableException
    {
        // Check the logic with the example before calculating answers
        testLogic();

        System.out.println( THE_ANSWER_IS_PT1 + part1( getData() ) );
        System.out.println( THE_ANSWER_IS_PT2 + part2( getData() ) );
    }

    /**
     * --- Part One ---
     * How many bag colors can eventually contain at least one shiny gold bag?
     * <p>
     * Answer: 337
     *
     * @param data the data to process for the question
     */
    private int part1( final List<String> data ) throws AnswerNotAvailableException
    {
        return getAnswer( data, 1 );
    }

    /**
     * --- Part Two ---
     * It's getting pretty expensive to fly these days - not because of ticket prices, but because of the ridiculous
     * number of bags you need to buy!
     * <p>
     * Consider again your shiny gold bag and the rules from the above example:
     * <p>
     * faded blue bags contain 0 other bags.
     * dotted black bags contain 0 other bags.
     * vibrant plum bags contain 11 other bags: 5 faded blue bags and 6 dotted black bags.
     * dark olive bags contain 7 other bags: 3 faded blue bags and 4 dotted black bags.
     * So, a single shiny gold bag must contain 1 dark olive bag (and the 7 bags within it) plus 2 vibrant plum bags
     * (and the 11 bags within each of those): 1 + 1*7 + 2 + 2*11 = 32 bags!
     * <p>
     * Of course, the actual rules have a small chance of going several levels deeper than this example; be sure to
     * count all of the bags, even if the nesting becomes topologically impractical!
     * <p>
     * Here's another example:
     * <p>
     * shiny gold bags contain 2 dark red bags.
     * dark red bags contain 2 dark orange bags.
     * dark orange bags contain 2 dark yellow bags.
     * dark yellow bags contain 2 dark green bags.
     * dark green bags contain 2 dark blue bags.
     * dark blue bags contain 2 dark violet bags.
     * dark violet bags contain no other bags.
     * In this example, a single shiny gold bag must contain 126 other bags.
     * <p>
     * How many individual bags are required inside your single shiny gold bag?
     * <p>
     * Answer: 50100
     *
     * @param data the data to process for the question
     */
    private long part2( final List<String> data ) throws AnswerNotAvailableException
    {
        return getAnswer( data, 2 );
    }

    /**
     * Calculate the answer based on the logic for the relevant part
     *
     * @param bagData the data to use
     * @param part    the question part whose logic should be used
     * @return the relevant int answer for the question part
     * @throws AnswerNotAvailableException if an invalid 'part' is provided
     */
    private int getAnswer( final List<String> bagData, final int part ) throws AnswerNotAvailableException
    {
        // Start a new set to record the distinct bag colours
        final Set<String> distinctColours = new HashSet<>();

        // Create the rules from the bagData
        final Map<String, List<String[]>> bagRules = createBagRules( bagData );

        // Set the bag colour we're going to ultimately count
        final String targetColour = TARGET_COLOUR;

        /*
         * Get the answer for Part 1:
         * --------------------------
         * How many bag colors can eventually contain at least one shiny gold bag?
         */
        if ( part == 1 )
        {
            // Return all the bags that contain a shiny gold bag
            final List<String> parentColours = checkForColour( targetColour, bagRules );
            distinctColours.addAll( parentColours );

            // Check the distinct bags each bag is in, starting with those above
            checkBags( distinctColours, parentColours, bagRules );

            return distinctColours.size();
        }
        /*
         * Get the answer for Part 2:
         * --------------------------
         * How many individual bags are required inside your single shiny gold bag?
         */
        else if ( part == 2 )
        {
            // Create the Shiny Gold Bag as an Object
            final Bag shinyGoldBag = new Bag( targetColour );

            // Add all the contents for the shiny gold bag
            shinyGoldBag.addBags( getBagContents( bagRules.get( targetColour ) ) );

            // Iterate through all of those contents
            final HashMap<Bag, Integer> bagsToIterate = new HashMap<>( shinyGoldBag.contents );

            // Construct the bag as objects
            constructBagObjects( bagRules, bagsToIterate );

            return shinyGoldBag.getSize();
        }
        throw new AnswerNotAvailableException();
    }

    /**
     * Construct the Bags hierarchy using {@link Bag} objects
     *
     * @param bagRules      the list of colour rules
     * @param bagsToIterate a Map of bags to iterate over
     */
    private void constructBagObjects( final Map<String, List<String[]>> bagRules, HashMap<Bag, Integer> bagsToIterate )
    {
        while ( bagsToIterate.size() > 0 )
        {
            final HashMap<Bag, Integer> newBagsToIterate = new HashMap<>();
            for ( final Map.Entry<Bag, Integer> bag : bagsToIterate.entrySet() )
            {
                bag.getKey().addBags( getBagContents( bagRules.get( bag.getKey().colour ) ) );
                newBagsToIterate.putAll( bag.getKey().contents );
            }
            bagsToIterate = new HashMap<>( newBagsToIterate );
        }
    }

    /**
     * Used in Part 2
     * Determines the contents of the bag from the array
     *
     * @param contentsArray the array of String contents
     * @return converted map containing a {@link Bag} and the qty of said bags
     */
    private Map<Bag, Integer> getBagContents( final List<String[]> contentsArray )
    {
        final Map<Bag, Integer> map = new HashMap<>();
        for ( final String[] contents : contentsArray )
        {
            map.put( new Bag( contents[ 1 ] ), Integer.valueOf( contents[ 0 ] ) );
        }

        return map;
    }

    /**
     * Used in Part 1
     * Builds a list of containing colours
     *
     * @param distinctColours the list of cumulative distinct colours
     * @param parentColours   the list of colours to traverse
     * @param bagRules        the list of colour rules
     */
    private void checkBags( final Set<String> distinctColours, final List<String> parentColours,
                            final Map<String, List<String[]>> bagRules )
    {
        List<String> containingColours = new ArrayList<>( parentColours );

        while ( containingColours.size() > 0 )
        {
            containingColours =
                    new ArrayList<>( checkForContainingColour( distinctColours, bagRules, containingColours ) );
        }
    }

    /**
     * Checks the rules for the targetColour
     *
     * @param targetColour the colour to check for
     * @param bagRules     the list of colour rules
     * @return list of colours that contain the target colour
     */
    private List<String> checkForColour( final String targetColour, final Map<String, List<String[]>> bagRules )
    {
        final List<String> coloursToAdd = new ArrayList<>();
        for ( final Map.Entry<String, List<String[]>> colour : bagRules.entrySet() )
        {
            for ( final String[] colours : colour.getValue() )
            {
                final String colourRule = colours[ 1 ];
                if ( colourRule.equals( targetColour ) )
                {
                    coloursToAdd.add( colour.getKey() );
                }
            }
        }
        return coloursToAdd;
    }

    /**
     * Iterates through a pre-existing list to check for containing colours
     *
     * @param distinctColours the list of cumulative distinct colours
     * @param bagRules        the list of colour rules
     * @return distinct list of colours to check next
     */
    private Set<String> checkForContainingColour( final Set<String> distinctColours,
                                                  final Map<String, List<String[]>> bagRules,
                                                  final List<String> coloursToCheck )
    {
        // Collect all the parent bags for each colour bag checked
        final Set<String> containingColours = new HashSet<>();

        for ( final String targetColour : coloursToCheck )
        {
            containingColours.addAll( checkForColour( targetColour, bagRules ) );
        }

        // Remove any that already have been checked
        containingColours.removeAll( distinctColours );

        // Add the checked/found parents to the distinct list
        distinctColours.addAll( containingColours );

        // We will check this new list next
        return containingColours;
    }

    /**
     * Iterates through the rules to build the bag rules
     *
     * @param data the data containing the rules
     * @return map of bag colours and contents
     */
    private Map<String, List<String[]>> createBagRules( final List<String> data )
    {
        final Map<String, List<String[]>> rulesMap = new HashMap<>();
        for ( final String bagRule : data )
        {
            // Remove the words 'bag' or 'bags'
            final String removeBags = bagRule.replaceAll( "\\b(bag|bags)\\b|\\.", EMPTY_STRING );

            // Separate the parent from the children via a split on the word 'contain'
            final List<String> list = Arrays.asList( removeBags.split( "contain" ) );

            // This means we now have parent/child (key/value)
            final String key = list.get( 0 );
            final String value = list.get( 1 );

            // With all other noise gone, each child can be found via a separating comma
            final String[] rules = value.split( COMMA );

            // Now we'll count how many bags of each colour there are by splitting via whitespace
            final List<String[]> colourList = new ArrayList<>();
            for ( final String rule : rules )
            {
                final String trimmed = rule.trim();
                if ( !trimmed.equals( "no other" ) )
                {
                    final String qty = trimmed.substring( 0, trimmed.indexOf( ' ' ) );
                    final String color = trimmed.substring( trimmed.indexOf( ' ' ) + 1 );
                    colourList.add( new String[]{ qty, color } );
                }
            }

            // Add the value to the list
            rulesMap.put( key.trim(), colourList );
        }

        return rulesMap;
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
     * Object to define a 'bag'
     */
    public static class Bag
    {
        private final String colour;
        private final Map<Bag, Integer> contents = new HashMap<>();

        /**
         * Constructor
         *
         * @param colour the bag colour
         */
        public Bag( final String colour )
        {
            this.colour = colour;
        }

        /**
         * Calculates the size of the bag from this level down
         *
         * @return the size of the bag
         */
        public int getSize()
        {
            int size = 0;
            for ( final Map.Entry<Bag, Integer> bagEntry : contents.entrySet() )
            {
                size += bagEntry.getValue() + ( bagEntry.getValue() * bagEntry.getKey().getSize() );
            }
            return size;
        }

        /**
         * Adds bags to the bag
         *
         * @param bags the map of bags to add
         */
        public void addBags( final Map<Bag, Integer> bags )
        {
            contents.putAll( bags );
        }
    }


    /* *************** *
     *     TESTS       *
     * *************** */

    /**
     * Checks the logic against the examples in the question.
     */
    private void testLogic() throws AnswerNotAvailableException
    {
        final List<String> exampleDataPt1 =
                Arrays.asList( "light red bags contain 1 bright white bag, 2 muted yellow bags.",
                        "dark orange bags contain 3 bright white bags, 4 muted yellow bags.",
                        "bright white bags contain 1 shiny gold bag.",
                        "muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.",
                        "shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.",
                        "dark olive bags contain 3 faded blue bags, 4 dotted black bags.",
                        "vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.",
                        "faded blue bags contain no other bags.",
                        "dotted black bags contain no other bags." );
        assert getAnswer( exampleDataPt1, 1 ) == 4 : PART_1_TEST_FAILED;
        assert getAnswer( exampleDataPt1, 2 ) == 32 : PART_2_TEST_FAILED;

        final List<String> exampleDataPt2 = Arrays.asList( "shiny gold bags contain 2 dark red bags.",
                "dark red bags contain 2 dark orange bags.",
                "dark orange bags contain 2 dark yellow bags.",
                "dark yellow bags contain 2 dark green bags.",
                "dark green bags contain 2 dark blue bags.",
                "dark blue bags contain 2 dark violet bags.",
                "dark violet bags contain no other bags." );
        assert getAnswer( exampleDataPt2, 2 ) == 126 : PART_2_TEST_FAILED;
    }

}
