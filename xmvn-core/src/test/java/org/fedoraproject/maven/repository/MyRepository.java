/*-
 * Copyright (c) 2013 Red Hat, Inc.
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
package org.fedoraproject.maven.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.fedoraproject.maven.model.Artifact;

@Component( role = Repository.class, hint = "my-type" )
public class MyRepository
    implements Repository
{
    @Override
    public void configure( List<String> artifactTypes, Properties properties, Xpp3Dom configuration )
    {
        assertNotNull( properties );
        assertNotNull( configuration );
        assertEquals( "bar", properties.get( "foo" ) );
        assertNull( properties.get( "baz" ) );
    }

    @Override
    public Path getPrimaryArtifactPath( Artifact artifact )
    {
        fail( "getPrimaryArtifactPath() was not expected to be called" );
        throw null;
    }

    @Override
    public List<Path> getArtifactPaths( Artifact artifact )
    {
        fail( "getArtifactPaths() was not expected to be called" );
        throw null;
    }

    @Override
    public List<Path> getArtifactPaths( List<Artifact> artifact )
    {
        fail( "getArtifactPaths() was not expected to be called" );
        throw null;
    }
}