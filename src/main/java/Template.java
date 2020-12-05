/*
 * Copyright (c) 5/12/2020 Chris Jackson (c-jack)
 * adventofcode.Template
 */

import static constants.Constants.*;

import java.util.List;

import exception.AnswerNotAvailableException;
import utils.AOCUtils;


/**
 * Advent of Code 2020
 * Day 5
 * <p>
 *
 * @author chris.jackson
 */
public class Template
{
    /**
     * Constructor
     */
    public Template() throws AnswerNotAvailableException
    {
        // Check the logic with the example before calculating answers
        testLogic();

        System.out.println( THE_ANSWER_IS_PT1 + part1( getData() ) );
        System.out.println( THE_ANSWER_IS_PT2 + part2( getData() ) );
    }

    /**
     * --- Part One ---
     *
     * @param data
     */
    private int part1( final List<String> data )
    {
        return 0;
    }

    /**
     * --- Part Two ---
     *
     * @param data
     */
    private long part2( final List<String> data )
    {

        return 0;
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
    private void testLogic()
    {
        assert 1 == 1;
    }
}
