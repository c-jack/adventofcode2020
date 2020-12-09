/*
 * Copyright (c) 5/12/2020 Chris Jackson (c-jack)
 * adventofcode.AOCUtils
 */
package utils;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Advent of Code Utils
 * Some static methods to help set up the challenge solutions
 *
 * @author chris.jackson
 */
public class AOCUtils
{

    /**
     * Get the data for the question
     *
     * @param resourceName name of the resource to load
     * @return list of String values, each representing a line from the resource
     */
    public static List<String> getData( final String resourceName )
    {
        final URL resource = AOCUtils.class.getClassLoader().getResource( resourceName );
        final List<String> dataStringList = new ArrayList<>();
        try
        {
            assert resource != null;
            try ( final Stream<String> stream = Files.lines( Paths.get( resource.getPath() ) ) )
            {
                stream.forEach( dataStringList::add );
            }
        }
        catch ( final IOException e )
        {
            e.printStackTrace();
        }
        return dataStringList;
    }

    /**
     * Get the data for the question
     *
     * @param resourceName name of the resource to load
     * @return list of Integer values, each representing a line from the resource
     */
    public static List<Integer> getIntegerData( final String resourceName )
    {
        final List<Integer> dataStringList = new ArrayList<>();
        for ( final String line : getData( resourceName ) )
        {
            dataStringList.add( Integer.valueOf( line ) );
        }

        return dataStringList;
    }

    /**
     * Get the data for the question
     *
     * @param resourceName name of the resource to load
     * @return list of Long values, each representing a line from the resource
     */
    public static List<Long> getLongData( final String resourceName )
    {
        final List<Long> dataStringList = new ArrayList<>();
        for ( final String line : getData( resourceName ) )
        {
            dataStringList.add( Long.valueOf( line ) );
        }

        return dataStringList;
    }
}
