package com.incadencecorp.unity.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.ConfigurationNode;
import org.apache.commons.configuration.tree.DefaultConfigurationNode;
import org.apache.commons.io.FileUtils;

import com.incadencecorp.unity.common.SettingType;

public class ConfigurationFile implements IConfigurationFile {

    /***************************
     * Private Member Variables *
     ***************************/

    private String _fileName;
    private XMLConfiguration _xmlDoc;

    // TODO: Implement Encryption
    // private Byte _key;
    // private Byte _IV;

    /***************************
     * Constructors *
     ***************************/

    public ConfigurationFile()
    {
        // TODO: intializeCrypto()
        _xmlDoc = new XMLConfiguration();
    }

    public ConfigurationFile(String fileName)
    {
        // TODO: intializeCrypto()
        open(fileName);
    }

    protected void intializeCrypto()
    {

        // TODO: IntializeCrypto stuff
    }

    /***************************
     * Public Properties *
     ***************************/

    public String getFileName()
    {
        return _fileName;
    }

    public void setFileName(String fileName)
    {
        this._fileName = fileName;
    }

    /***************************
     * Public Methods *
     ***************************/

    @Override
    public void open(String fileName)
    {
        try
        {
            Boolean rtn = false;
            // open file
            rtn = openFile(fileName);

            // success?
            if (rtn == true)
            {
                rtn = createRestoreFile(fileName);
            }
            else
            {
                // No; Do we have a Restore File?
                rtn = restoreFileExists(fileName);

                if (rtn == true)
                {
                    // Yes, restore
                    rtn = restoreFile(fileName);

                    // success?
                    if (rtn == true)
                    {
                        // yes open the restored file
                        rtn = openFile(fileName);
                    }
                    else
                    {
                        // no could not restore just open from the restore file itself
                        rtn = openFromRestoreFile(fileName);
                    }
                }
                else
                {
                    // create new file
                    rtn = createNewFile(fileName);
                }
            }
        }
        catch (Exception ex)
        {
            // Handle Error
        }

    }

    protected Boolean openFile(String fileName)
    {

        File file = new File(fileName);

        try
        {
            if (file.exists())
            {
                // load xml doc
                _xmlDoc = new XMLConfiguration(fileName);

                // Persist File name
                _fileName = fileName;
                // return success
                return true;
            }
            else
            {
                // No File return false
                return false;
            }
        }
        catch (Exception ex)
        {
            return false;
        }
    }

    protected Boolean openFromRestoreFile(String fileName)
    {
        try
        {
            File file = new File(fileName);
            String restoreFileName = file.getParent() + "\\~" + file.getName();

            // load xml doc
            _xmlDoc = new XMLConfiguration(restoreFileName);

            // Persist Filename (use the actual Filename, so that when we save we save back to the Filename, not over the
            // Restore Filename.
            // This is a last ditch effort to restore.)
            _fileName = fileName;

            // success;
            return true;

        }
        catch (Exception ex)
        {
            return false;
        }
    }

    protected Boolean createNewFile(String fileName)
    {
        try
        {
            // create xml doc
            _xmlDoc = new XMLConfiguration();
            _xmlDoc.setFileName(fileName);

            // save
            _xmlDoc.save();

            // persist file name
            _fileName = fileName;

            // success
            return true;
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    protected Boolean createRestoreFile(String fileName)
    {
        try
        {
            // Copy the original File to the RestoreFilename
            File file = new File(fileName);
            String restoreFileName = file.getParent() + "\\~" + file.getName();
            File fileRestore = new File(restoreFileName);
            FileUtils.copyFile(file, fileRestore);

            // success
            return true;

        }
        catch (Exception ex)
        {
            return false;
        }
    }

    protected Boolean restoreFile(String fileName)
    {
        try
        {
            // Copy the original File to the RestoreFilename
            File file = new File(fileName);
            String restoreFileName = file.getParent() + "\\~" + file.getName();
            File fileRestore = new File(restoreFileName);
            FileUtils.copyFile(fileRestore, file);

            // success
            return true;

        }
        catch (Exception ex)
        {
            return false;
        }
    }

    protected Boolean restoreFileExists(String fileName)
    {
        try
        {
            // get restore file name
            File file = new File(fileName);
            String restoreFileName = file.getParent() + "\\~" + file.getName();
            File fileRestore = new File(restoreFileName);

            // return if it exists or not
            return fileRestore.exists();

        }
        catch (Exception ex)
        {
            return false;
        }
    }

    @Override
    public void save()
    {
        try
        {
            if (_fileName != null)
            {
                _xmlDoc.save();
            }
        }
        catch (Exception ex)
        {
            // ex.printStackTrace();
        }
    }

    @Override
    public void setSetting(String settingPath, String value, SettingType type)
    {
        // This method checks to see whether or not the Value is located
        // ' at the end of the Path. If so, it changes the value. If not, then it adds the
        // ' value. If the path doesn't exist, then it creates the path. The path can be
        // ' passed in as either "." delimited, "\" delimited, or "/" delimited; the software
        // ' is tolerant of these conventions.
        ConfigurationNode node;

        // 1. Normalize Path

        // 2. Get the Setting node (creating if necessary)
        node = getSettingNode(settingPath, true);
        // save file
        save();

        // 3. Set the Attributes
        // XmlHelper xmlHelper = new XmlHelper();

        if (node != null)
        {
            Boolean rtn = false;

            // Update Value
            if (type == SettingType.ST_ENCRYPTED_STRING)
            {
                // TODO: encryption stuff
                rtn = true;
            }
            else
            {
                // rtn = xmlHelper.createOrUpdateAttribute(_xmlDoc, node, "value", value);
                rtn = createOrUpdateAttribute(settingPath, "value", value);
                if (rtn == false) return;
            }

            // update Path
            // rtn = xmlHelper.createOrUpdateAttribute(_xmlDoc, node, "path", settingPath);
            rtn = createOrUpdateAttribute(settingPath, "path", settingPath);
            if (rtn == false) return;

            // update type
            // rtn = xmlHelper.createOrUpdateAttribute(_xmlDoc, node, "type", type.getSettingType().toString());
            rtn = createOrUpdateAttribute(settingPath, "type", type.getSettingType().toString());
            if (rtn == false) return;

            // save file
            save();
        }

    }

    @Override
    public String getSetting(String settingPath, String defaultValue, SettingType type, Boolean setIfNotFound)
    {

        try
        {
            ConfigurationNode node;
            ConfigurationNode attribute;

            // 1. Get the Setting node (creating if necessary)
            node = getSettingNode(settingPath, true);

            // 2. Get the value (setting if necessary)
            List<ConfigurationNode> attributeList = node.getAttributes("value");

            if (attributeList.isEmpty())
            {
                // not found

                // have we been asked to set it to default value if not found?
                if (setIfNotFound == true)
                {
                    setSetting(settingPath, defaultValue, type);
                }

                // return default value
                return defaultValue;
            }
            else
            {
                attribute = attributeList.get(0);

                if (node.getAttributes("type").get(0).getValue() != null)
                {
                    // found
                    // get the integer for the setting type
                    try
                    {
                        String value = (String) node.getAttributes("type").get(0).getValue();
                        type.setSettingType(Integer.parseInt(value));
                    }
                    catch (Exception ex)
                    {
                        // failed, set setting type to unknown
                        type = SettingType.ST_UNKNOWN;
                    }

                    // check if value is encrypted
                    if (type == SettingType.ST_ENCRYPTED_STRING)
                    {
                        // TODO: Decrypt String
                    }
                    else
                    {
                        return attribute.getValue().toString();
                    }

                }
                else
                {
                    // Have we been asked to set it to default value if not found?
                    if (setIfNotFound == true)
                    {
                        setSetting(settingPath, defaultValue, type);
                    }

                    return defaultValue;
                }
            }

        }
        catch (Exception ex)
        {
            return defaultValue;
        }

        return null;
    }

    @Override
    public SettingType getSettingType(String settingPath)
    {
        // This method locates the setting and returns back the SettingType.
        SettingType type = SettingType.ST_UNKNOWN;

        // 1. Get the Setting node (does not create if it does not exist)
        ConfigurationNode node = getSettingNode(settingPath, false);
        if (node != null)
        {

            try
            {
                String value = (String) node.getAttributes("type").get(0).getValue();
                type.setSettingType(Integer.parseInt(value));
                return type;
            }
            catch (Exception ex)
            {
                // failed, set setting type to unknown
                return type = SettingType.ST_UNKNOWN;
            }

        }
        else
        {
            return type = SettingType.ST_UNKNOWN;
        }

    }

    @Override
    public void deleteSetting(String settingPath)
    {
        // Remove the Setting Node
        removeSettingNode(settingPath);

        // Persist the file.
        save();
    }

    @Override
    public ConfigurationNode getSection(String sectionPath)
    {
        ConfigurationNode node = null;
        // 1. Get the Section node (will not be created if it does not exist)
        node = getSectionNode(sectionPath);

        // 2. Return the XML
        if (node == null)
        {
            return null;
        }
        else
        {

            // TODO: convert type ConfigurationNode to string
            try
            {

                return node;

            }
            catch (Exception ex)
            {
                return null;
            }
        }
    }

    @Override
    public String[] getSectionList(String sectionPath)
    {
        return null;
    }

    @Override
    public void deleteSection(String sectionPath)
    {
        removeSectionNode(sectionPath);
        save();

    }

    protected void removeSettingNode(String settingPath)
    {
        // normalize path
        settingPath = settingPath.replace("\\", "/");
        settingPath = settingPath.replace(".", "/");

        String[] pathParts = settingPath.split("/");
        int pathPartLength = pathParts.length;

        // get section node that is parent of setting node
        int endIndex = settingPath.lastIndexOf("/");
        String sectionPath = settingPath.substring(0, endIndex);
        ConfigurationNode sectionNode = getSectionNode(sectionPath);

        // find the child setting node
        List<ConfigurationNode> settingNodeList = sectionNode.getChildren("setting");

        if (settingNodeList != null)
        {
            // loop through all of child nodes to find matching name for setting
            for (ConfigurationNode element : settingNodeList)
            {
                // get name attribute and compare it to settingPath
                String value = element.getAttribute(0).getValue().toString();
                if (value.equals(pathParts[pathPartLength - 1]))
                {
                    // delete it
                    sectionNode.removeChild(element);
                    break;
                }
            }
        }
        else
        {
            // the setting node does not exist
            return;
        }

    }

    protected void removeSectionNode(String sectionPath)
    {
        // normalize path
        sectionPath = sectionPath.replace("\\", "/");
        sectionPath = sectionPath.replace(".", "/");

        String[] pathParts = sectionPath.split("/");
        int pathPartLength = pathParts.length;

        // get section node that is parent of section node to be removed
        int endIndex = sectionPath.lastIndexOf("/");
        String sectionPathParent = sectionPath.substring(0, endIndex);
        ConfigurationNode sectionNodeParent = getSection(sectionPathParent);

        // find the section node to be removed
        List<ConfigurationNode> sectionNodeList = sectionNodeParent.getChildren("setting");

        if (sectionNodeList != null)
        {
            // loop through all of child nodes to find matching name for setting
            for (ConfigurationNode element : sectionNodeList)
            {
                // get name attribute and compare it to settingPath
                String value = element.getAttribute(0).getValue().toString();
                if (value.equals(pathParts[pathPartLength - 1]))
                {
                    // delete the section node
                    sectionNodeParent.removeChild(element);
                    break;
                }
            }
        }
        else
        {
            // the setting node does not exist
            return;
        }

    }

    protected ConfigurationNode getSettingNode(String settingPath, Boolean createIfMissing)
    {
        // normalize path
        settingPath = settingPath.replace("\\", "/");
        settingPath = settingPath.replace(".", "/");

        ConfigurationNode settingNode = null;

        String[] pathParts = settingPath.split("/");
        List<String> pathPartsList = new ArrayList<String>(Arrays.asList(pathParts));

        int pathPartLength = pathParts.length;

        // get section node that contains the setting node
        int endIndex = settingPath.lastIndexOf("/");
        String sectionPath = settingPath.substring(0, endIndex);
        ConfigurationNode sectionNode = getSectionNode(sectionPath);

        if (sectionNode == null)
        {

            if (createIfMissing)
            {

                ConfigurationNode rootNode = _xmlDoc.getRootNode();

                // create new section node
                sectionNode = newSectionNode(pathPartsList, rootNode);

                // create setting node
                DefaultConfigurationNode node = new DefaultConfigurationNode("setting");

                // get setting name
                String settingName = pathParts[pathPartLength - 1];

                // create attributes
                DefaultConfigurationNode attributeNode = new DefaultConfigurationNode("name", settingName);

                node.addAttribute(new HierarchicalConfiguration.Node(attributeNode));
                sectionNode.addChild(new HierarchicalConfiguration.Node(node));
                // removesave
                save();
                return node;
            }
            else
            {

                return null;
            }

        }
        else
        {
            // a section node was found, get list of child nodes in sectionNodes
            List<ConfigurationNode> settingNodeList = sectionNode.getChildren("setting");

            if (settingNodeList.isEmpty())
            {
                // section node has no children, create one or return null

                if (createIfMissing)
                {

                    // no setting nodes in section, create one and return
                    // create setting node
                    DefaultConfigurationNode node = new DefaultConfigurationNode("setting");

                    // get setting name
                    String settingName = pathPartsList.get(pathPartLength - 1);

                    // create attributes
                    DefaultConfigurationNode attributeNode = new DefaultConfigurationNode("name", settingName);

                    node.addAttribute(new HierarchicalConfiguration.Node(attributeNode));
                    sectionNode.addChild(new HierarchicalConfiguration.Node(node));

                    return node;
                }
                else
                {

                    return null;
                }

            }
            else
            {
                // section node has children, loop through all of child nodes to find setting node that has the input setting
                // path

                Boolean settingNodeExists = false;

                for (ConfigurationNode element : settingNodeList)
                {
                    // get name attribute and compare it to settingPath
                    String value = element.getAttribute(0).getValue().toString();
                    if (value.equals(pathPartsList.get(pathPartLength - 1)))
                    {
                        // save it
                        settingNode = element;
                        settingNodeExists = true;
                        break;
                    }
                }

                // check if the setting node was found
                if (settingNodeExists)
                {
                    return settingNode;
                }
                else
                {
                    // no setting nodes matched, create one and return or return null

                    if (createIfMissing)
                    {

                        // create setting node
                        DefaultConfigurationNode node = new DefaultConfigurationNode("setting");

                        // get setting name
                        String settingName = pathPartsList.get(pathPartLength - 1);

                        // create attributes
                        DefaultConfigurationNode attributeNode = new DefaultConfigurationNode("name", settingName);

                        node.addAttribute(new HierarchicalConfiguration.Node(attributeNode));
                        sectionNode.addChild(new HierarchicalConfiguration.Node(node));
                        // removesave
                        save();

                        return node;
                    }
                    else
                    {
                        return null;
                    }
                }
            }
        }
    }

    protected ConfigurationNode newSectionNode(List<String> pathPartsList, ConfigurationNode rootNode)
    {

        int pathPartsLength = pathPartsList.size();

        if (pathPartsLength < 2)
        {
            // the root node is the section node because the last element in pathParts is the setting node name
            // exit recursive function and return
            return rootNode;

        }
        else
        {
            List<ConfigurationNode> childNodeList = rootNode.getChildren("section");

            if (childNodeList.isEmpty())
            {

                // create node because it does not exist
                DefaultConfigurationNode sectionNode = new DefaultConfigurationNode("section");
                // create attributes
                DefaultConfigurationNode attributeNode = new DefaultConfigurationNode("name", pathPartsList.get(0));

                sectionNode.addAttribute(new HierarchicalConfiguration.Node(attributeNode));
                rootNode.addChild(new HierarchicalConfiguration.Node(sectionNode));

                // select new section node
                childNodeList = rootNode.getChildren("section");
                for (ConfigurationNode node : childNodeList)
                {

                    String value = node.getAttribute(0).getValue().toString();
                    if (value.equals(pathPartsList.get(0)))
                    {

                        // section node exists
                        rootNode = node;
                    }

                }

                // update pathPartsList and restart from beginning
                pathPartsList.remove(0);
                return newSectionNode(pathPartsList, rootNode);

            }
            else
            {

                // check if sectionNode is in childNodeList
                Boolean sectionNodeExists = false;

                for (ConfigurationNode node : childNodeList)
                {

                    String value = node.getAttribute(0).getValue().toString();
                    if (value.equals(pathPartsList.get(0)))
                    {

                        // section node exists
                        rootNode = node;
                        sectionNodeExists = true;
                    }

                }

                if (sectionNodeExists)
                {

                    // update pathPartsList and restart from beginning
                    pathPartsList.remove(0);
                    return newSectionNode(pathPartsList, rootNode);

                }
                else
                {

                    // create node because it does not exist
                    DefaultConfigurationNode sectionNode = new DefaultConfigurationNode("section");
                    // create attributes
                    DefaultConfigurationNode attributeNode = new DefaultConfigurationNode("name", pathPartsList.get(0));

                    sectionNode.addAttribute(new HierarchicalConfiguration.Node(attributeNode));
                    rootNode.addChild(new HierarchicalConfiguration.Node(sectionNode));

                    // select new section node
                    childNodeList = rootNode.getChildren("section");
                    for (ConfigurationNode node : childNodeList)
                    {

                        String value = node.getAttribute(0).getValue().toString();
                        if (value.equals(pathPartsList.get(0)))
                        {

                            // section node exists
                            rootNode = node;
                        }

                    }

                    // update pathPartsList and restart from beginning
                    pathPartsList.remove(0);
                    return newSectionNode(pathPartsList, rootNode);
                }
            }
        }
    }

    protected ConfigurationNode getSectionNode(String sectionPath)
    {
        // normalize path
        sectionPath = sectionPath.replace("\\", "/");
        sectionPath = sectionPath.replace(".", "/");

        String[] pathParts = sectionPath.split("/");

        Boolean sectionExists = false;
        ConfigurationNode sectionNode = _xmlDoc.getRootNode();

        for (String pathPart : pathParts)
        {
            sectionExists = false;
            // Retrieve list of section nodes
            List<ConfigurationNode> nodeList = sectionNode.getChildren("section");
            // loop through all of child nodes to find matching name for pathPart
            for (ConfigurationNode element : nodeList)
            {
                // get name attribute and compare it to settingPath
                String value = element.getAttribute(0).getValue().toString();
                if (value.equals(pathPart))
                {
                    // save it
                    sectionNode = element;
                    sectionExists = true;
                    break;
                }
            }
        }

        // return section node if found. return null if nothing was found
        if (sectionExists)
        {
            return sectionNode;
        }
        else
        {
            return null;
        }
    }

    /*
     * private static final String[] EMPTY_STRING_ARRAY = new String[0];
     * 
     * private String[] settingPathPartsToSectionPathParts(String[] pathParts) { int pathPartsLength = pathParts.length;
     * List<String> list = new ArrayList<String>(Arrays.asList(pathParts)); list.remove(pathPartsLength - 1); //
     * list.removeAll(Arrays.asList("a")); return list.toArray(EMPTY_STRING_ARRAY); }
     */

    protected Boolean hasAttribute(ConfigurationNode node, String attributeName)
    {
        try
        {
            List<ConfigurationNode> attributeList = node.getAttributes(attributeName);
            if (attributeList.isEmpty())
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        catch (Exception ex)
        {
            return false;
        }
    }

    protected Boolean createOrUpdateAttribute(String settingPath, String attributeName, String value)
    {
        try
        {
            ConfigurationNode node = getSettingNode(settingPath, true);
            // Do we have a Doc?
            if (_xmlDoc != null)
            {
                // Do we have a Node?
                if (node != null)
                {

                    // does the attribute already exist?
                    if (hasAttribute(node, attributeName))
                    {
                        // remove the Attribute
                        node.removeAttribute(attributeName);
                        // set attribute
                        node.addAttribute(new HierarchicalConfiguration.Node(attributeName, value));
                        // Return True; Success
                        return true;
                    }
                    else
                    {
                        // create the Attribute
                        node.addAttribute(new HierarchicalConfiguration.Node(attributeName, value));
                        _xmlDoc.save();
                        // Return True; Success
                        return true;
                    }
                }
                else
                {
                    // no node, return false
                    return false;
                }
            }
            else
            {
                // no xml doc, return false
                return false;
            }
        }
        catch (Exception ex)
        {
            // Return False (Failed)
            return false;
        }
    }
}
