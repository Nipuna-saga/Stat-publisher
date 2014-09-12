/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

package org.wso2.carbon.stat.publisher.conf;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.wso2.carbon.stat.publisher.util.StatPublisherException;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Read configuration values from jmx and carbon xml files.
 */
public class ReadJMXConfiguration {

    private static Logger logger = Logger.getLogger(ReadJMXConfiguration.class);
    private boolean StartRMIServer;
    private String HostName;
    private String RMIRegistryPort;
    private String RMIServerPort;
    private String offSet;

    /**
     * ReadJMXConfiguration Class constructor load xml files and read values
     */
    public ReadJMXConfiguration() throws StatPublisherException {

        try {
            /**
             * Load jmx.xml file
             */
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document jmxDocument;
            String JMXFilePath = XMLConfigurationConstants.JMX_FILE_PATH;

            File jmxFilePath= new File(JMXFilePath);
            if (!jmxFilePath.exists()) {
                logger.error("jmx.xml doesn't exists!!!");
            } else {
                jmxDocument = docBuilder.parse(JMXFilePath);
                jmxDocument.getDocumentElement().normalize();
                String jmxRootNode = jmxDocument.getDocumentElement().getNodeName();
                NodeList jmxDataList = jmxDocument.getElementsByTagName(jmxRootNode);

                String StartRMIServerValue =
                         ((Element) jmxDataList.item(0)).getElementsByTagName("StartRMIServer").
                                item(0).getChildNodes().item(0).getTextContent();
                String nodeValue = StartRMIServerValue.trim();
                this.StartRMIServer = Boolean.parseBoolean(nodeValue);

                String HostNameValue =
                        ((Element) jmxDataList.item(0)).getElementsByTagName("HostName").
                                item(0).getChildNodes().item(0).getTextContent();
                this.HostName = HostNameValue.trim();

                /**
                 * Load carbon.xml file
                 */
                Document carbonDocument;
                String carbonFilePath = XMLConfigurationConstants.CARBON_FILE_PATH;

                File carbonFile = new File(carbonFilePath);
                if (!carbonFile.exists()) {
                    logger.error("carbon.xml doesn't exists!!!");
                } else {
                    carbonDocument = docBuilder.parse(carbonFilePath);
                    carbonDocument.getDocumentElement().normalize();
                    String carbonRootNode = carbonDocument.getDocumentElement().getNodeName();
                    NodeList carbonDataList = carbonDocument.getElementsByTagName(carbonRootNode);

                    String RMIRegistryPortValue =
                            ((Element) carbonDataList.item(0)).getElementsByTagName("RMIRegistryPort").
                                    item(0).getChildNodes().item(0).getTextContent();
                    this.RMIRegistryPort = RMIRegistryPortValue.trim();

                    String RMIServerPortValue =
                            ((Element) carbonDataList.item(0)).getElementsByTagName("RMIServerPort").
                                    item(0).getChildNodes().item(0).getTextContent();
                    this.RMIServerPort = RMIServerPortValue.trim();

                    String offSetValue =
                            ((Element) carbonDataList.item(0)).getElementsByTagName("Offset").
                                    item(0).getChildNodes().item(0).getTextContent();
                    this.offSet = offSetValue.trim();
                }
            }
        } catch (ParserConfigurationException e) {
            throw new StatPublisherException("Indicate configuration error!", e);
        } catch (SAXException e) {
            throw new StatPublisherException("Indicate a general SAX error or warning!", e);
        } catch (IOException e) {
            throw new StatPublisherException("Indicate file loading error!", e);
        }
    }

    /**
     * Get value of StartRMIServer
     * @return StartRMIServer Value
     */
    public boolean isStartRMIServer() {
        return StartRMIServer;
    }

    /**
     * Get value of RMIServerPort
     * @return RMIServerPort
     */
    public String getRMIServerPort() {
        return RMIServerPort;
    }

    /**
     * Get value of HostName
     * @return HostName
     */
    public String getHostName() {
        return HostName;
    }

    /**
     * Get value of RMIRegistryPort
     * @return RMIRegistryPort
     */
    public String getRMIRegistryPort() {
        return RMIRegistryPort;
    }

    /**
     * Get value of offSet
     * @return offSet
     */
    public String getOffSet() {
        return offSet;
    }

}
