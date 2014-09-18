package org.wso2.carbon.stat.publisher.publisher;


import org.wso2.carbon.databridge.commons.Attribute;
import org.wso2.carbon.databridge.commons.AttributeType;
import org.wso2.carbon.databridge.commons.StreamDefinition;
import org.wso2.carbon.databridge.commons.exception.MalformedStreamDefinitionException;

import java.util.ArrayList;
import java.util.List;

public class StreamDefinitionCreator {


    public static String serverStatsStreamName = "SYSTEM_STATISTICS_MB";
    public static String serverStatsVersion = "1.0.0";
    public static String serverStatsNickName = "system stats";
    public static String serverStatsDescription = "Publish Message broker server statistics";

    public static String mbStatsStreamName = "MB_STATISTICS";
    public static String mbStatsVersion = "1.0.0";
    public static String mbStatsNickName = "MB stats";
    public static String mbStatsDescription = "Publish Message broker statistics";

    public static String messageStatsStreamName = "MESSAGE_STATISTICS";
    public static String messageStatsVersion = "1.0.0";
    public static String messageStatsNickName = "message stats";
    public static String messageStatsDescription = "Publish Message broker message statistics";


    public static StreamDefinition getServerStatsStreamDef(String streamType) throws MalformedStreamDefinitionException {


        StreamDefinition streamDefinition = new StreamDefinition(serverStatsStreamName, serverStatsVersion);
        streamDefinition.setDescription(serverStatsDescription);
        streamDefinition.setNickName(serverStatsNickName);
        streamDefinition.setMetaData(getServerStatsMetaDefinitions());
        streamDefinition.setPayloadData(getServerStatsPayloadDefinition());
       // streamDefinition.setCorrelationData(getCorrelationDefinition());
        return streamDefinition;
    }

    private static List<Attribute> getServerStatsMetaDefinitions() {

        List<Attribute> metaList = new ArrayList<Attribute>(7);

        metaList.add(new Attribute("publisherIP", AttributeType.STRING));

        return metaList;
    }

    private static List<Attribute> getServerStatsPayloadDefinition() {

        List<Attribute> payloadList = new ArrayList<Attribute>(7);

        payloadList.add(new Attribute("HeapMemoryUsage", AttributeType.STRING));
        payloadList.add(new Attribute("nonHeapMemoryUsage", AttributeType.STRING));
        payloadList.add(new Attribute("CPULoadAverage", AttributeType.STRING));
        payloadList.add(new Attribute("timestamp", AttributeType.LONG));


        return payloadList;
    }
}
