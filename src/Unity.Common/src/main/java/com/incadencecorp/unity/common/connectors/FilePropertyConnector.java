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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This implementation is for reading property files from the file system.
 *
 * @author n78554
 */
public class FilePropertyConnector extends PropertyConnector {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilePropertyConnector.class);

    private String configPath;

    /**
     * Use the default location for configuration files.
     */
    public FilePropertyConnector()
    {
        initialize(Paths.get("config"));
    }

    /**
     * Specify the location of configuration files.
     * 
     * @param path location of the configuration files.
     */
    public FilePropertyConnector(String path)
    {
        if (path == null)
        {
            initialize(Paths.get("config"));
        }
        else
        {
            initialize(Paths.get(path));
        }
    }

    /**
     * Specify the location of configuration files.
     * 
     * @param path location of the configuration files.
     */
    public FilePropertyConnector(Path path)
    {
        initialize(path);
    }

    /**
     * @return the full path of the directory being used to store configuration
     *         files.
     */
    public String getFullPath()
    {
        return configPath;
    }

    @Override
    protected InputStream getPropertyInputStream(String key) throws FileNotFoundException
    {
        return new FileInputStream(configPath + key);
    }

    @Override
    protected OutputStream getPropertyOutputStream(String key) throws IOException
    {

        File file = new File(configPath + key);

        if (!file.exists())
        {

            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        return new FileOutputStream(file);

    }

    private void initialize(Path path)
    {
        configPath = path.toString();

        if (configPath != null)
        {

            // Yes; Value contains separator?
            if (!configPath.endsWith(File.separator))
            {
                // No; Append separator
                configPath = path.toString() + File.separator;
            }

        }
        else
        {
            configPath = "config" + File.separator;
        }

        LOGGER.info("Configuration Location: {}", this.configPath);
    }

}
