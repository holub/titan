package com.thinkaurelius.titan.graphdb;

import com.thinkaurelius.titan.core.PropertyKey;
import com.thinkaurelius.titan.core.TitanVertex;
import com.thinkaurelius.titan.diskstorage.configuration.ModifiableConfiguration;
import com.thinkaurelius.titan.diskstorage.configuration.WriteConfiguration;
import com.thinkaurelius.titan.graphdb.configuration.GraphDatabaseConfiguration;
import com.thinkaurelius.titan.graphdb.serializer.SpecialInt;
import com.thinkaurelius.titan.graphdb.serializer.SpecialIntSerializer;
import org.junit.Test;

import static com.thinkaurelius.titan.graphdb.configuration.GraphDatabaseConfiguration.CUSTOM_ATTRIBUTE_CLASS;
import static com.thinkaurelius.titan.graphdb.configuration.GraphDatabaseConfiguration.CUSTOM_SERIALIZER_CLASS;
import static org.junit.Assert.assertEquals;

/**
 * Created by aholub on 12/22/14.
 */
public class TitanGraphAttributesTest extends TitanGraphTest {
    @Override
    protected boolean isLockingOptimistic() {
        return true;
    }

    @Override
    public WriteConfiguration getConfiguration() {
        ModifiableConfiguration config = GraphDatabaseConfiguration.buildConfiguration();
        config.set(GraphDatabaseConfiguration.STORAGE_BACKEND,"inmemory");
        return config.getConfiguration();
    }


    @Test
    public void testCustomAttribute() {
        clopen(option(CUSTOM_ATTRIBUTE_CLASS,"attribute10"),SpecialInt.class.getCanonicalName(),
                option(CUSTOM_SERIALIZER_CLASS,"attribute10"),SpecialIntSerializer.class.getCanonicalName());

        PropertyKey sint = makeKey("int", SpecialInt.class);
        finishSchema();

        clopen();
        TitanVertex v = tx.addVertex();
        v.setProperty("name", "XXX");
        v.setProperty(sint, new SpecialInt(10));

        tx.commit();

        assertEquals("XXX", v.getProperty("name"));
        assertEquals(10, v.<SpecialInt>getProperty(sint).getValue());
    }
}
