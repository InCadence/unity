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

package com.incadencecorp.unity.common.connectors;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * This implementation is for reading property files from a JAR's resources.
 * Because the properties is a resource is read only and therefore setSetting
 * will always return <code>false</code>.
 * 
 * @author n78554
 */
public class ResourcePropertyConnector extends PropertyConnector {

    @Override
    protected InputStream getPropertyInputStream(String key)
    {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(key);
    }

    @Override
    protected OutputStream getPropertyOutputStream(String key)
    {
        // Read Only
        return null;
    }

}
