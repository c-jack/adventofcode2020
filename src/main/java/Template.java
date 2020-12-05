/*
 * Copyright (c) 5/12/2020 Chris Jackson (c-jack)
 * adventofcode.Template
 */

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
     *
     * @param part part to run
     */
    public Template( final int part ) throws AnswerNotAvailableException
    {
        if ( part == 1 )
        {
//            System.out.println( THE_ANSWER_IS + part1() );
            throw new AnswerNotAvailableException();
        }
        else if ( part == 2 )
        {
//            System.out.println( THE_ANSWER_IS + part2() );
            throw new AnswerNotAvailableException();
        }
    }

    /**
     * --- Part One ---
     */
    private int part1()
    {
        return 0;
    }


    /**
     * --- Part Two ---
     */
    private long part2()
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
}
