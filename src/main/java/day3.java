import static constants.Constants.*;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import exception.AnswerNotAvailableException;


/**
 * Advent of Code 2020
 * Day 3 Part 1
 * <p>
 * --- Day 3: Toboggan Trajectory ---
 * With the toboggan login problems resolved, you set off toward the airport. While travel by toboggan might be easy,
 * it's certainly not safe: there's very minimal steering and the area is covered in trees. You'll need to see which
 * angles will take you near the fewest trees.
 * <p>
 * Due to the local geology, trees in this area only grow on exact integer coordinates in a grid. You make a map
 * (your puzzle input) of the open squares (.) and trees (#) you can see. For example:
 * <p>
 * ..##.......
 * #...#...#..
 * .#....#..#.
 * ..#.#...#.#
 * .#...##..#.
 * ..#.##.....
 * .#.#.#....#
 * .#........#
 * #.##...#...
 * #...##....#
 * .#..#...#.#
 * These aren't the only trees, though; due to something you read about once involving arboreal genetics and biome
 * stability, the same pattern repeats to the right many times:
 * <p>
 * ..##.........##.........##.........##.........##.........##.......  --->
 * #...#...#..#...#...#..#...#...#..#...#...#..#...#...#..#...#...#..
 * .#....#..#..#....#..#..#....#..#..#....#..#..#....#..#..#....#..#.
 * ..#.#...#.#..#.#...#.#..#.#...#.#..#.#...#.#..#.#...#.#..#.#...#.#
 * .#...##..#..#...##..#..#...##..#..#...##..#..#...##..#..#...##..#.
 * ..#.##.......#.##.......#.##.......#.##.......#.##.......#.##.....  --->
 * .#.#.#....#.#.#.#....#.#.#.#....#.#.#.#....#.#.#.#....#.#.#.#....#
 * .#........#.#........#.#........#.#........#.#........#.#........#
 * #.##...#...#.##...#...#.##...#...#.##...#...#.##...#...#.##...#...
 * #...##....##...##....##...##....##...##....##...##....##...##....#
 * .#..#...#.#.#..#...#.#.#..#...#.#.#..#...#.#.#..#...#.#.#..#...#.#  --->
 * You start on the open square (.) in the top-left corner and need to reach the bottom (below the bottom-most row on
 * your map).
 * <p>
 * The toboggan can only follow a few specific slopes (you opted for a cheaper model that prefers rational numbers);
 * start by counting all the trees you would encounter for the slope right 3, down 1:
 * <p>
 * From your starting position at the top-left, check the position that is right 3 and down 1. Then, check the
 * position that is right 3 and down 1 from there, and so on until you go past the bottom of the map.
 * <p>
 * The locations you'd check in the above example are marked here with O where there was an open square and X where
 * there was a tree:
 * <p>
 * ..##.........##.........##.........##.........##.........##.......  --->
 * #..O#...#..#...#...#..#...#...#..#...#...#..#...#...#..#...#...#..
 * .#....X..#..#....#..#..#....#..#..#....#..#..#....#..#..#....#..#.
 * ..#.#...#O#..#.#...#.#..#.#...#.#..#.#...#.#..#.#...#.#..#.#...#.#
 * .#...##..#..X...##..#..#...##..#..#...##..#..#...##..#..#...##..#.
 * ..#.##.......#.X#.......#.##.......#.##.......#.##.......#.##.....  --->
 * .#.#.#....#.#.#.#.O..#.#.#.#....#.#.#.#....#.#.#.#....#.#.#.#....#
 * .#........#.#........X.#........#.#........#.#........#.#........#
 * #.##...#...#.##...#...#.X#...#...#.##...#...#.##...#...#.##...#...
 * #...##....##...##....##...#X....##...##....##...##....##...##....#
 * .#..#...#.#.#..#...#.#.#..#...X.#.#..#...#.#.#..#...#.#.#..#...#.#  --->
 * In this example, traversing the map using this slope would cause you to encounter 7 trees.
 * <p>
 * Starting at the top-left corner of your map and following a slope of right 3 and down 1, how many trees would you
 * encounter?
 *
 * @author chris.jackson
 */
public class day3
{

    public static final char TREE = '#';

    /**
     * Constructor
     *
     * @param part part to run
     */
    public day3( final int part ) throws AnswerNotAvailableException
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
     * Starting at the top-left corner of your map and following a slope of right 3 and down 1, how many trees would
     * you encounter?
     */
    private int part1()
    {
        // Set to TRUE to print the map
        final boolean printMap = false;

        return countTrees( printMap, 3, 1 );
    }


    /**
     * --- Part Two ---
     * Time to check the rest of the slopes - you need to minimize the probability of a sudden arboreal stop, after all.
     * <p>
     * Determine the number of trees you would encounter if, for each of the following slopes, you start at the
     * top-left corner and traverse the map all the way to the bottom:
     * <p>
     * Right 1, down 1.
     * Right 3, down 1. (This is the slope you already checked.)
     * Right 5, down 1.
     * Right 7, down 1.
     * Right 1, down 2.
     * In the above example, these slopes would find 2, 7, 3, 4, and 2 tree(s) respectively; multiplied together,
     * these produce the answer 336.
     * <p>
     * What do you get if you multiply together the number of trees encountered on each of the listed slopes?
     */
    private long part2()
    {

        final long path1 = countTrees( false, 1, 1 );
        final long path2 = countTrees( false, 3, 1 );
        final long path3 = countTrees( false, 5, 1 );
        final long path4 = countTrees( false, 7, 1 );
        final long path5 = countTrees( false, 1, 2 );

        return path1 * path2 * path3 * path4 * path5;
    }


    /**
     * Count the trees encountered on the map when given the provided right/down params
     *
     * @param printMap whether to output the amended map to the console with tree positions
     * @param right    how many steps to the right to take per iteration
     * @param down     how many steps down to take per iteration
     * @return the number of trees (#) encountered
     */
    private int countTrees( final boolean printMap, final int right, final int down )
    {
        final List<String> mapOfTrees = getData();
        int currentRow = 0;
        int currentCol = 0;
        int treeCount = 0;


        if ( printMap )
        {
            // Output first row;
            System.out.println( mapOfTrees.get( 0 ) );
        }
        while ( currentRow < mapOfTrees.size() - 1 )
        {
            currentCol += right;
            currentRow += down;

            // Wrap the row if the next index is too long
            String row = mapOfTrees.get( currentRow );
            while ( !( currentCol < row.length() ) )
            {
                row += row;
            }


            char icon = 'O';
            if ( row.charAt( currentCol ) == TREE )
            {
                treeCount++;
                icon = 'X';
            }

            if ( printMap )
            {
                // Output next row
                final char[] mapRow = row.toCharArray();
                mapRow[ currentCol ] = icon;
                System.out.println( String.valueOf( mapRow ) );
            }
        }
        return treeCount;
    }

    /**
     * Get the data for the question
     *
     * @return map of trees
     */
    private List<String> getData()
    {
        final URL resource = getClass().getClassLoader().getResource( getClass().getName() );
        final List<String> anActualTreeMap = new ArrayList<>();
        try
        {
            assert resource != null;
            try ( final Stream<String> stream = Files.lines( Paths.get( resource.getPath() ) ) )
            {
                stream.forEach( anActualTreeMap::add );
            }
        }
        catch ( final IOException e )
        {
            e.printStackTrace();
        }
        return anActualTreeMap;
    }
}
