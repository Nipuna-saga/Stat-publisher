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
 * Read stream configuration values from configurationData xml file.
 */
public class ReadStreamConfiguration {
    private static Logger logger = Logger.getLogger(ReadStreamConfiguration.class);
    private String versionMessage;
    private String versionAck;
    private String forwardSlash;
    private String trustStorePassword;
    private String versionSystemStatistic;
    private String versionMBStatistic;

    /**
     * ReadStreamConfiguration Class constructor load configurationData.xml and read values
     */
    public ReadStreamConfiguration() throws StatPublisherException {

        try {
            /**
             * Load configurationData.xml file
             */
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document document;
            String filePath = XMLConfigurationConstants.CONF_DATA_FILE_PATH;
            File file = new File(filePath);

            if (!file.exists()) {
                logger.error("configurationData.xml doesn't exists!!");
            } else {
                document = docBuilder.parse(filePath);
                document.getDocumentElement().normalize();
                String rootNode = document.getDocumentElement().getNodeName();
                NodeList dataList = document.getElementsByTagName(rootNode);

                String versionMessageValue =
                        ((Element) dataList.item(0)).getElementsByTagName("versionMessage").
                                item(0).getChildNodes().item(0).getTextContent();
                this.versionMessage = versionMessageValue.trim();

                String versionAckValue =
                        ((Element) dataList.item(0)).getElementsByTagName("versionAck").
                                item(0).getChildNodes().item(0).getTextContent();
                this.versionAck = versionAckValue.trim();

                String forwardSlashValue =
                        ((Element) dataList.item(0)).getElementsByTagName("forwardSlash").
                                item(0).getChildNodes().item(0).getTextContent();
                this.forwardSlash = forwardSlashValue.trim();

                String trustStorePasswordValue =
                        ((Element) dataList.item(0)).getElementsByTagName("trustStorePassword").
                                item(0).getChildNodes().item(0).getTextContent();
                this.trustStorePassword = trustStorePasswordValue.trim();

                String versionSystemStatisticValue =
                        ((Element) dataList.item(0)).getElementsByTagName("versionSystemStatistic").
                                item(0).getChildNodes().item(0).getTextContent();
                this.versionSystemStatistic = versionSystemStatisticValue.trim();

                String versionMBStatisticValue =
                        ((Element) dataList.item(0)).getElementsByTagName("versionMBStatistic").
                                item(0).getChildNodes().item(0).getTextContent();
                this.versionMBStatistic = versionMBStatisticValue.trim();

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
     * Get message version
     * @return versionMessage
     */
    public String getVersionMessage() {
        return versionMessage;
    }

    /**
     * Get acknowledge packet version
     * @return versionMessage
     */
    public String getVersionAck() {
        return versionAck;
    }

    /**
     * Get forward slash
     * @return forwardSlash
     */
    public String getForwardSlash() {
        return forwardSlash;
    }

    /**
     * Get trustStore password
     * @return trustStorePassword
     */
    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    /**
     * Get system statistic version
     * @return versionSystemStatistic
     */
    public String getVersionSystemStatistic() {
        return versionSystemStatistic;
    }

    /**
     * Get message broker statistic version
     * @return versionMBStatistic
     */
    public String getVersionMBStatistic() {
        return versionMBStatistic;
    }
}
