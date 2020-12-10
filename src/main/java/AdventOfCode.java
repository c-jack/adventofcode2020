/*
 * Copyright (c) 8/12/2020 Chris Jackson (c-jack)
 * adventofcode.AdventOfCode
 */

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import exception.AnswerNotAvailableException;

/**
 * https://adventofcode.com/2020/
 *
 * @author chris.jackson
 */
public class AdventOfCode
{
    final static Map<String, String> selectionMap = new HashMap<>();

    public static void main( final String[] args )
    {
        buildOptions();
        menu();
    }

    /**
     * Build the menu options
     */
    private static void buildOptions()
    {
        selectionMap.put( "1", "Day 1: Report Repair" );
        selectionMap.put( "2", "Day 2: Password Philosophy" );
        selectionMap.put( "3", "Day 3: Toboggan Trajectory" );
        selectionMap.put( "4", "Day 4: Passport Processing" );
        selectionMap.put( "5", "Day 5: Binary Boarding" );
        selectionMap.put( "6", "Day 6: Custom Customs" );
        selectionMap.put( "7", "Day 7: Handy Haversacks" );
        selectionMap.put( "8", "Day 8: Handheld Halting" );
        selectionMap.put( "9", "Day 9: Encoding Error" );
        selectionMap.put( "10", "Day 10: Adapter Array" );
        selectionMap.put( "quit", "Exit" );
    }


    /**
     * Build the menu to display
     */
    public static void menu()
    {
        String selection = "";
        final Scanner input = new Scanner( System.in );

        System.out.println( "                      *\n"
                + "                     /.\\\n"
                + "                    /..'\\\n"
                + "                    /'.'\\\n"
                + "                   /.''.'\\\n"
                + "                   /.'.'.\\\n"
                + "                  /'.''.'.\\\n"
                + "                  ^^^[_]^^^" );
        System.out.println( "|-------------------------------------------|" );
        System.out.println( "|  ~ Advent of Code 2020 : Chris Jackson ~  |" );
        System.out.println( "|   Pick a problem to run the solution for  |" );
        System.out.println( "|-------------------------------------------|" );
        for ( final Map.Entry<String, String> a : selectionMap.entrySet() )
        {
            if ( !a.getKey().equals( "quit" ) )
            {
                System.out.println( "--> (" + a.getKey() + ") " + a.getValue() );
            }
        }
        System.out.println( "|-------------------------------------------|" );
        System.out.println( "|            Type 'quit' to Exit            |" );
        System.out.println( "|-------------------------------------------|" );
        while ( !selection.equals( "quit" ) )
        {
            System.out.print( "Choice: " );
            selection = input.next();

            while ( !selectionMap.containsKey( selection ) )
            {
                System.out.println( "Invalid choice!" );
                System.out.print( "Choice: " );
                selection = input.next();
            }
            getDayAnswers( selection );
        }

        System.out.println( "Bye!" );
    }

    /**
     * Process the chosen menu option
     *
     * @param selection the menu option selected
     */
    private static void getDayAnswers( final String selection )
    {
        try
        {
            switch ( selection )
            {
                case "1":
                    new Day1();
                    break;
                case "2":
                    new Day2();
                    break;
                case "3":
                    new Day3();
                    break;
                case "4":
                    new Day4();
                    break;
                case "5":
                    new Day5();
                    break;
                case "6":
                    new Day6();
                    break;
                case "7":
                    new Day7();
                    break;
                case "8":
                    new Day8();
                    break;
                case "9":
                    new Day9();
                    break;
                case "10":
                    new Day10();
                    break;
                default:
                    throw new AnswerNotAvailableException();
            }
        }
        catch ( final AnswerNotAvailableException e )
        {
            System.out.println( "No answer available for this question!" );
        }
    }
}
