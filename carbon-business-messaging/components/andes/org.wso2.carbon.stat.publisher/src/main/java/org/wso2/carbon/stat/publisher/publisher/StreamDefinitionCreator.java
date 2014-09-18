package org.wso2.carbon.stat.publisher.publisher;


import org.wso2.carbon.databridge.commons.Attribute;
import org.wso2.carbon.databridge.commons.AttributeType;
import org.wso2.carbon.databridge.commons.StreamDefinition;
import org.wso2.carbon.databridge.commons.exception.MalformedStreamDefinitionException;
import org.wso2.carbon.stat.publisher.conf.StreamConfiguration;

import java.util.ArrayList;
import java.util.List;

public class StreamDefinitionCreator {


    public static String serverStatsStreamName = "SYSTEM_STATISTICS_MB_2";
    public static String serverStatsNickName = "system stats";
    public static String serverStatsDescription = "Publish Message broker server statistics";

    public static String mbStatsStreamName = "MB_STATISTICS_TEST2";
    public static String mbStatsNickName = "MB stats";
    public static String mbStatsDescription = "Publish Message broker statistics";

    public static String messageStatsStreamName = "MESSAGE_STATISTICS";
    public static String messageStatsNickName = "message stats";
    public static String messageStatsDescription = "Publish Message broker message statistics";


    public static StreamDefinition getServerStatsStreamDef(StreamConfiguration streamConfiguration) throws MalformedStreamDefinitionException {

        StreamDefinition streamDefinition = new StreamDefinition(serverStatsStreamName, streamConfiguration.getVersionSystemStatistic());
        streamDefinition.setDescription(serverStatsDescription);
        streamDefinition.setNickName(serverStatsNickName);
        streamDefinition.setMetaData(getMetaDefinitions());
        streamDefinition.setPayloadData(getServerStatsPayloadDefinition());
        streamDefinition.setCorrelationData(null);
        return streamDefinition;
    }

    public static StreamDefinition getMBStatsStreamDef(StreamConfiguration streamConfiguration) throws MalformedStreamDefinitionException {

        StreamDefinition streamDefinition = new StreamDefinition(mbStatsStreamName, streamConfiguration.getVersionMBStatistic());
        streamDefinition.setDescription(mbStatsDescription);
        streamDefinition.setNickName(mbStatsNickName);
        streamDefinition.setMetaData(getMetaDefinitions());
        streamDefinition.setPayloadData(getMBStatsPayloadDefinition());
        streamDefinition.setCorrelationData(null);
        return streamDefinition;
    }

    private static List<Attribute> getMetaDefinitions() {

        List<Attribute> metaList = new ArrayList<Attribute>(1);

        metaList.add(new Attribute("publisherIP", AttributeType.STRING));

        return metaList;
    }

    private static List<Attribute> getServerStatsPayloadDefinition() {

        List<Attribute> payloadList = new ArrayList<Attribute>(4);

        payloadList.add(new Attribute("HeapMemoryUsage", AttributeType.STRING));
        payloadList.add(new Attribute("nonHeapMemoryUsage", AttributeType.STRING));
        payloadList.add(new Attribute("CPULoadAverage", AttributeType.STRING));
        payloadList.add(new Attribute("timestamp", AttributeType.LONG));

        return payloadList;
    }

    private static List<Attribute> getMBStatsPayloadDefinition() {

        List<Attribute> payloadList = new ArrayList<Attribute>(3);

        payloadList.add(new Attribute("NoOfSubscribers", AttributeType.INT));
        payloadList.add(new Attribute("NoOfTopics", AttributeType.INT));
        payloadList.add(new Attribute("timestamp", AttributeType.LONG));

        return payloadList;
    }


}
