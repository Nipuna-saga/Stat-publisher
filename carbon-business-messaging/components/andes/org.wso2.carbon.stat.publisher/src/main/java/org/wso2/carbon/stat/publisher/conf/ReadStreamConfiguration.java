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

public class ReadStreamConfiguration {
    private static Logger logger = Logger.getLogger(ReadStreamConfiguration.class);
    private String versionMessage;
    private String versionAck;
    private String forwardSlash;
    private String trustStorePassword;
    private String versionSystemStatistic;
    private String versionMBStatistic;

    public ReadStreamConfiguration() throws StatPublisherException {

        try {
            String filePath = XMLConfigurationConstants.CONF_DATA_FILE_PATH;

            /**
             * Loads configurationData.xml file
             */
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc;

            File file = new File(filePath);

            if (!file.exists()) {

                logger.error("configurationData.xml doesn't exists!!");

            } else {
                doc = docBuilder.parse(filePath);
                doc.getDocumentElement().normalize();

                String rootNode = doc.getDocumentElement().getNodeName();
                NodeList dataList = doc.getElementsByTagName(rootNode);

                String versionMessageValue =
                        (String) ((Element) dataList.item(0)).getElementsByTagName("versionMessage").
                                item(0).getChildNodes().item(0).getTextContent();
                this.versionMessage = versionMessageValue.trim();

                String versionAckValue =
                        (String) ((Element) dataList.item(0)).getElementsByTagName("versionAck").
                                item(0).getChildNodes().item(0).getTextContent();
                this.versionAck = versionAckValue.trim();

                String forwardSlashValue =
                        (String) ((Element) dataList.item(0)).getElementsByTagName("forwardSlash").
                                item(0).getChildNodes().item(0).getTextContent();
                this.forwardSlash = forwardSlashValue.trim();

                String trustStorePasswordValue =
                        (String) ((Element) dataList.item(0)).getElementsByTagName("trustStorePassword").
                                item(0).getChildNodes().item(0).getTextContent();
                this.trustStorePassword = trustStorePasswordValue.trim();

                String versionSystemStatisticValue =
                        (String) ((Element) dataList.item(0)).getElementsByTagName("versionSystemStatistic").
                                item(0).getChildNodes().item(0).getTextContent();
                this.versionSystemStatistic = versionSystemStatisticValue.trim();

                String versionMBStatisticValue =
                        (String) ((Element) dataList.item(0)).getElementsByTagName("versionMBStatistic").
                                item(0).getChildNodes().item(0).getTextContent();
                this.versionMBStatistic = versionMBStatisticValue.trim();

            }
        } catch (ParserConfigurationException parserException) {
            throw new StatPublisherException("ParserConfigurationException", parserException);
        } catch (SAXException saxException) {
            throw new StatPublisherException("SAXException", saxException);
        } catch (IOException ioException) {
            throw new StatPublisherException("IOException", ioException);
        }
    }

    public String getVersionMessage() {
        return versionMessage;
    }

    public String getVersionAck() {
        return versionAck;
    }

    public String getForwardSlash() {
        return forwardSlash;
    }

    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    public String getVersionSystemStatistic() {
        return versionSystemStatistic;
    }

    public String getVersionMBStatistic() {
        return versionMBStatistic;
    }
}
