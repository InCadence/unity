/*-----------------------------------------------------------------------------'
 Copyright 2016 - InCadence Strategic Solutions Inc., All Rights Reserved

 Notwithstanding any contractor copyright notice, the Government has Unlimited
 Rights in this work as defined by DFARS 252.227-7013 and 252.227-7014.  Use
 of this work other than as specifically authorized by these DFARS Clauses may
 violate Government rights in this work.

 DFARS Clause reference: 252.227-7013 (a)(16) and 252.227-7014 (a)(16)
 Unlimited Rights. The Government has the right to use, modify, reproduce,
 perform, display, release or disclose this computer software and to have or
 authorize others to do so.

 Distribution Statement D. Distribution authorized to the Department of
 Defense and U.S. DoD contractors only in support of U.S. DoD efforts.
 -----------------------------------------------------------------------------*/

package com.incadencecorp.unity.common;

import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import com.incadencecorp.unity.common.connectors.FilePropertyConnector;
import com.incadencecorp.unity.common.connectors.MemoryConnector;
import com.incadencecorp.unity.common.connectors.ResourcePropertyConnector;

/**
 * These test ensure proper operation of the {@link MemoryConnector}.
 * 
 * @author n78554
 */
public class ConnectorTest {

    private static final String PARAM1 = "param1";
    private static final String PARAM2 = "param2";
    private static final String DEFAULT_VALUE = "";
    private static final String CONFIG_NAME = "test";

    /**
     * Test Memory Connector
     * 
     * @throws Exception
     */
    @Test
    public void testMemoryConnector() throws Exception
    {
        testConnector(new MemoryConnector());
    }

    /**
     * Test Resource Property Connector
     * 
     * @throws Exception
     */
    @Test
    public void testResourcePropertyConnector() throws Exception
    {
        testConnector(new ResourcePropertyConnector());
    }

    /**
     * Test File Property Connector.
     * 
     * @throws Exception
     */
    @Test
    public void testFilePropertyConnector() throws Exception
    {
        testConnector(new FilePropertyConnector(Paths.get("src", "test", "resources")));
    }

    private void testConnector(IConfigurationsConnector connector) throws Exception
    {
        String value1 = UUID.randomUUID().toString();
        String value2 = UUID.randomUUID().toString();

        connector.setSetting(CONFIG_NAME, PARAM1, value1, SettingType.ST_STRING);
        connector.setSetting(CONFIG_NAME, PARAM2, DEFAULT_VALUE, SettingType.ST_STRING);

        Assert.assertEquals(value1, connector.getSetting(CONFIG_NAME, PARAM1, null, SettingType.ST_STRING, false));
        Assert.assertEquals(DEFAULT_VALUE, connector.getSetting(CONFIG_NAME, PARAM2, null, SettingType.ST_STRING, false));

        connector.setSetting(CONFIG_NAME, PARAM2, value2, SettingType.ST_STRING);

        Assert.assertEquals(value1, connector.getSetting(CONFIG_NAME, PARAM1, null, SettingType.ST_STRING, false));
        Assert.assertEquals(value2, connector.getSetting(CONFIG_NAME, PARAM2, null, SettingType.ST_STRING, false));

        Map<String, String> settings = connector.getSettings(CONFIG_NAME);

        Assert.assertEquals(2, settings.size());
        Assert.assertEquals(value1, settings.get(PARAM1));
        Assert.assertEquals(value2, settings.get(PARAM2));

        Assert.assertEquals(0, connector.getSettings(UUID.randomUUID().toString()).size());
        
    }

}
