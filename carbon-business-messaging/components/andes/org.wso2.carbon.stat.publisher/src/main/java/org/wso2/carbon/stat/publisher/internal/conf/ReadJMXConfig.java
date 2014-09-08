package org.wso2.carbon.stat.publisher.internal.conf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;



public class ReadJMXConfig {
    private boolean StartRMIServer;
    private String HostName;
    private String RMIRegistryPort;
    private String RMIServerPort;

    public static Log log = LogFactory.getLog(ReadJMXConfig.class);

    public ReadJMXConfig(){

        final String emptyString= "";

        try {
            String filePath= "repository/conf/etc/jmx.xml";

            // get XML file
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc;

            File file = new File(filePath);

            if (!file.exists()) {

                System.err.println("jmx.xml doesn't exists!!");

            } else {
                doc = docBuilder.parse(filePath);
                doc.getDocumentElement().normalize();

                String rootNode =  doc.getDocumentElement().getNodeName();
                NodeList dataList = doc.getElementsByTagName(rootNode);

                String StartRMIServerValue = (String) ((Element) dataList.item(0)).getElementsByTagName("StartRMIServer").
                        item(0).getChildNodes().item(0).getTextContent();
                if (StartRMIServerValue.equals(emptyString)) {
                    StartRMIServer = false;
                } else {

                    String nodeValue = StartRMIServerValue.trim();
                    StartRMIServer = Boolean.parseBoolean(nodeValue);

                }

                String HostNameValue = (String) ((Element) dataList.item(0)).getElementsByTagName("HostName").
                        item(0).getChildNodes().item(0).getTextContent();
                this.HostName=HostNameValue.trim();

                String RMIRegistryPortValue = (String) ((Element) dataList.item(0)).getElementsByTagName("RMIRegistryPort").
                        item(0).getChildNodes().item(0).getTextContent();
                this.RMIRegistryPort=RMIRegistryPortValue.trim();

                String RMIServerPortValue = (String) ((Element) dataList.item(0)).getElementsByTagName("RMIRegistryPort").
                        item(0).getChildNodes().item(0).getTextContent();
                this.RMIServerPort=RMIServerPortValue.trim();


            }
        }catch(ParserConfigurationException parserException){
            log.error("ParserConfigurationException",parserException);
        }
        catch(SAXException saxException){
            log.error("SAXException",saxException);
        }
        catch(IOException ioException){
            log.error("IOException",ioException);
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
}
