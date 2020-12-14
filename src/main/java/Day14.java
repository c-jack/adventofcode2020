/*
 * Copyright (c) 14/12/2020 Chris Jackson (c-jack)
 * adventofcode.Day14
 */

import static constants.Constants.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exception.AnswerNotAvailableException;
import utils.AOCUtils;


/**
 * Advent of Code 2020
 * Day 14
 * <p>
 * --- Day 14: Docking Data ---
 * As your ferry approaches the sea port, the captain asks for your help again. The computer system that runs this
 * port isn't compatible with the docking program on the ferry, so the docking parameters aren't being correctly
 * initialized in the docking program's memory.
 * <p>
 * After a brief inspection, you discover that the sea port's computer system uses a strange bitmask system in its
 * initialization program. Although you don't have the correct decoder chip handy, you can emulate it in software!
 * <p>
 * The initialization program (your puzzle input) can either update the bitmask or write a value to memory. Values
 * and memory addresses are both 36-bit unsigned integers. For example, ignoring bitmasks for a moment, a line like
 * mem[8] = 11 would write the value 11 to memory address 8.
 * <p>
 * The bitmask is always given as a string of 36 bits, written with the most significant bit (representing 2^35) on
 * the left and the least significant bit (2^0, that is, the 1s bit) on the right. The current bitmask is applied to
 * values immediately before they are written to memory: a 0 or 1 overwrites the corresponding bit in the value,
 * while an X leaves the bit in the value unchanged.
 * <p>
 * For example, consider the following program:
 * <p>
 * mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
 * mem[8] = 11
 * mem[7] = 101
 * mem[8] = 0
 * This program starts by specifying a bitmask (mask = ....). The mask it specifies will overwrite two bits in every
 * written value: the 2s bit is overwritten with 0, and the 64s bit is overwritten with 1.
 * <p>
 * The program then attempts to write the value 11 to memory address 8. By expanding everything out to individual
 * bits, the mask is applied as follows:
 * <p>
 * value:  000000000000000000000000000000001011  (decimal 11)
 * mask:   XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
 * result: 000000000000000000000000000001001001  (decimal 73)
 * So, because of the mask, the value 73 is written to memory address 8 instead. Then, the program tries to write 101
 * to address 7:
 * <p>
 * value:  000000000000000000000000000001100101  (decimal 101)
 * mask:   XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
 * result: 000000000000000000000000000001100101  (decimal 101)
 * This time, the mask has no effect, as the bits it overwrote were already the values the mask tried to set.
 * Finally, the program tries to write 0 to address 8:
 * <p>
 * value:  000000000000000000000000000000000000  (decimal 0)
 * mask:   XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
 * result: 000000000000000000000000000001000000  (decimal 64)
 * 64 is written to address 8 instead, overwriting the value that was there previously.
 * <p>
 * To initialize your ferry's docking program, you need the sum of all values left in memory after the initialization
 * program completes. (The entire 36-bit address space begins initialized to the value 0 at every address.) In the
 * above example, only two values in memory are not zero - 101 (at address 7) and 64 (at address 8) - producing a sum
 * of 165.
 * <p>
 * Execute the initialization program. What is the sum of all values left in memory after it completes?
 *
 * @author chris.jackson
 */
public class Day14
{

    public static final char FLOATING_CHAR = 'X';
    public static final String DATA_SEPARATOR = " = ";
    public static final char ZERO_CHAR = '0';
    public static final char ONE_CHAR = '1';

    /**
     * Constructor
     */
    public Day14() throws AnswerNotAvailableException
    {
        // Check the logic with the example before calculating answers
        testLogic();

        System.out.println( THE_ANSWER_IS_PT1 + part1( getData() ) );

        System.out.println( THE_ANSWER_IS_PT2 + part2( getData() ) );
    }

    /**
     * --- Part One ---
     * <p>
     * What is the sum of all values left in memory after it completes?
     * <p>
     * Answer: 6513443633260
     *
     * @param instructions the data to process for the question
     * @return sum of all values in the memory map
     */
    private long part1( final List<String> instructions )
    {
        // reset the sum
        long sum = 0;

        // Create a Map to store the memory values
        final Map<Integer, String> memory = new HashMap<>();

        // Convert the instructions to a list of Instructions
        final List<Instruction> instructionValues = convertInstructionStringsToInstructions( instructions );

        // Calculate all the memory values using Part 1's rules
        calculateAndAddBinaryValuesPart1( instructionValues, memory );

        // Calculate the sum of the values
        for ( final String memoryValue : memory.values() )
        {
            // Convert the value into a number and add it to the sum
            sum += new BigInteger( memoryValue, 2 ).longValue();
        }

        return sum;
    }

    /**
     * --- Part Two ---
     * For some reason, the sea port's computer system still can't communicate with your ferry's docking program. It
     * must be using version 2 of the decoder chip!
     * <p>
     * A version 2 decoder chip doesn't modify the values being written at all. Instead, it acts as a memory address
     * decoder. Immediately before a value is written to memory, each bit in the bitmask modifies the corresponding
     * bit of the destination memory address in the following way:
     * <p>
     * If the bitmask bit is 0, the corresponding memory address bit is unchanged.
     * If the bitmask bit is 1, the corresponding memory address bit is overwritten with 1.
     * If the bitmask bit is X, the corresponding memory address bit is floating.
     * A floating bit is not connected to anything and instead fluctuates unpredictably. In practice, this means the
     * floating bits will take on all possible values, potentially causing many memory addresses to be written all at
     * once!
     * <p>
     * For example, consider the following program:
     * <p>
     * mask = 000000000000000000000000000000X1001X
     * mem[42] = 100
     * mask = 00000000000000000000000000000000X0XX
     * mem[26] = 1
     * When this program goes to write to memory address 42, it first applies the bitmask:
     * <p>
     * address: 000000000000000000000000000000101010  (decimal 42)
     * mask:    000000000000000000000000000000X1001X
     * result:  000000000000000000000000000000X1101X
     * After applying the mask, four bits are overwritten, three of which are different, and two of which are
     * floating. Floating bits take on every possible combination of values; with two floating bits, four actual
     * memory addresses are written:
     * <p>
     * 000000000000000000000000000000011010  (decimal 26)
     * 000000000000000000000000000000011011  (decimal 27)
     * 000000000000000000000000000000111010  (decimal 58)
     * 000000000000000000000000000000111011  (decimal 59)
     * Next, the program is about to write to memory address 26 with a different bitmask:
     * <p>
     * address: 000000000000000000000000000000011010  (decimal 26)
     * mask:    00000000000000000000000000000000X0XX
     * result:  00000000000000000000000000000001X0XX
     * This results in an address with three floating bits, causing writes to eight memory addresses:
     * <p>
     * 000000000000000000000000000000010000  (decimal 16)
     * 000000000000000000000000000000010001  (decimal 17)
     * 000000000000000000000000000000010010  (decimal 18)
     * 000000000000000000000000000000010011  (decimal 19)
     * 000000000000000000000000000000011000  (decimal 24)
     * 000000000000000000000000000000011001  (decimal 25)
     * 000000000000000000000000000000011010  (decimal 26)
     * 000000000000000000000000000000011011  (decimal 27)
     * The entire 36-bit address space still begins initialized to the value 0 at every address, and you still need
     * the sum of all values left in memory at the end of the program. In this example, the sum is 208.
     * <p>
     * Execute the initialization program using an emulator for a version 2 decoder chip. What is the sum of all
     * values left in memory after it completes?
     * <p>
     * Answer: 3442819875191
     *
     * @param instructions the data to process for the question
     * @return the answer as a {@link Long}
     */
    private Long part2( final List<String> instructions )
    {
        // reset the sum
        long sum = 0;

        // Create a Map to store the memory values
        final Map<Long, Integer> values = new HashMap<>();

        // Convert the instructions to a list of Instructions
        final List<Instruction> instructionValues = convertInstructionStringsToInstructions( instructions );

        // Process the instructions by populating the binary values, as per part 2's rules
        calculateAndAddBinaryValuesPart2( instructionValues );

        // Convert the floating characters and populate the value list
        for ( final Instruction instruction : instructionValues )
        {
            convertFloatingCharacters( 0, values, instruction );
        }

        // Calculate the sum of the values
        for ( final Map.Entry<Long, Integer> memoryValue : values.entrySet() )
        {
            sum += memoryValue.getValue();
        }

        return sum;
    }

    /**
     * Converts the {@link String} instructions to a list of {@link Instruction}s
     *
     * @param instructions the list of string instructions
     * @return a list of {@link Instruction} values
     */
    private List<Instruction> convertInstructionStringsToInstructions( final List<String> instructions )
    {
        final List<Instruction> instructionValueMap = new ArrayList<>();
        String map = EMPTY_STRING;

        for ( final String instruction : instructions )
        {
            // Update the mask, if the entry is a mask
            if ( instruction.startsWith( "mask" ) )
            {
                map = instruction.split( DATA_SEPARATOR )[ 1 ];
            }
            else
            {

                // Split the instruction
                final String[] instructionData = instruction.split( DATA_SEPARATOR );

                // Get the memory address to update
                final int memoryAddress = Integer.parseInt( instructionData[ 0 ].replaceAll( "\\D+", EMPTY_STRING ) );

                // Get the new value to set
                final int newMemoryValue = Integer.parseInt( instructionData[ 1 ] );

                instructionValueMap.add( new Instruction( map, memoryAddress, newMemoryValue ) );

            }
        }

        return instructionValueMap;
    }

    /**
     * Converts the floating characters in the binary value
     *
     * @param iteration   the iteration
     * @param values      the cumulative list of memory values
     * @param instruction the {@link Instruction} being processed
     */
    private void convertFloatingCharacters( final int iteration,
                                            final Map<Long, Integer> values,
                                            final Instruction instruction )
    {
        int index = iteration;
        final String instructionBinary = instruction.binary;

        // Process the Instruction until its binary no longer contains an X
        while ( instructionBinary.contains( String.valueOf( FLOATING_CHAR ) ) )
        {
            if ( instructionBinary.toCharArray()[ index ] == FLOATING_CHAR )
            {
                // Convert the X to 0
                convertAndRecurse( values, instruction, index, ZERO_CHAR );

                // Convert the X to 1
                convertAndRecurse( values, instruction, index, ONE_CHAR );

                break;
            }
            index++;
        }
        // If the instructionBinary no longer contains an X, add it to the value list
        if ( !instructionBinary.contains( String.valueOf( FLOATING_CHAR ) ) )
        {
            values.put( new BigInteger( instructionBinary, 2 ).longValue(), instruction.value );
        }
    }

    /**
     * Converts the X value in the binary value
     *
     * @param values      the cumulative list of memory values
     * @param instruction the {@link Instruction} being processed
     * @param index       the index of the character being iterated
     * @param convertTo   the character to convert the 'X' value to
     */
    private void convertAndRecurse( final Map<Long, Integer> values,
                                    final Instruction instruction,
                                    final int index,
                                    final char convertTo )
    {
        final char[] modifiedMemoryValue = instruction.binary.toCharArray().clone();
        modifiedMemoryValue[ index ] = convertTo;
        final String modifiedString = new String( modifiedMemoryValue );

        convertFloatingCharacters( index, values, new Instruction( instruction, modifiedString ) );
    }


    /**
     * Process the instructions for Part 2
     *
     * @param instructions the instructions to process
     * @param values the list of values to append
     */
    private void calculateAndAddBinaryValuesPart1( final List<Instruction> instructions,
                                                   final Map<Integer, String> values )
    {
        for ( final Instruction instruction : instructions )
        {
            // Convert the memory address to the starting binary
            final char[] addressBinary = instruction.getBinaryCharArray( 1 );

            // Transform the value using the current Mask
            final char[] charArray = instruction.mask.toCharArray();

            for ( int i = 0; i < charArray.length; i++ )
            {
                final char v = charArray[ i ];
                if ( v != FLOATING_CHAR )
                {
                    addressBinary[ i ] = v;
                }
            }

            // Add it to the memory map
            values.put( instruction.memoryAddress, String.valueOf( addressBinary ) );
        }
    }

    /**
     * Process the instructions for Part 2
     *
     * @param instructions the instructions to process
     */
    private void calculateAndAddBinaryValuesPart2( final List<Instruction> instructions )
    {
        for ( final Instruction instruction : instructions )
        {
            // Convert the memory address to the starting binary
            final char[] addressBinary = instruction.getBinaryCharArray( 2 );

            // Transform the value using the current Mask
            final char[] charArray = instruction.mask.toCharArray();

            for ( int i = 0; i < charArray.length; i++ )
            {
                final char v = charArray[ i ];
                if ( v != ZERO_CHAR )
                {
                    addressBinary[ i ] = v;
                }
            }

            // Add it to the memory map
            instruction.setBinary( String.valueOf( addressBinary ) );
        }
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
     * Inner Class to define an instruction
     */
    static class Instruction
    {
        int memoryAddress;
        int value;
        String binary;
        String mask;

        /**
         * Constructor for making a new Instruction
         *
         * @param bitmask       the bitmask for this instruction
         * @param memoryAddress the memory address for the instruction
         * @param value         the value for the instruction
         */
        public Instruction( final String bitmask, final int memoryAddress, final int value )
        {
            this.mask = bitmask;
            this.memoryAddress = memoryAddress;
            this.value = value;
        }

        /**
         * Constructor for making a new Instruction based on an existing {@link Instruction}
         *
         * @param instruction the existing {@link Instruction} to clone
         * @param binary      the modified binary value to set
         */
        public Instruction( final Instruction instruction, final String binary )
        {
            this.mask = instruction.mask;
            this.memoryAddress = instruction.memoryAddress;
            this.value = instruction.value;
            this.binary = binary;
        }

        /**
         * Sets a binary value for this instruction
         *
         * @param binary The binary to set.
         */
        public void setBinary( final String binary )
        {
            this.binary = binary;
        }

        /**
         * Converts the Instruction's:
         * - value (if part 1) to a binary char array, or;
         * - memoryAddress (if part 2)  to a binary char array.
         *
         * @param part the question part
         * @return a binary char array
         */
        public char[] getBinaryCharArray( final int part )
        {
            // Choose the relevant value to convert
            final int valueToConvert = part == 1 ? value : memoryAddress;

            final StringBuilder binary = new StringBuilder( BigInteger.valueOf( valueToConvert ).toString( 2 ) );

            // Pad the value until it is the correct length
            while ( binary.length() != 36 )
            {
                binary.insert( 0, "0" );
            }

            // Convert the padded value to a Char Array
            return binary.toString().toCharArray();
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
        final List<String> testData = Arrays.asList(
                "mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X",
                "mem[8] = 11",
                "mem[7] = 101",
                "mem[8] = 0" );
        assert part1( testData ) == 165 : PART_1_TEST_FAILED;

        final List<String> testData2 = Arrays.asList(
                "mask = 000000000000000000000000000000X1001X",
                "mem[42] = 100",
                "mask = 00000000000000000000000000000000X0XX",
                "mem[26] = 1" );

        assert part2( testData2 ) == 208 : PART_2_TEST_FAILED;
    }
}
