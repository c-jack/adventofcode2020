import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import exception.AnswerNotAvailableException;

/**
 * https://adventofcode.com/2020/
 *
 * @author chris.jackson
 */
public class adventOfCode
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
        selectionMap.put( "1", "Day 1: Report Repair - Part 1" );
        selectionMap.put( "2", "Day 1: Report Repair - Part 2" );
        selectionMap.put( "3", "Day 2: Password Philosophy - Part 1" );
        selectionMap.put( "4", "Day 2: Password Philosophy - Part 2" );
        selectionMap.put( "5", "Day 3: Toboggan Trajectory - Part 1" );
        selectionMap.put( "6", "Day 3: Toboggan Trajectory - Part 2" );
        selectionMap.put( "7", "Day 4: Toboggan Trajectory - Part 1" );
        selectionMap.put( "8", "Day 4: Toboggan Trajectory - Part 2" );
        selectionMap.put( "quit", "Exit" );
    }


    /**
     * Build the menu to display
     */
    public static void menu()
    {
        String selection = "";
        final Scanner input = new Scanner( System.in );

        System.out.println( "             *\n"
                + "            /.\\\n"
                + "           /..'\\\n"
                + "           /'.'\\\n"
                + "          /.''.'\\\n"
                + "          /.'.'.\\\n"
                + "         /'.''.'.\\\n"
                + "         ^^^[_]^^^" );
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
            getOption( selection );
        }

        System.out.println( "Bye!" );
    }

    /**
     * Process the chosen menu option
     *
     * @param selection the menu option selected
     */
    private static void getOption( final String selection )
    {
        try
        {
            switch ( selection )
            {
                case "1":
                    new day1( 1 );
                    break;
                case "2":
                    new day1( 2 );
                    break;
                case "3":
                    new day2( 1 );
                    break;
                case "4":
                    new day2( 2 );
                    break;
                case "5":
                    new day3( 1 );
                    break;
                case "6":
                    new day3( 2 );
                    break;
                case "7":
                    new day4( 1 );
                    break;
                case "8":
                    new day4( 2 );
                    break;
            }
        }
        catch ( final AnswerNotAvailableException e )
        {
            System.out.println( "No answer available for this question!" );
        }
    }
}
