import static constants.Constants.*;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import exception.AnswerNotAvailableException;


/**
 * Advent of Code 2020
 * Day 4
 * <p>
 * --- Day 4: Passport Processing ---
 * You arrive at the airport only to realize that you grabbed your North Pole Credentials instead of your passport.
 * While these documents are extremely similar, North Pole Credentials aren't issued by a country and therefore
 * aren't actually valid documentation for travel in most of the world.
 * <p>
 * It seems like you're not the only one having problems, though; a very long line has formed for the automatic
 * passport scanners, and the delay could upset your travel itinerary.
 * <p>
 * Due to some questionable network security, you realize you might be able to solve both of these problems at the
 * same time.
 * <p>
 * The automatic passport scanners are slow because they're having trouble detecting which passports have all
 * required fields. The expected fields are as follows:
 * <p>
 * byr (Birth Year)
 * iyr (Issue Year)
 * eyr (Expiration Year)
 * hgt (Height)
 * hcl (Hair Color)
 * ecl (Eye Color)
 * pid (Passport ID)
 * cid (Country ID)
 * Passport data is validated in batch files (your puzzle input). Each passport is represented as a sequence of
 * key:value pairs separated by spaces or newlines. Passports are separated by blank lines.
 * <p>
 * Here is an example batch file containing four passports:
 * <p>
 * ecl:gry pid:860033327 eyr:2020 hcl:#fffffd
 * byr:1937 iyr:2017 cid:147 hgt:183cm
 * <p>
 * iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884
 * hcl:#cfa07d byr:1929
 * <p>
 * hcl:#ae17e1 iyr:2013
 * eyr:2024
 * ecl:brn pid:760753108 byr:1931
 * hgt:179cm
 * <p>
 * hcl:#cfa07d eyr:2025 pid:166559648
 * iyr:2011 ecl:brn hgt:59in
 * The first passport is valid - all eight fields are present. The second passport is invalid - it is missing hgt
 * (the Height field).
 * <p>
 * The third passport is interesting; the only missing field is cid, so it looks like data from North Pole
 * Credentials, not a passport at all! Surely, nobody would mind if you made the system temporarily ignore missing
 * cid fields. Treat this "passport" as valid.
 * <p>
 * The fourth passport is missing two fields, cid and byr. Missing cid is fine, but missing any other field is not,
 * so this passport is invalid.
 * <p>
 * According to the above rules, your improved system would report 2 valid passports.
 * <p>
 * Count the number of valid passports - those that have all required fields. Treat cid as optional.
 *
 * @author chris.jackson
 */
public class day4
{
    private final List<Passport> validPassports = new ArrayList<>();

    /**
     * Constructor
     *
     * @param part part to run
     */
    public day4( final int part ) throws AnswerNotAvailableException
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
     * --- Part One ---
     * In your batch file, how many passports are valid?
     */
    private int part1()
    {
        return checkPassports( false );
    }


    /**
     * --- Part Two ---
     * The line is moving more quickly now, but you overhear airport security talking about how passports with
     * invalid data are getting through. Better add some data validation, quick!
     * <p>
     * You can continue to ignore the cid field, but each other field has strict rules about what values are valid
     * for automatic validation:
     * <p>
     * byr (Birth Year) - four digits; at least 1920 and at most 2002.
     * iyr (Issue Year) - four digits; at least 2010 and at most 2020.
     * eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
     * hgt (Height) - a number followed by either cm or in:
     * If cm, the number must be at least 150 and at most 193.
     * If in, the number must be at least 59 and at most 76.
     * hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
     * ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
     * pid (Passport ID) - a nine-digit number, including leading zeroes.
     * cid (Country ID) - ignored, missing or not.
     * Your job is to count the passports where all required fields are both present and valid according to the above
     * rules. Here are some example values:
     * <p>
     * byr valid:   2002
     * byr invalid: 2003
     * <p>
     * hgt valid:   60in
     * hgt valid:   190cm
     * hgt invalid: 190in
     * hgt invalid: 190
     * <p>
     * hcl valid:   #123abc
     * hcl invalid: #123abz
     * hcl invalid: 123abc
     * <p>
     * ecl valid:   brn
     * ecl invalid: wat
     * <p>
     * pid valid:   000000001
     * pid invalid: 0123456789
     * Here are some invalid passports:
     * <p>
     * eyr:1972 cid:100
     * hcl:#18171d ecl:amb hgt:170 pid:186cm iyr:2018 byr:1926
     * <p>
     * iyr:2019
     * hcl:#602927 eyr:1967 hgt:170cm
     * ecl:grn pid:012533040 byr:1946
     * <p>
     * hcl:dab227 iyr:2012
     * ecl:brn hgt:182cm pid:021572410 eyr:2020 byr:1992 cid:277
     * <p>
     * hgt:59cm ecl:zzz
     * eyr:2038 hcl:74454a iyr:2023
     * pid:3556412378 byr:2007
     * Here are some valid passports:
     * <p>
     * pid:087499704 hgt:74in ecl:grn iyr:2012 eyr:2030 byr:1980
     * hcl:#623a2f
     * <p>
     * eyr:2029 ecl:blu cid:129 byr:1989
     * iyr:2014 pid:896056539 hcl:#a97842 hgt:165cm
     * <p>
     * hcl:#888785
     * hgt:164cm byr:2001 iyr:2015 cid:88
     * pid:545766238 ecl:hzl
     * eyr:2022
     * <p>
     * iyr:2010 hgt:158cm hcl:#b6652a ecl:blu byr:1944 eyr:2021 pid:093154719
     * Count the number of valid passports - those that have all required fields and valid values. Continue to treat
     * cid as optional. In your batch file, how many passports are valid?
     */
    private int part2()
    {
        return checkPassports( true );
    }


    /**
     * Checks the validity of the passports.
     * This is the main entry method for retrieving the data and producing an answer
     *
     * @param checkData if TRUE, the data will be validated as per part 2.
     * @return number of 'valid' passports
     */
    private int checkPassports( final boolean checkData )
    {
        StringBuilder passportStringBuilder = new StringBuilder();
        final List<String> passportsStrings = new ArrayList<>();

        for ( final String line : getData() )
        {
            passportStringBuilder.append( line ).append( SINGLE_SPACE );
            if ( line.length() == 0 )
            {
                // Blank line indicates end of this passport entry, so add it as it is
                passportsStrings.add( new String( passportStringBuilder ) );

                // Clear it so the next entry can begin
                passportStringBuilder = new StringBuilder();
            }
        }
        // Add the final entry
        passportsStrings.add( new String( passportStringBuilder ) );

        // Validate the entries
        validateAllPassports( processPassports( passportsStrings, checkData ) );

        return validPassports.size();
    }

    /**
     * Checks if a{@link Passport} is valid, and adds it to the 'valid' list if it is
     *
     * @param passports list of {@link Passport}s
     */
    private void validateAllPassports( final List<Passport> passports )
    {
        for ( final Passport passport : passports )
        {
            if ( passport.isValid() )
            {
                validPassports.add( passport );
            }
        }
    }

    /**
     * Converts the {@link String} passports into {@link Passport} objects
     *
     * @param passportsStrings the list of String passports
     * @param checkData        if TRUE, the data will be validated as per part 2.
     * @return a list of converted {@link Passport} objects
     */
    private List<Passport> processPassports( final List<String> passportsStrings, final boolean checkData )
    {
        final List<Passport> passports = new ArrayList<>();
        for ( final String passportString : passportsStrings )
        {
            passports.add( new Passport( passportString, checkData ) );
        }
        return passports;
    }


    /**
     * Get the data for the question
     *
     * @return list of passport data lines
     */
    private List<String> getData()
    {
        final URL resource = getClass().getClassLoader().getResource( getClass().getName() );
        final List<String> data = new ArrayList<>();
        try
        {
            assert resource != null;
            try ( final Stream<String> stream = Files.lines( Paths.get( resource.getPath() ) ) )
            {
                stream.forEach( data::add );
            }
        }
        catch ( final IOException e )
        {
            e.printStackTrace();
        }
        return data;
    }


    /**
     * Inner object to define a 'Passport'
     */
    static class Passport
    {
        public static final String CENTIMETERS = "cm";
        public static final String INCHES = "in";

        // Fields that are 'required'
        final List<String> requiredFields = Arrays.asList( "byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid" );

        // Eye colours that are 'valid'
        final List<String> validEyeColours = Arrays.asList( "amb", "blu", "brn", "gry", "grn", "hzl", "oth" );

        private final Map<String, String> data = new HashMap<>();
        private final List<String> missingFields = new ArrayList<>();
        private boolean valid;

        /**
         * Constructor to convert the key/value strings into a Map
         *
         * @param passportData the String data
         * @param checkData    if TRUE, the data will be validated as per part 2.
         */
        public Passport( final String passportData, final boolean checkData )
        {
            final String[] keyValueStrings = passportData.split( SINGLE_SPACE );
            for ( final String keyValueString : keyValueStrings )
            {
                final String[] keyValuePair = keyValueString.split( COLON );
                {
                    data.put( keyValuePair[ 0 ], keyValuePair[ 1 ] );
                }
            }
            validate( checkData );
        }


        /**
         * Must contain all required fields and (part 2 only) pass validation for each field.
         */
        private void validate( final boolean checkData )
        {
            for ( final String field : requiredFields )
            {
                if ( !data.containsKey( field ) )
                {
                    missingFields.add( field );
                }
            }

            if ( missingFields.isEmpty() )
            {
                if ( checkData )
                {
                    valid = validateData();
                }
                else
                {
                    valid = true;
                }
            }
        }

        /**
         * ------------------------------------------------------------------------------------
         * byr (Birth Year) - four digits; at least 1920 and at most 2002.
         * iyr (Issue Year) - four digits; at least 2010 and at most 2020.
         * eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
         * hgt (Height) - a number followed by either cm or in:
         * If cm, the number must be at least 150 and at most 193.
         * If in, the number must be at least 59 and at most 76.
         * hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
         * ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
         * pid (Passport ID) - a nine-digit number, including leading zeroes.
         * cid (Country ID) - ignored, missing or not.
         */
        public boolean validateData()
        {
            // Get fields to validate
            final int birthYear = Integer.parseInt( data.get( "byr" ) );
            final int issuerYear = Integer.parseInt( data.get( "iyr" ) );
            final int expirationYear = Integer.parseInt( data.get( "eyr" ) );

            final String height = data.get( "hgt" );
            final String heightUnits = height.substring( height.length() - 2 );

            // Check we have a valid height unit
            if ( heightUnits.matches( "(in|cm)" ) )
            {
                final int heightValue = Integer.parseInt( height.substring( 0, height.length() - 2 ) );

                final String hairColour = data.get( "hcl" );
                final String eyeColor = data.get( "ecl" );
                final String passportId = data.get( "pid" );


                /*
                 * byr (Birth Year) - four digits; at least 1920 and at most 2002.
                 * iyr (Issue Year) - four digits; at least 2010 and at most 2020.
                 * eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
                 */
                if ( isBetween( birthYear, 1920, 2002 )
                        || isBetween( issuerYear, 2010, 2020 )
                        || isBetween( expirationYear, 2020, 2030 ) )
                {
                    return false;
                }

                /*
                 * hgt (Height) - a number followed by either cm or in:
                 * If cm, the number must be at least 150 and at most 193.
                 * If in, the number must be at least 59 and at most 76.
                 */
                if ( heightUnits.equals( CENTIMETERS ) && isBetween( heightValue, 150, 193 ) )
                {
                    return false;
                }
                else if ( heightUnits.equals( INCHES ) && isBetween( heightValue, 59, 76 ) )
                {
                    return false;
                }

                /*
                 * hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
                 */
                if ( !hairColour.matches( "(#[0-9a-f]{6})" ) )
                {
                    return false;
                }

                /*
                 * ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
                 */
                if ( !validEyeColours.contains( eyeColor ) )
                {
                    return false;
                }

                /*
                 * pid (Passport ID) - a nine-digit number, including leading zeroes.
                 */
                return passportId.matches( "([0-9]{9})" );
            }
            return false;
        }

        /**
         * Checks if the given integerToCheck is between the given boundaries (inclusive)
         *
         * @param integerToCheck the value to check
         * @param lower          the lower boundary (inclusive)
         * @param upper          the upper boundary (inclusive)
         * @return TRUE if the integerToCheck is at least the lower value, and at most the upper value
         */
        private boolean isBetween( final int integerToCheck, final int lower, final int upper )
        {
            return integerToCheck < lower || integerToCheck > upper;
        }

        /**
         * This value is set during validation
         *
         * @return the validity of this passport
         */
        public boolean isValid()
        {
            return valid;
        }
    }
}


