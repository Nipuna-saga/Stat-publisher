package org.wso2.carbon.stat.publisher.util;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.wso2.carbon.stat.publisher.conf.JMXConfiguration;
import org.wso2.carbon.stat.publisher.conf.StreamConfiguration;
import org.wso2.carbon.stat.publisher.exception.StatPublisherConfigurationException;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class XMLConfigurationReader {

    /**
     *Load xml files and read values
     */
    public JMXConfiguration readJMXConfiguration() throws StatPublisherConfigurationException {

        JMXConfiguration jmxConfiguration=new JMXConfiguration();
        try {
            //Load jmx.xml file
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document jmxDocument;
            String JMXFilePath = StatPublisherConstants.JMX_DIRECTORY_PATH+StatPublisherConstants.JMX_XML;

            File jmxFile= new File(JMXFilePath);
            if (!jmxFile.exists()) {
                throw new StatPublisherConfigurationException("jmx.xml does not exists in "+
                                                              jmxFile.getPath());
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
                jmxConfiguration.setHostName(HostNameValue.trim());

                 //Load carbon.xml file
                Document carbonDocument;
                String carbonFilePath = StatPublisherConstants.CONF_DIRECTORY_PATH+StatPublisherConstants.CARBON_XML;

                File carbonFile = new File(carbonFilePath);
                if (!carbonFile.exists()) {
                    throw new StatPublisherConfigurationException("carbon.xml does not exists in "+
                                                                  carbonFile.getPath());
                } else {
                    carbonDocument = docBuilder.parse(carbonFilePath);
                    carbonDocument.getDocumentElement().normalize();
                    String carbonRootNode = carbonDocument.getDocumentElement().getNodeName();
                    NodeList carbonDataList = carbonDocument.getElementsByTagName(carbonRootNode);

                    String RMIRegistryPortValue =
                            ((Element) carbonDataList.item(0)).getElementsByTagName("RMIRegistryPort").
                                    item(0).getChildNodes().item(0).getTextContent();
                    jmxConfiguration.setRmiRegistryPort(RMIRegistryPortValue.trim());

                    String RMIServerPortValue =
                            ((Element) carbonDataList.item(0)).getElementsByTagName("RMIServerPort").
                                    item(0).getChildNodes().item(0).getTextContent();
                    jmxConfiguration.setRmiServerPort(RMIServerPortValue.trim());

                    String offSetValue =
                            ((Element) carbonDataList.item(0)).getElementsByTagName("Offset").
                                    item(0).getChildNodes().item(0).getTextContent();
                    jmxConfiguration.setOffSet(offSetValue.trim());
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

    /**
     * Load mbStatConfiguration.xml and read values
     */
    public StreamConfiguration readStreamConfiguration() throws StatPublisherConfigurationException {

        StreamConfiguration streamConfiguration=new StreamConfiguration();
        try {
             //Load mbStatConfiguration.xml file
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document document;
            String filePath = StatPublisherConstants.CONF_DIRECTORY_PATH+StatPublisherConstants.STAT_CONF_XML;
            File file = new File(filePath);

            if (!file.exists()) {
                throw new StatPublisherConfigurationException("mbStatConfiguration.xml does not exists in "+
                                                              file.getPath());
            } else {
                document = docBuilder.parse(filePath);
                document.getDocumentElement().normalize();
                String rootNode = document.getDocumentElement().getNodeName();
                NodeList dataList = document.getElementsByTagName(rootNode);

                String versionMessageValue =
                        ((Element) dataList.item(0)).getElementsByTagName("versionMessage").
                                item(0).getChildNodes().item(0).getTextContent();
                streamConfiguration.setVersionMessage(versionMessageValue.trim());

                String versionAckValue =
                        ((Element) dataList.item(0)).getElementsByTagName("versionAck").
                                item(0).getChildNodes().item(0).getTextContent();
                streamConfiguration.setVersionAck(versionAckValue.trim());

                String forwardSlashValue =
                        ((Element) dataList.item(0)).getElementsByTagName("forwardSlash").
                                item(0).getChildNodes().item(0).getTextContent();
                streamConfiguration.setForwardSlash(forwardSlashValue.trim());

                String trustStorePasswordValue =
                        ((Element) dataList.item(0)).getElementsByTagName("trustStorePassword").
                                item(0).getChildNodes().item(0).getTextContent();
                streamConfiguration.setTrustStorePassword(trustStorePasswordValue.trim());

                String versionSystemStatisticValue =
                        ((Element) dataList.item(0)).getElementsByTagName("versionSystemStatistic").
                                item(0).getChildNodes().item(0).getTextContent();
                streamConfiguration.setVersionSystemStatistic(versionSystemStatisticValue.trim());

                String versionMBStatisticValue =
                        ((Element) dataList.item(0)).getElementsByTagName("versionMBStatistic").
                                item(0).getChildNodes().item(0).getTextContent();
                streamConfiguration.setVersionMBStatistic(versionMBStatisticValue.trim());
            }
        } catch (ParserConfigurationException e) {
            throw new StatPublisherConfigurationException("Indicate configuration error!", e);
        } catch (SAXException e) {
            throw new StatPublisherConfigurationException("Indicate a general SAX error or warning!", e);
        } catch (IOException e) {
            throw new StatPublisherConfigurationException("Indicate file loading error!", e);
        }
        return  streamConfiguration;
    }
}
