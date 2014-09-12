package org.wso2.carbon.stat.publisher.util;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.wso2.carbon.stat.publisher.conf.JMXConfiguration;
import org.wso2.carbon.stat.publisher.conf.XMLConfigurationConstants;
import org.wso2.carbon.stat.publisher.exception.StatPublisherConfigurationException;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class JMXConfigurationReader {
    private static Logger logger = Logger.getLogger(JMXConfigurationReader.class);
    /**
     * JMXConfiguration Class constructor load xml files and read values
     */
    public static JMXConfiguration readJMXConfiguration() throws StatPublisherConfigurationException {

        JMXConfiguration jmxConfiguration=new JMXConfiguration();
        try {
            //Load jmx.xml file
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document jmxDocument;
            String JMXFilePath = XMLConfigurationConstants.JMX_FILE_PATH;

            File jmxFilePath= new File(JMXFilePath);
            if (!jmxFilePath.exists()) {
                throw new StatPublisherConfigurationException("jmx.xml does not exists in "+
                                                              jmxFilePath.getPath());
            } else {
                jmxDocument = docBuilder.parse(JMXFilePath);
                jmxDocument.getDocumentElement().normalize();
                String jmxRootNode = jmxDocument.getDocumentElement().getNodeName();
                NodeList jmxDataList = jmxDocument.getElementsByTagName(jmxRootNode);

                String StartRMIServerValue =
                        ((Element) jmxDataList.item(0)).getElementsByTagName("StartRMIServer").
                                item(0).getChildNodes().item(0).getTextContent();
                String nodeValue = StartRMIServerValue.trim();
                jmxConfiguration.setStartRMIServer(Boolean.parseBoolean(nodeValue));

                String HostNameValue =
                        ((Element) jmxDataList.item(0)).getElementsByTagName("HostName").
                                item(0).getChildNodes().item(0).getTextContent();
                this.hostName = HostNameValue.trim();

                /**
                 * Load carbon.xml file
                 */
                Document carbonDocument;
                String carbonFilePath = XMLConfigurationConstants.CARBON_FILE_PATH;

                File carbonFile = new File(carbonFilePath);
                if (!carbonFile.exists()) {
                    throw new StatPublisherConfigurationException("carbon.xml does not exists in "+
                                                                  jmxFilePath.getPath());
                } else {
                    carbonDocument = docBuilder.parse(carbonFilePath);
                    carbonDocument.getDocumentElement().normalize();
                    String carbonRootNode = carbonDocument.getDocumentElement().getNodeName();
                    NodeList carbonDataList = carbonDocument.getElementsByTagName(carbonRootNode);

                    String RMIRegistryPortValue =
                            ((Element) carbonDataList.item(0)).getElementsByTagName("RMIRegistryPort").
                                    item(0).getChildNodes().item(0).getTextContent();
                    jmxConfiguration.rmiRegistryPort = RMIRegistryPortValue.trim();

                    String RMIServerPortValue =
                            ((Element) carbonDataList.item(0)).getElementsByTagName("RMIServerPort").
                                    item(0).getChildNodes().item(0).getTextContent();
                    this.rmiServerPort = RMIServerPortValue.trim();

                    String offSetValue =
                            ((Element) carbonDataList.item(0)).getElementsByTagName("Offset").
                                    item(0).getChildNodes().item(0).getTextContent();
                    this.offSet = offSetValue.trim();
                }
            }
        } catch (ParserConfigurationException e) {
            throw new StatPublisherConfigurationException("Indicate configuration error!", e);
        } catch (SAXException e) {
            throw new StatPublisherConfigurationException("Indicate a general SAX error or warning!", e);
        } catch (IOException e) {
            throw new StatPublisherConfigurationException("Indicate file loading error!", e);
        }

        return jmxConfiguration;
    }

}
