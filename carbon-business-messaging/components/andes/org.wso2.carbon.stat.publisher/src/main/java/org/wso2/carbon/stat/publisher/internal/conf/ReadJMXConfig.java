package org.wso2.carbon.stat.publisher.internal.conf;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.wso2.carbon.stat.publisher.internal.util.StatPublisherException;
import org.xml.sax.SAXException;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class ReadJMXConfig {

    private static Logger logger = Logger.getLogger(ReadJMXConfig.class);
    private boolean StartRMIServer;
    private String HostName;
    private String RMIRegistryPort;
    private String RMIServerPort;
    private String offSet;

    public ReadJMXConfig() throws StatPublisherException {

        final String emptyString = "";

        try {
            String filePath_JMX = ConfConstants.JMXFilePath;

            /**
             * Loads jmx.xml file
             */
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc;

            File jmx_file = new File(filePath_JMX);

            if (!jmx_file.exists()) {

                logger.error("jmx.xml doesn't exists!!!");

            } else {
                doc = docBuilder.parse(filePath_JMX);
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

        try{

            String filePath_Carbon = ConfConstants.CarbonFilePath;

            /**
             * Loads carbon.xml file
             */
            DocumentBuilderFactory docBuilderFactory_carbon = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder_carbon = docBuilderFactory_carbon.newDocumentBuilder();
            Document document;

            File carbon_file = new File(filePath_Carbon);

            if (!carbon_file.exists()) {

                logger.error("carbon.xml doesn't exists!!!");

            } else {
                document = docBuilder_carbon.parse(filePath_Carbon);
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
        }
        catch (ParserConfigurationException parserException) {
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

    public String getOffSet() { return offSet;}

}
