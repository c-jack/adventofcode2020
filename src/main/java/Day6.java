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
 * Day 6
 * <p>
 * --- Day 6: Custom Customs ---
 * As your flight approaches the regional airport where you'll switch to a much larger plane, customs declaration
 * forms are distributed to the passengers.
 * <p>
 * The form asks a series of 26 yes-or-no questions marked a through z. All you need to do is identify the questions
 * for which anyone in your group answers "yes". Since your group is just you, this doesn't take very long.
 * <p>
 * However, the person sitting next to you seems to be experiencing a language barrier and asks if you can help. For
 * each of the people in their group, you write down the questions for which they answer "yes", one per line. For
 * example:
 * <p>
 * abcx
 * abcy
 * abcz
 * In this group, there are 6 questions to which anyone answered "yes": a, b, c, x, y, and z. (Duplicate answers to
 * the same question don't count extra; each question counts at most once.)
 * <p>
 * Another group asks for your help, then another, and eventually you've collected answers from every group on the
 * plane (your puzzle input). Each group's answers are separated by a blank line, and within each group, each
 * person's answers are on a single line. For example:
 * <p>
 * abc
 * <p>
 * a
 * b
 * c
 * <p>
 * ab
 * ac
 * <p>
 * a
 * a
 * a
 * a
 * <p>
 * b
 * This list represents answers from five groups:
 * <p>
 * The first group contains one person who answered "yes" to 3 questions: a, b, and c.
 * The second group contains three people; combined, they answered "yes" to 3 questions: a, b, and c.
 * The third group contains two people; combined, they answered "yes" to 3 questions: a, b, and c.
 * The fourth group contains four people; combined, they answered "yes" to only 1 question, a.
 * The last group contains one person who answered "yes" to only 1 question, b.
 * In this example, the sum of these counts is 3 + 3 + 3 + 1 + 1 = 11.
 *
 * @author chris.jackson
 */
public class Day6
{
    /**
     * Constructor
     */
    public Day6() throws AnswerNotAvailableException
    {
        // Check the logic with the example before calculating answers
        testLogic();

        System.out.println( THE_ANSWER_IS_PT1 + part1( getData() ) );
        System.out.println( THE_ANSWER_IS_PT2 + part2( getData() ) );
    }

    /**
     * --- Part One ---
     * For each group, count the number of questions to which anyone answered "yes". What is the sum of those counts?
     * <p>
     * Answer: 6310
     *
     * @param data the data to process for the question
     */
    private int part1( final List<String> data ) throws AnswerNotAvailableException
    {
        return processAnswers( data, 1 );
    }

    /**
     * --- Part Two ---
     * As you finish the last group's customs declaration, you notice that you misread one word in the instructions:
     * <p>
     * You don't need to identify the questions to which anyone answered "yes"; you need to identify the questions to
     * which everyone answered "yes"!
     * <p>
     * Using the same example as above:
     * <p>
     * abc
     * <p>
     * a
     * b
     * c
     * <p>
     * ab
     * ac
     * <p>
     * a
     * a
     * a
     * a
     * <p>
     * b
     * This list represents answers from five groups:
     * <p>
     * In the first group, everyone (all 1 person) answered "yes" to 3 questions: a, b, and c.
     * In the second group, there is no question to which everyone answered "yes".
     * In the third group, everyone answered yes to only 1 question, a. Since some people did not answer "yes" to b
     * or c, they don't count.
     * In the fourth group, everyone answered yes to only 1 question, a.
     * In the fifth group, everyone (all 1 person) answered "yes" to 1 question, b.
     * In this example, the sum of these counts is 3 + 0 + 1 + 1 + 1 = 6.
     * <p>
     * For each group, count the number of questions to which everyone answered "yes". What is the sum of those counts?
     * <p>
     * Answer: 3193
     *
     * @param data the data to process for the question
     */
    private int part2( final List<String> data ) throws AnswerNotAvailableException
    {
        return processAnswers( data, 2 );
    }

    /**
     * Process the question answer using the provided data
     *
     * @param questionData the data to use
     * @param part         the question part logic to use
     * @return sum of the calculated answer values
     * @throws AnswerNotAvailableException if the answer can't be calculated
     */
    private int processAnswers( final List<String> questionData, final int part ) throws AnswerNotAvailableException
    {
        final Map<Integer, List<String>> questionMap = new HashMap<>();
        List<String> groupQuestions = new ArrayList<>();
        int i = 0;

        // Produce a Map of group answers from the provided data
        for ( final String line : questionData )
        {
            if ( line.length() == 0 )
            {
                // If the line is blank, it's the end of a group.  Add them to the map and reset the groupQuestions
                questionMap.put( i, groupQuestions );
                groupQuestions = new ArrayList<>();
                i++;
            }
            else
            {
                groupQuestions.add( line );
            }
        }

        /*
         * Add the final answer set from the loop.
         * This is caused by using the enhanced for, and is just a way to become familiar with the cons of using them.
         */
        questionMap.put( i, groupQuestions );

        /*
         * Get the answer for Part 1:
         * --------------------------
         * "For each group, count the number of questions to which ANYONE answered "yes". What is the sum of those
         * counts?"
         */
        if ( part == 1 )
        {
            // Because it's ANYONE, well track the distinct answers
            final List<Integer> distinctAnswersPerGroup = new ArrayList<>();

            for ( final Map.Entry<Integer, List<String>> group : questionMap.entrySet() )
            {
                final Set<String> distinctQuestions = new HashSet<>();
                for ( final String questions : group.getValue() )
                {
                    /*
                     * Split the questions into a list of single character Strings.
                     * This is a Set, so it'll handle distinct additions for us.
                     */
                    distinctQuestions.addAll( Arrays.asList( questions.split( "(?!^)" ) ) );
                }
                /*
                 * Add the size of the remaining answers to the common answers list.
                 * (This could have been a simple +=. but it's a good excuse to play with streams later
                 */
                distinctAnswersPerGroup.add( distinctQuestions.size() );
            }

            // Get the sum of all values
            return distinctAnswersPerGroup.stream().mapToInt( Integer::intValue ).sum();
        }
        /*
         * Get the answer for Part 2:
         * --------------------------
         * "For each group, count the number of questions to which EVERYONE answered "yes". What is the sum of those
         * counts?"
         */
        else if ( part == 2 )
        {
            // Because it's EVERYONE, well track the common answers only
            final List<Integer> commonAnswersPerGroup = new ArrayList<>();

            for ( final Map.Entry<Integer, List<String>> group : questionMap.entrySet() )
            {
                final List<String> sharedQuestions = new ArrayList<>();
                boolean addFirst = true;

                for ( final String answers : group.getValue() )
                {
                    // Split the answers into a list
                    final List<String> theseQuestions = Arrays.asList( answers.split( "(?!^)" ) );

                    if ( addFirst )
                    {
                        /*
                         * Add the first set of answers to compare against.
                         * If subsequent answers are in the first one, they aren't common (in any order)
                         */
                        sharedQuestions.addAll( theseQuestions );
                        addFirst = false;
                    }
                    else
                    {
                        // Get a list of questions in the 'sharedQuestions' list that aren't in theseQuestions
                        final List<String> uncommonSharedQuestions = new ArrayList<>( sharedQuestions );
                        uncommonSharedQuestions.removeAll( theseQuestions );

                        // Get a list of questions in the 'theseQuestions' list that aren't in sharedQuestions
                        final List<String> uncommonTheseQuestions = new ArrayList<>( theseQuestions );
                        uncommonTheseQuestions.removeAll( sharedQuestions );

                        // Remove both the uncommon values from the main list
                        sharedQuestions.removeAll( uncommonSharedQuestions );
                        sharedQuestions.removeAll( uncommonTheseQuestions );
                    }
                }
                /*
                 * Add the size of the remaining answers to the common answers list.
                 * (This could have been a simple +=. but it's a good excuse to play with streams later
                 */
                commonAnswersPerGroup.add( sharedQuestions.size() );
            }

            // Get the sum of all values
            return commonAnswersPerGroup.stream().mapToInt( Integer::intValue ).sum();
        }
        throw new AnswerNotAvailableException();
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
     * <p>
     * NOTE: These require the VM option '-enableassertions' to be added when AdventOfCode is ran in order to halt
     * execution.
     */
    private void testLogic() throws AnswerNotAvailableException
    {
        final List<String> testData = Arrays.asList(
                "abc",
                "",
                "a",
                "b",
                "c",
                "",
                "ab",
                "ac",
                "",
                "a",
                "a",
                "a",
                "a",
                "",
                "b" );

        final int i = part1( testData );
        assert i == 11 : PART_1_TEST_FAILED;

        final int j = part2( testData );
        assert j == 6 : PART_2_TEST_FAILED;
    }
}
