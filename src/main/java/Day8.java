/*
 * Copyright (c) 8/12/2020 Chris Jackson (c-jack)
 * adventofcode.Day8
 */

import static constants.Constants.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exception.AnswerNotAvailableException;
import utils.AOCUtils;


/**
 * Advent of Code 2020
 * Day 8
 * <p>
 * --- Day 8: Handheld Halting ---
 * Your flight to the major airline hub reaches cruising altitude without incident. While you consider checking the
 * in-flight menu for one of those drinks that come with a little umbrella, you are interrupted by the kid sitting
 * next to you.
 * <p>
 * Their handheld game console won't turn on! They ask if you can take a look.
 * <p>
 * You narrow the problem down to a strange infinite loop in the boot code (your puzzle input) of the device. You
 * should be able to fix it, but first you need to be able to run the code in isolation.
 * <p>
 * The boot code is represented as a text file with one instruction per line of text. Each instruction consists of an
 * operation (acc, jmp, or nop) and an argument (a signed number like +4 or -20).
 * <p>
 * acc increases or decreases a single global value called the accumulator by the value given in the argument. For
 * example, acc +7 would increase the accumulator by 7. The accumulator starts at 0. After an acc instruction, the
 * instruction immediately below it is executed next.
 * jmp jumps to a new instruction relative to itself. The next instruction to execute is found using the argument as
 * an offset from the jmp instruction; for example, jmp +2 would skip the next instruction, jmp +1 would continue to
 * the instruction immediately below it, and jmp -20 would cause the instruction 20 lines above to be executed next.
 * nop stands for No OPeration - it does nothing. The instruction immediately below it is executed next.
 * For example, consider the following program:
 * <p>
 * nop +0
 * acc +1
 * jmp +4
 * acc +3
 * jmp -3
 * acc -99
 * acc +1
 * jmp -4
 * acc +6
 * These instructions are visited in this order:
 * <p>
 * nop +0  | 1
 * acc +1  | 2, 8(!)
 * jmp +4  | 3
 * acc +3  | 6
 * jmp -3  | 7
 * acc -99 |
 * acc +1  | 4
 * jmp -4  | 5
 * acc +6  |
 * First, the nop +0 does nothing. Then, the accumulator is increased from 0 to 1 (acc +1) and jmp +4 sets the next
 * instruction to the other acc +1 near the bottom. After it increases the accumulator from 1 to 2, jmp -4 executes,
 * setting the next instruction to the only acc +3. It sets the accumulator to 5, and jmp -3 causes the program to
 * continue back at the first acc +1.
 * <p>
 * This is an infinite loop: with this sequence of jumps, the program will run forever. The moment the program tries
 * to run any instruction a second time, you know it will never terminate.
 * <p>
 * Immediately before the program would run an instruction a second time, the value in the accumulator is 5.
 *
 * @author chris.jackson
 */
public class Day8
{
    /**
     * Constructor
     */
    public Day8() throws AnswerNotAvailableException
    {
        // Check the logic with the example before calculating answers
        testLogic();

        System.out.println( THE_ANSWER_IS_PT1 + part1( getData() ) );
        System.out.println( THE_ANSWER_IS_PT2 + part2( getData() ) );
    }

    /**
     * --- Part One ---
     * Run your copy of the boot code. Immediately before any instruction is executed a second time, what value is in
     * the accumulator?
     * <p>
     * Answer: 1928
     *
     * @param data the data to process for the question
     * @return the accumulator total when the program terminates
     */
    private int part1( final List<String> data )
    {
        final List<Integer> runList = new ArrayList<>();

        return runProgram( createInstructionMap( data ), runList );
    }

    /**
     * --- Part Two ---
     * After some careful analysis, you believe that exactly one instruction is corrupted.
     * <p>
     * Somewhere in the program, either a jmp is supposed to be a nop, or a nop is supposed to be a jmp. (No acc
     * instructions were harmed in the corruption of this boot code.)
     * <p>
     * The program is supposed to terminate by attempting to execute an instruction immediately after the last
     * instruction in the file. By changing exactly one jmp or nop, you can repair the boot code and make it
     * terminate correctly.
     * <p>
     * For example, consider the same program from above:
     * <p>
     * nop +0
     * acc +1
     * jmp +4
     * acc +3
     * jmp -3
     * acc -99
     * acc +1
     * jmp -4
     * acc +6
     * If you change the first instruction from nop +0 to jmp +0, it would create a single-instruction infinite loop,
     * never leaving that instruction. If you change almost any of the jmp instructions, the program will still
     * eventually find another jmp instruction and loop forever.
     * <p>
     * However, if you change the second-to-last instruction (from jmp -4 to nop -4), the program terminates! The
     * instructions are visited in this order:
     * <p>
     * nop +0  | 1
     * acc +1  | 2
     * jmp +4  | 3
     * acc +3  |
     * jmp -3  |
     * acc -99 |
     * acc +1  | 4
     * nop -4  | 5
     * acc +6  | 6
     * After the last instruction (acc +6), the program terminates by attempting to run the instruction below the
     * last instruction in the file. With this change, after the program terminates, the accumulator contains the
     * value 8 (acc +1, acc +1, acc +6).
     * <p>
     * Fix the program so that it terminates normally by changing exactly one jmp (to nop) or nop (to jmp). What is
     * the value of the accumulator after the program terminates?
     * <p>
     * Answer: 1319
     *
     * @param data the data to process for the question
     * @return the accumulator total when the program terminates
     */
    private int part2( final List<String> data )
    {
        // Keep a list of the steps that have run
        final List<Integer> runList = new ArrayList<>();
        int changeIndex = 0;
        int answer;

        // Create the instructions and pass them to the program
        Map<Integer, Instruction> instructionMap = createInstructionMap( data );
        answer = runProgram( instructionMap, runList );

        /*
         * iF the runList doesn't contain the last step in the instructions, it hasn't completed.
         * We'll loop around again
         */
        while ( !runList.contains( data.size() - 1 ) )
        {
            // Re-create the instruction map
            instructionMap = createInstructionMap( data );

            if ( changeIndex == 0 )
            {
                // For the first correction, return the last step that was tried
                changeIndex = Collections.max( runList );
            }
            else
            {
                // Otherwise, try the one before it
                changeIndex--;
            }

            // Clear the runList before the next attempt
            runList.clear();

            // Detail the failed instruction for debugging
//            System.out.println( "Failed instruction was " + data.get( changeIndex ) + " at position " + changeIndex );

            // Get the instruction that we want to try to change
            final Instruction instruction = instructionMap.get( changeIndex );

            // Fix the program so that it terminates normally by changing exactly one jmp (to nop)
            if ( instruction.getAction().equals( Action.JMP ) )
            {
                instruction.changeAction( Action.NOP );
                answer = runProgram( instructionMap, runList );
            }
            //  ...or nop (to jmp).
            else if ( instruction.getAction().equals( Action.NOP ) )
            {
                instruction.changeAction( Action.JMP );
            }

            // any others will loop again and increment -1
        }

        // The loop contained the last step, so return the answer
        return answer;
    }


    /**
     * Creates a map of instructions containing the index (Integer) and instruction {@link Instruction}
     *
     * @param data the data to use to create the map
     * @return a Map of {@link Instruction} values, with the step number
     */
    private Map<Integer, Instruction> createInstructionMap( final List<String> data )
    {
        final Map<Integer, Instruction> instructions = new HashMap<>();
        int i = 0;

        // Convert each line and add it as an instruction
        for ( final String item : data )
        {
            instructions.put( i, new Instruction( item ) );
            i++;
        }
        return instructions;
    }


    /**
     * Runs the program
     *
     * @param instructionMap the instructions
     * @param runList        the list of steps that are ran during this execution
     * @return the final value of the accumulator during this execution
     */
    private int runProgram( final Map<Integer, Instruction> instructionMap, final List<Integer> runList )
    {
        int index = 0;
        int accumulator = 0;
        boolean run = true;

        while ( run )
        {
            final Instruction instruction = instructionMap.get( index );

            if ( index == ( instructionMap.size() - 1 ) )
            {
                // We've just selected the final index, so the console started!
                run = false;
                System.out.println( "Console started!" );
            }

            // Check if we've run this step before
            if ( !runList.contains( index ) )
            {
                // Add this index to our run list
                runList.add( index );

                // Calculate the next run list
                if ( instruction.getAction().equals( Action.JMP ) )
                {
                    if ( instruction.getChange() == '+' )
                    {
                        index += instruction.getValue();
                    }
                    if ( instruction.getChange() == '-' )
                    {
                        index -= instruction.getValue();
                    }

                    // Skip to the next
                    continue;
                }
                else if ( instruction.getAction().equals( Action.ACC ) )
                {
                    if ( instruction.getChange() == '+' )
                    {
                        accumulator += instruction.getValue();
                    }
                    if ( instruction.getChange() == '-' )
                    {
                        accumulator -= instruction.getValue();
                    }
                }

                // for anything but JMP, increment the index by 1
                index++;
            }
            else
            {
                // We've run this step before, so fail the loop to prevent recursion.  Optional print for debugging
//                System.out.println( "** RECURSION **" );
                run = false;
            }
        }
        return accumulator;
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
     * Enum for the action
     * <p>
     * ACC increases or decreases a single global value called the accumulator by the value given in the argument.
     * For example, acc +7 would increase the accumulator by 7. The accumulator starts at 0. After an acc
     * instruction, the instruction immediately below it is executed next.
     * <p>
     * JMP jumps to a new instruction relative to itself. The next instruction to execute is found using the argument
     * as an offset from the jmp instruction; for example, jmp +2 would skip the next instruction, jmp +1 would
     * continue to the instruction immediately below it, and jmp -20 would cause the instruction 20 lines above to be
     * executed next.
     * <p>
     * NOP stands for No OPeration - it does nothing. The instruction immediately below it is executed next.
     */
    enum Action
    {
        ACC,
        JMP,
        NOP
    }

    /**
     * Instruction
     * <p>
     * This object stores all of the data for an Instruction (action, change +/-, value(
     */
    static class Instruction
    {
        private Action action;
        private final char change;
        private final int value;

        /**
         * Constructor
         */
        public Instruction( final String item )
        {
            final String[] instruction = item.split( SINGLE_SPACE );
            this.action = Action.valueOf( instruction[ 0 ].toUpperCase() );
            this.change = instruction[ 1 ].substring( 0, 1 ).toCharArray()[ 0 ];
            this.value = Integer.parseInt( instruction[ 1 ].substring( 1 ) );
        }

        /**
         * Changes the action to a different {@link Action}
         *
         * @param action the value to change to
         */
        public void changeAction( final Action action )
        {
            this.action = action;
        }

        /**
         * @return The action.
         */
        public Action getAction()
        {
            return action;
        }

        /**
         * @return The change.
         */
        public char getChange()
        {
            return change;
        }

        /**
         * @return The value.
         */
        public int getValue()
        {
            return value;
        }

        /**
         * This method can be used to diagnostically print out the instruction
         */
        public void print()
        {
            System.out.println( action.toString() + change + value );
        }
    }

    /* *************** *
     *     TESTS       *
     * *************** */

    /**
     * Checks the logic against the examples in the question.
     */
    private void testLogic()
    {
        final List<String> exampleData = Arrays.asList( "nop +0",
                "acc +1",
                "jmp +4",
                "acc +3",
                "jmp -3",
                "acc -99",
                "acc +1",
                "jmp -4",
                "acc +6" );
        assert part1( exampleData ) == 5 : PART_1_TEST_FAILED;
        assert part2( exampleData ) == 8 : PART_2_TEST_FAILED;
    }
}
