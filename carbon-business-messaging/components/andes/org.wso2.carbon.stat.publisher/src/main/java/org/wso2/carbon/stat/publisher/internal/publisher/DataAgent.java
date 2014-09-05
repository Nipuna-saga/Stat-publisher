package org.wso2.carbon.stat.publisher.internal.publisher;

import org.apache.log4j.Logger;
import org.wso2.andes.kernel.*;
import org.wso2.carbon.databridge.agent.thrift.Agent;
import org.wso2.carbon.databridge.agent.thrift.AsyncDataPublisher;
import org.wso2.carbon.databridge.agent.thrift.conf.AgentConfiguration;
import org.wso2.carbon.databridge.agent.thrift.exception.AgentException;
import org.wso2.carbon.databridge.commons.Event;
import org.wso2.carbon.serverStats.mbeans.MbeansStats;
import org.wso2.carbon.stat.publisher.internal.conf.ReadJMXConfig;
import org.wso2.carbon.stat.publisher.internal.data.StatConfiguration;
import org.wso2.carbon.utils.CarbonUtils;

import java.util.List;

public class DataAgent {

    private static Logger logger = Logger.getLogger(DataAgent.class);

    private StatConfiguration statConfigurationInstance;
    private static DataAgent instance = null;
    private Agent agent;
    long timeStamp;
    AsyncDataPublisher asyncDataPublisherSystemStats = null;
    AsyncDataPublisher asyncDataPublisherMBStatistics = null;
    AsyncDataPublisher asyncDataPublisherMessageStatistics = null;
    AsyncDataPublisher asyncDataPublisherACKStatistics = null;


    public final String VERSION_MESSAGE = "1.0.0";
    public final String VERSION_ACK = "1.0.2";


    //subscriptions
    private SubscriptionStore subscriptionStore;


    //topic and queue
    private int noOfTopics;
    private int totalSubscribers;
    private  String JMSConfiguration[];


    private DataAgent() { //private constructor
//todo need to be removed sleep
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        AgentConfiguration agentConfiguration = new AgentConfiguration();
        System.setProperty("javax.net.ssl.trustStore", CarbonUtils.getCarbonHome() + "/repository/resources/security/client-truststore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
        agent = new Agent(agentConfiguration);


    }

    public static DataAgent getObjectDataAgent() {

        if (instance == null) {
            instance = new DataAgent();

        }
        return instance;
    }






    public void sendSystemStats(String URL, String[] credentials) {

        //get server stats
        try {

            //get JMX configuration

            JMSConfiguration = getJMXConfiguration();


            MbeansStats mbeansStats = new MbeansStats(JMSConfiguration[0], Integer.parseInt(JMSConfiguration[1]), JMSConfiguration[2], JMSConfiguration[3]);

            String heapMemoryUsage = mbeansStats.getHeapMemoryUsage();
            String nonHeapMemoryUsage = mbeansStats.getNonHeapMemoryUsage();
            String CPULoadAverage = mbeansStats.getCPULoadAverage();


            //Using Asynchronous data publisher
           if (asyncDataPublisherSystemStats == null) { //create the publisher object only once
                asyncDataPublisherSystemStats = new AsyncDataPublisher( URL, "admin", "admin", agent);
            }
            String VERSION_SYSTEM_STATISTICS = "1.0.0";
            String messageStreamDefinition = "{" +
                    "  'name':'" + "SYSTEM_STATISTICS_MB" + "'," +
                    "  'version':'" + VERSION_SYSTEM_STATISTICS + "'," +
                    "  'nickName': 'MB stats'," +
                    "  'description': 'Server Stats'," +
                    "  'metaData':[" +
                    "          {'name':'publisherIP','type':'STRING'}" +
                    "  ]," +
                    "  'payloadData':[" +

                    " {'name':'HeapMemoryUsage','type':'STRING'}," +
                    "         {'name':'nonHeapMemoryUsage','type':'STRING'}," +
                    "          {'name':'CPULoadAverage','type':'STRING'}," +
                    " 			{'name':'timestamp','type':'LONG'}" +
                    "  ]" +
                    "}";


            asyncDataPublisherSystemStats.addStreamDefinition(messageStreamDefinition, "SYSTEM_STATISTICS_MB", VERSION_SYSTEM_STATISTICS);


            timeStamp = getTimeStamp();


            Object[] payload = new Object[]{heapMemoryUsage, nonHeapMemoryUsage, CPULoadAverage, timeStamp};
            Event event = eventObject(null, new Object[]{URL}, payload);
            asyncDataPublisherSystemStats.publish("SYSTEM_STATISTICS_MB", VERSION_SYSTEM_STATISTICS, event);

            logger.info("System statistics sent at " + timeStamp);


        } catch (Exception e) {
            logger.error("Failed to send server stats", e);
        }


    }

    public void sendMBStatistics(String URL, String[] credentials) {


        //Using Asynchronous data publisher
        if (asyncDataPublisherMBStatistics == null) { //create the publisher object only once
            asyncDataPublisherMBStatistics = new AsyncDataPublisher(URL, credentials[0], credentials[1], agent);
        }

        String VERSION_MB_STATISTICS = "1.0.0";
        String messageStreamDefinition = "{" +
                "  'name':'" + "MB_STATISTICS" + "'," +
                "  'version':'" + VERSION_MB_STATISTICS + "'," +
                "  'nickName': 'MB stats'," +
                "  'description': 'Server Stats'," +
                "  'metaData':[" +
                "          {'name':'publisherIP','type':'STRING'}" +
                "  ]," +
                "  'payloadData':[" +


                "          {'name':'NoOfSubscribers','type':'INT'}," +
                "          {'name':'NoOfTopics','type':'INT'}," +
                " 			{'name':'timestamp','type':'LONG'}" +
                "  ]" +
                "}";
        asyncDataPublisherMBStatistics.addStreamDefinition(messageStreamDefinition, "MB_STATISTICS", VERSION_MB_STATISTICS);


        timeStamp = getTimeStamp();

        try {
            noOfTopics = getTopicList().size(); //get number of topic in a cluster
            totalSubscribers = getTotalSubscriptions();

            Object[] payload = new Object[]{totalSubscribers, noOfTopics, timeStamp};
            Event event = eventObject(null, new Object[]{URL}, payload);


            asyncDataPublisherMBStatistics.publish("MB_STATISTICS", VERSION_MB_STATISTICS, event);


        } catch (AgentException e) {
            logger.error("Failed to publish event", e);
        } catch (Exception e) {
            logger.error("Failed to send MB statistics", e);
        }


    }

    private void sendMessageStatistics(String URL, String[] credentials, AndesMessageMetadata message,  int subscribers) {


        long messageID = message.getMessageID();
        String messageDestination = message.getDestination();
        //  messageMetaData = message.getMetadata();
        int messageContentLength = message.getMessageContentLength();
        long expirationTime = message.getExpirationTime();

        int noOfsubscribers = subscribers;


        //Using Asynchronous data publisher
        if (asyncDataPublisherMessageStatistics == null) { //create the publisher object only once
            asyncDataPublisherMessageStatistics = new AsyncDataPublisher(URL, credentials[0], credentials[1], agent);
        }
        String messageStreamDefinition = "{" +
                "  'name':'" + "MESSAGE_STATISTICS" + "'," +
                "  'version':'" + VERSION_MESSAGE + "'," +
                "  'nickName': 'MB stats'," +
                "  'description': 'A message received'," +
                "  'metaData':[" +
                "          {'name':'publisherIP','type':'STRING'}" +
                "  ]," +
                "  'payloadData':[" +

                "          {'name':'Message_id','type':'LONG'}," +
                "          {'name':'Destination','type':'STRING'}," +
                "          {'name':'MessageContentLength','type':'INT'}," +
                "          {'name':'expirationTime','type':'LONG'}," +
                "          {'name':'NoOfSubscriptions','type':'STRING'}," +

                " 			{'name':'timestamp','type':'LONG'}" +
                "  ]" +
                "}";
        asyncDataPublisherMessageStatistics.addStreamDefinition(messageStreamDefinition, "MESSAGE_STATISTICS", VERSION_MESSAGE);
        timeStamp = getTimeStamp();


        Object[] payload = new Object[]{messageID, messageDestination, messageContentLength, expirationTime, Integer.toString(noOfsubscribers), timeStamp};
        Event event = eventObject(null, new Object[]{URL}, payload);
        try {

            asyncDataPublisherMessageStatistics.publish("MESSAGE_STATISTICS", VERSION_MESSAGE, event);
        } catch (AgentException e) {
            logger.error("Failed to publish event for send message statistics", e);
        }


    }


    public void sendACKStatistics(String URL, String[] credentials, AndesAckData ack) {

        long ackMessageID = ack.messageID;
        String queueName = ack.qName;

        //Using Asynchronous data publisher
        if (asyncDataPublisherACKStatistics == null) { //create the publisher object only once
            asyncDataPublisherACKStatistics = new AsyncDataPublisher(URL, credentials[0], credentials[1], agent);
        }

        String ackStreamDefinition = "{" +
                "  'name':'" + "" + "MESSAGE_STATISTICS'," +
                "  'version':'" + VERSION_ACK + "'," +
                "  'nickName': 'MB stats'," +
                "  'description': 'A ack received'," +
                "  'metaData':[" +
                "          {'name':'publisherIP','type':'STRING'}" +
                "  ]," +
                "  'payloadData':[" +

                "          {'name':'Message_id','type':'LONG'}," +
                " 			{'name':'timestamp','type':'LONG'}," +
                " 			{'name':'queueName','type':'STRING'}" +


                "  ]" +
                "}";
        asyncDataPublisherACKStatistics.addStreamDefinition(ackStreamDefinition, "MESSAGE_STATISTICS", VERSION_ACK);


        timeStamp = getTimeStamp();


        Object[] payload = new Object[]{ackMessageID, queueName, timeStamp};
        Event event = eventObject(null, new Object[]{URL}, payload);
        try {

            asyncDataPublisherACKStatistics.publish("MESSAGE_STATISTICS", VERSION_ACK, event);
        } catch (AgentException e) {
            logger.error("Failed to publish event", e);
        }


    }

    private int getTotalSubscriptions() throws Exception {

        totalSubscribers = 0;

        List<String> topics = getTopicList();

        MessagingEngine messagingEngine = MessagingEngine.getInstance();
        subscriptionStore = messagingEngine.getSubscriptionStore();

        for (String topic : topics) {


            List<Subscrption> subscriptionsList = subscriptionStore.getActiveClusterSubscribersForDestination(topic, true);
            totalSubscribers += subscriptionsList.size();


        }


        return totalSubscribers;


    }


    private List<String> getTopicList() throws Exception {

        MessagingEngine messaginEngine = MessagingEngine.getInstance();
        subscriptionStore = messaginEngine.getSubscriptionStore();
        List<String> topics = subscriptionStore.getTopics();
        noOfTopics = topics.size();


        return topics;


    }

    //this method will return JMXConfiguration as an array. array contains ip,port,username,password
    private String[] getJMXConfiguration() {

       ReadJMXConfig readJMXConfig = new ReadJMXConfig();

        
        System.out.println("hostname-------------------: " + readJMXConfig.getHostName());

        String JMSConfig[] = {"localhost", "10000", "admin", "admin"};

        return JMSConfig;

    }

    private Event eventObject(Object[] correlationData, Object[] metaData, Object[] payLoadData) {
        Event event = new Event();
        event.setCorrelationData(correlationData);
        event.setMetaData(metaData);
        event.setPayloadData(payLoadData);
        return event;
    }

    //get current time stamp
    private long getTimeStamp() {

        return System.currentTimeMillis() / 1000L;

    }


}
