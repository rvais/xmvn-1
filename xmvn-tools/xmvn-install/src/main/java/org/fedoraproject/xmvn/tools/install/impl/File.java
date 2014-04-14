/*-
 * Copyright (c) 2014 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fedoraproject.xmvn.tools.install.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * An abstract class denoting a file entry in a package with all relevant attributes.
 * <p>
 * A file does not necessairly mean a regular file, it could be for example a directory or a device file.
 * 
 * @author Mikolaj Izdebski
 */
abstract class File
{
    /**
     * Path to target file. This path must be relative to buildroot (must not be absolute).
     */
    private final Path targetPath;

    /**
     * Unix access mode (an integer in range from 0 to 0777).
     */
    private final int accessMode;

    /**
     * Install the file into specified location.
     * <p>
     * Implementations of this method can assume that all parent directory of target file already exists. Access mode of
     * target file doesn't have to be set as it will be manipulated with other means.
     * 
     * @param installRoot absolute path of root directory under which the file should be installed
     * @throws IOException
     */
    protected abstract void installContents( Path installRoot )
        throws IOException;

    /**
     * Create a file object with specified path and default access mode of 0644.
     * 
     * @param targetPath
     */
    public File( Path targetPath )
    {
        this( targetPath, 0644 );
    }

    /**
     * Create a file object with specified path and access mode.
     * 
     * @param targetPath
     * @param accessMode
     */
    public File( Path targetPath, int accessMode )
    {
        if ( targetPath.isAbsolute() )
            throw new IllegalArgumentException( "target path must not be absolute" );
        if ( accessMode < 0 || accessMode > 0777 )
            throw new IllegalArgumentException( "access mode must be in range from 0 to 777" );

        this.targetPath = targetPath;
        this.accessMode = accessMode;
    }

    /**
     * Return path to target file. Returned patjh is always relative to buildroot (never absolute).
     * 
     * @return file target path (never {@code null})
     */
    public Path getTargetPath()
    {
        return targetPath;
    }

    /**
     * Get Unix access mode for this file.
     * 
     * @return Unix access mode (an integer in range from 0 to 0777)
     */
    public int getAccessMode()
    {
        return accessMode;
    }

    /**
     * Install file into specified root directory.
     * 
     * @param installRoot
     * @throws IOException
     */
    public void install( Path installRoot )
        throws IOException
    {
        installRoot = installRoot.toAbsolutePath();

        if ( Files.exists( installRoot ) )
        {
            if ( !Files.isDirectory( installRoot ) )
                throw new IOException( "Installation root " + installRoot + " already exists and is not a directory." );
        }
        else
        {
            Files.createDirectory( installRoot );
        }

        Path targetAbsolutePath = installRoot.resolve( targetPath );
        Files.createDirectories( targetAbsolutePath.getParent() );

        installContents( installRoot );
    }
}
