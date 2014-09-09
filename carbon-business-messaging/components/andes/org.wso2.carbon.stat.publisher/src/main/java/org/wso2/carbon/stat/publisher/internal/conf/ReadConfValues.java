package org.wso2.carbon.stat.publisher.internal.conf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Created by dilshani on 9/9/14.
 */
public class ReadConfValues {

    public static Log log = LogFactory.getLog(ReadConfValues.class);
    private String versionMessage;
    private String versionAck;
    private String forwardSlash;
    private String trustStorePassword;
    private String versionSystemStatistic;

    public ReadConfValues() {

        final String emptyString = "";

        try {
            String filePath = ConfConstants.ConfDataFilePath;

            /**
             * Loads XML file
             */
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc;

            File file = new File(filePath);

            if (!file.exists()) {

                log.error("jmx.xml doesn't exists!!");

            } else {
                doc = docBuilder.parse(filePath);
                doc.getDocumentElement().normalize();

                String rootNode = doc.getDocumentElement().getNodeName();
                NodeList dataList = doc.getElementsByTagName(rootNode);

                String versionMessageValue = (String) ((Element) dataList.item(0)).getElementsByTagName("versionMessage").
                        item(0).getChildNodes().item(0).getTextContent();
                this.versionMessage = versionMessageValue.trim();

                String versionAckValue = (String) ((Element) dataList.item(0)).getElementsByTagName("versionAck").
                        item(0).getChildNodes().item(0).getTextContent();
                this.versionAck = versionAckValue.trim();

                String forwardSlashValue = (String) ((Element) dataList.item(0)).getElementsByTagName("forwardSlash").
                        item(0).getChildNodes().item(0).getTextContent();
                this.forwardSlash = forwardSlashValue.trim();

                String trustStorePasswordValue = (String) ((Element) dataList.item(0)).getElementsByTagName("trustStorePassword").
                        item(0).getChildNodes().item(0).getTextContent();
                this.trustStorePassword = trustStorePasswordValue.trim();

                String versionSystemStatisticValue = (String) ((Element) dataList.item(0)).getElementsByTagName("versionSystemStatistic").
                        item(0).getChildNodes().item(0).getTextContent();
                this.versionSystemStatistic = versionSystemStatisticValue.trim();

            }
        } catch (ParserConfigurationException parserException) {
            log.error("ParserConfigurationException", parserException);
        } catch (SAXException saxException) {
            log.error("SAXException", saxException);
        } catch (IOException ioException) {
            log.error("IOException", ioException);
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
}
