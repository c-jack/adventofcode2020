import static constants.Constants.*;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import exception.AnswerNotAvailableException;

/**
 * Advent of Code 2020
 * Day 2 Part 1
 * <p>
 * --- Day 2: Password Philosophy ---
 * Your flight departs in a few days from the coastal airport; the easiest way down to the coast from here is via
 * toboggan.
 * <p>
 * The shopkeeper at the North Pole Toboggan Rental Shop is having a bad day. "Something's wrong with our computers;
 * we can't log in!" You ask if you can take a look.
 * <p>
 * Their password database seems to be a little corrupted: some of the passwords wouldn't have been allowed by the
 * Official Toboggan Corporate Policy that was in effect when they were chosen.
 * <p>
 * To try to debug the problem, they have created a list (your puzzle input) of passwords (according to the corrupted
 * database) and the corporate policy when that password was set.
 * <p>
 * For example, suppose you have the following list:
 * <p>
 * 1-3 a: abcde
 * 1-3 b: cdefg
 * 2-9 c: ccccccccc
 * Each line gives the password policy and then the password. The password policy indicates the lowest and highest
 * number of times a given letter must appear for the password to be valid. For example, 1-3 a means that the
 * password must contain a at least 1 time and at most 3 times.
 *
 * @author chris.jackson
 */
public class day2
{
    public static final String CORRECT_PASSWORDS_KEY = "correct";
    public static final String INCORRECT_PASSWORDS_KEY = "incorrect";

    /**
     * Constructor
     *
     * @param part part to run
     */
    public day2( final int part ) throws AnswerNotAvailableException
    {
        if ( part == 1 )
        {
            System.out.println( THE_ANSWER_IS + part1() );
        }
        else if ( part == 2 )
        {
            System.out.println( THE_ANSWER_IS + part2() );
        }
    }

    /**
     * In the above example, 2 passwords are valid. The middle password, cdefg, is not; it contains no instances of b,
     * but needs at least 1. The first and third passwords are valid: they contain one a or nine c, both within the
     * limits of their respective policies.
     * <p>
     * How many passwords are valid according to their policies?
     * --------------------------------------------------------------------------------------------------------------
     */
    private int part1()
    {
        return getIncorrectPasswords( 1 ).get( CORRECT_PASSWORDS_KEY ).size();
    }


    /**
     * --- Part Two ---
     * While it appears you validated the passwords correctly, they don't seem to be what the Official Toboggan
     * Corporate Authentication System is expecting.
     * <p>
     * The shopkeeper suddenly realizes that he just accidentally explained the password policy rules from his old
     * job at the sled rental place down the street! The Official Toboggan Corporate Policy actually works a little
     * differently.
     * <p>
     * Each policy actually describes two positions in the password, where 1 means the first character, 2 means the
     * second character, and so on. (Be careful; Toboggan Corporate Policies have no concept of "index zero"!)
     * Exactly one of these positions must contain the given letter. Other occurrences of the letter are irrelevant
     * for the purposes of policy enforcement.
     * <p>
     * Given the same example list from above:
     * <p>
     * 1-3 a: abcde is valid: position 1 contains a and position 3 does not.
     * 1-3 b: cdefg is invalid: neither position 1 nor position 3 contains b.
     * 2-9 c: ccccccccc is invalid: both position 2 and position 9 contain c.
     * <p>
     * How many passwords are valid according to the new interpretation of the policies?
     */
    private int part2()
    {

        return getIncorrectPasswords( 2 ).get( CORRECT_PASSWORDS_KEY ).size();
    }

    /**
     * Iterate through the values to get the passwords that are correct/incorrect
     *
     * @param part the question part whose criteria should be applied
     * @return map of correct and incorrect password lists
     */
    private Map<String, List<String>> getIncorrectPasswords( final int part )
    {
        final Map<String, List<String>> results = new HashMap<>();
        final List<String> correctPasswords = new ArrayList<>();
        final List<String> incorrectPasswords = new ArrayList<>();
        final URL resource = getClass().getClassLoader().getResource( getClass().getName() );
        try
        {
            assert resource != null;
            try ( final Stream<String> stream = Files.lines( Paths.get( resource.getPath() ) ) )
            {
                stream.forEach( line -> {

                    final String numbers = line.substring( 0, line.indexOf( SINGLE_SPACE ) );
                    final int lower = Integer.parseInt( numbers.substring( 0, numbers.indexOf( HYPHEN ) ) );
                    final int upper = Integer.parseInt( numbers.substring( numbers.indexOf( HYPHEN ) + 1 ) );

                    final String letterToCheck =
                            line.substring( line.indexOf( SINGLE_SPACE ) + 1, line.indexOf( ": " ) );
                    final String password = line.substring( line.indexOf( COLON + SINGLE_SPACE ) + 2 );

                    if ( part == 1 )
                    {
                        calculatePart1Validity( correctPasswords, incorrectPasswords, lower, upper, letterToCheck,
                                password );
                    }
                    else if ( part == 2 )
                    {
                        calculatePart2Validity( correctPasswords, incorrectPasswords, lower, upper, letterToCheck,
                                password );
                    }

                } );
            }
        }
        catch ( final IOException e )
        {
            e.printStackTrace();
        }
        results.put( CORRECT_PASSWORDS_KEY, correctPasswords );
        results.put( INCORRECT_PASSWORDS_KEY, incorrectPasswords );
        return results;
    }

    /**
     * Calculates the Part 1 validity
     * The instances of the letterToCheck should be within the upper and lower boundaries
     *
     * @param correctPasswords   list of correct passwords
     * @param incorrectPasswords list of incorrect passwords
     * @param lower              upper boundary
     * @param upper              lower boundary
     * @param letterToCheck      the letter to search for
     * @param password           the password to validate
     */
    private void calculatePart1Validity( final List<String> correctPasswords, final List<String> incorrectPasswords,
                                         final int lower,
                                         final int upper, final String letterToCheck, final String password )
    {
        int count = 0;
        for ( final char letter : password.toCharArray() )
        {
            if ( letterToCheck.charAt( 0 ) == letter )
            {
                count++;
            }
        }

        if ( count < lower || count > upper )
        {
            incorrectPasswords.add( password );
        }
        else
        {
            correctPasswords.add( password );
        }
    }

    /**
     * Calculates the Part 2 validity
     * At each index, only one character should match the letterToCheckfor the password to be valid
     *
     * @param correctPasswords   list of correct passwords
     * @param incorrectPasswords list of incorrect passwords
     * @param lower              upper index (0=1)
     * @param upper              lower index (0=1)
     * @param letterToCheck      the letter to search for
     * @param password           the password to validate
     */
    private void calculatePart2Validity( final List<String> correctPasswords, final List<String> incorrectPasswords,
                                         final int lower,
                                         final int upper, final String letterToCheck, final String password )
    {
        final int lowerIndex = lower - 1;
        final int upperIndex = upper - 1;

        if ( lowerIndex >= 0 && upperIndex <= password.length() )
        {
            // Check the position matches
            final boolean position1match = password.charAt( lowerIndex ) == letterToCheck.charAt( 0 );
            final boolean position2match = password.charAt( upperIndex ) == letterToCheck.charAt( 0 );

            // Only one of the values should match for the password to be valid
            if ( ( position1match || position2match ) && !( position1match && position2match ) )
            {
                correctPasswords.add( password );
                return;
            }
        }
        incorrectPasswords.add( password );
    }
}
