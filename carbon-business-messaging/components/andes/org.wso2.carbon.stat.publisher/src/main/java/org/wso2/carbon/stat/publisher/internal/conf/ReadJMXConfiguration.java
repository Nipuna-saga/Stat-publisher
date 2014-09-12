package org.wso2.carbon.stat.publisher.internal.conf;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.wso2.carbon.stat.publisher.internal.util.StatPublisherException;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class ReadJMXConfiguration {

    private static Logger logger = Logger.getLogger(ReadJMXConfiguration.class);
    private boolean StartRMIServer;
    private String HostName;
    private String RMIRegistryPort;
    private String RMIServerPort;
    private String offSet;

    public ReadJMXConfiguration() throws StatPublisherException {

        final String emptyString = "";

        try {
            String filePathJMX = XMLConfigurationConstants.JMX_FILE_PATH;

            /**
             * Loads jmx.xml file
             */
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc;

            File jmxFilePath= new File(filePathJMX);

            if (!jmxFilePath.exists()) {

                logger.error("jmx.xml doesn't exists!!!");

            } else {
                doc = docBuilder.parse(filePathJMX);
                doc.getDocumentElement().normalize();

                String rootNode = doc.getDocumentElement().getNodeName();
                NodeList dataList = doc.getElementsByTagName(rootNode);

                String StartRMIServerValue =
                        (String) ((Element) dataList.item(0)).getElementsByTagName("StartRMIServer").
                                item(0).getChildNodes().item(0).getTextContent();

                if (StartRMIServerValue.equals(emptyString)) {
                    StartRMIServer = false;
                } else {
                    String nodeValue = StartRMIServerValue.trim();
                    StartRMIServer = Boolean.parseBoolean(nodeValue);
                }

                String HostNameValue =
                        (String) ((Element) dataList.item(0)).getElementsByTagName("HostName").
                                item(0).getChildNodes().item(0).getTextContent();
                this.HostName = HostNameValue.trim();
            }
        } catch (ParserConfigurationException parserException) {
            throw new StatPublisherException("ParserConfigurationException", parserException);
        } catch (SAXException saxException) {
            throw new StatPublisherException("SAXException", saxException);
        } catch (IOException ioException) {
            throw new StatPublisherException("IOException", ioException);
        }

        try {

            String filePathCarbon = XMLConfigurationConstants.CARBON_FILE_PATH;

            /**
             * Loads carbon.xml file
             */
            DocumentBuilderFactory docBuilderFactory_carbon = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder_carbon = docBuilderFactory_carbon.newDocumentBuilder();
            Document document;

            File carbonFile = new File(filePathCarbon);

            if (!carbonFile.exists()) {

                logger.error("carbon.xml doesn't exists!!!");

            } else {
                document = docBuilder_carbon.parse(filePathCarbon);
                document.getDocumentElement().normalize();

                String rootNode = document.getDocumentElement().getNodeName();
                NodeList dataList = document.getElementsByTagName(rootNode);

                String RMIRegistryPortValue =
                        (String) ((Element) dataList.item(0)).getElementsByTagName("RMIRegistryPort").
                                item(0).getChildNodes().item(0).getTextContent();
                this.RMIRegistryPort = RMIRegistryPortValue.trim();

                String RMIServerPortValue =
                        (String) ((Element) dataList.item(0)).getElementsByTagName("RMIServerPort").
                                item(0).getChildNodes().item(0).getTextContent();
                this.RMIServerPort = RMIServerPortValue.trim();

                String offSetValue =
                        (String) ((Element) dataList.item(0)).getElementsByTagName("Offset").
                                item(0).getChildNodes().item(0).getTextContent();
                this.offSet = offSetValue.trim();
            }
        } catch (ParserConfigurationException parserException) {
            throw new StatPublisherException("ParserConfigurationException", parserException);
        } catch (SAXException saxException) {
            throw new StatPublisherException("SAXException", saxException);
        } catch (IOException ioException) {
            throw new StatPublisherException("IOException", ioException);
        }
    }

    public boolean isStartRMIServer() {
        return StartRMIServer;
    }

    public String getRMIServerPort() {
        return RMIServerPort;
    }

    public String getHostName() {
        return HostName;
    }

    public String getRMIRegistryPort() {
        return RMIRegistryPort;
    }

    public String getOffSet() {
        return offSet;
    }

}
