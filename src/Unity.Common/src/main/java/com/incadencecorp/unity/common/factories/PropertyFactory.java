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

package com.incadencecorp.unity.common.factories;

import java.util.Map;

import com.incadencecorp.unity.common.IConfigurationsConnector;

/**
 * Factory for reading properties into a Map.
 * 
 * @author n78554
 */
public class PropertyFactory {

    private IConfigurationsConnector connector;

    /**
     * Constructs this factory with a connector.
     * 
     * @param connector
     */
    public PropertyFactory(IConfigurationsConnector connector)
    {
        this.connector = connector;
    }

    /**
     * @param config
     * @return all the properties available for the provided configuration file.
     */
    public Map<String, String> getProperties(String config)
    {
        return connector.getSettings(config);
    }

}
