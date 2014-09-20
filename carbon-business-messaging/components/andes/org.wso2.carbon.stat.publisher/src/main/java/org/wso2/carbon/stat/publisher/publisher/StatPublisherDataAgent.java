package org.wso2.carbon.stat.publisher.publisher;

import org.apache.log4j.Logger;
import org.wso2.andes.kernel.*;
import org.wso2.carbon.databridge.agent.thrift.exception.AgentException;
import org.wso2.carbon.databridge.agent.thrift.lb.DataPublisherHolder;
import org.wso2.carbon.databridge.agent.thrift.lb.LoadBalancingDataPublisher;
import org.wso2.carbon.databridge.agent.thrift.lb.ReceiverGroup;
import org.wso2.carbon.databridge.commons.StreamDefinition;
import org.wso2.carbon.databridge.commons.exception.MalformedStreamDefinitionException;
import org.wso2.carbon.stat.publisher.conf.JMXConfiguration;
import org.wso2.carbon.stat.publisher.conf.StatPublisherConfiguration;
import org.wso2.carbon.stat.publisher.conf.StreamConfiguration;
import org.wso2.carbon.stat.publisher.serverStats.MbeansStats;
import org.wso2.carbon.stat.publisher.serverStats.data.MbeansStatsData;

import javax.management.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class StatPublisherDataAgent {

    private  JMXConfiguration jmxConfiguration;
    private  StreamConfiguration streamConfiguration;
    private  StatPublisherConfiguration statPublisherConfiguration;

    private  MbeansStats mbeansStats = null;
    private MbeansStatsData mbeansStatsData;

    private StreamDefinition serverStatsStreamDef;
    private StreamDefinition mbStatsStreamDef;
    private StreamDefinition messageStatsStreamDef;
    private StreamDefinition ackStatsStreamDef;

   private LoadBalancingDataPublisher loadBalancingDataPublisher;

   private List<Object> metaData;
   private List<Object> payLoadData;

    private MessagingEngine messagingEngine;
    private  SubscriptionStore subscriptionStore;
    List<Subscrption> subscriptionsList;

    private  Logger logger = Logger.getLogger(StatPublisherDataAgent.class);

    public StatPublisherDataAgent(JMXConfiguration jmxConfiguration,
                                  StreamConfiguration streamConfiguration,
                                  StatPublisherConfiguration statPublisherConfiguration) {

        //set configurations
        this.jmxConfiguration = jmxConfiguration;
        this.streamConfiguration = streamConfiguration;
        this.statPublisherConfiguration = statPublisherConfiguration;

        try {

            //creating all stream definitions
            serverStatsStreamDef = StreamDefinitionCreator.getServerStatsStreamDef(streamConfiguration);
            mbStatsStreamDef = StreamDefinitionCreator.getMBStatsStreamDef(streamConfiguration);
            messageStatsStreamDef = StreamDefinitionCreator.getMessageStatsStreamDef(streamConfiguration);
            ackStatsStreamDef = StreamDefinitionCreator.getAckStatsStreamDef(streamConfiguration);

        } catch (MalformedStreamDefinitionException e) {
            e.printStackTrace();
        }

        ArrayList<ReceiverGroup> allReceiverGroups = new ArrayList<ReceiverGroup>();

        ArrayList<DataPublisherHolder> dataPublisherHolders = new ArrayList<DataPublisherHolder>();


        String[] urls = {statPublisherConfiguration.getURL()};

        for (String aUrl : urls) {

            DataPublisherHolder aNode = new DataPublisherHolder(null, aUrl.trim(), statPublisherConfiguration.getUsername(),statPublisherConfiguration.getPassword());
            dataPublisherHolders.add(aNode);
        }


        ReceiverGroup group = new ReceiverGroup(dataPublisherHolders);
        allReceiverGroups.add(group);

        loadBalancingDataPublisher = new LoadBalancingDataPublisher(allReceiverGroups);


        //adding Stream definitions to publisher
        loadBalancingDataPublisher.addStreamDefinition(serverStatsStreamDef);
        loadBalancingDataPublisher.addStreamDefinition(mbStatsStreamDef);
        loadBalancingDataPublisher.addStreamDefinition(messageStatsStreamDef);
        loadBalancingDataPublisher.addStreamDefinition(ackStatsStreamDef);


        messagingEngine = MessagingEngine.getInstance();
        subscriptionStore = messagingEngine.getSubscriptionStore();

    }


    public void sendSystemStats() throws MalformedObjectNameException, ReflectionException, IOException,
            InstanceNotFoundException, AttributeNotFoundException, MBeanException {

        if(mbeansStats==null) {
            mbeansStats = new MbeansStats(jmxConfiguration);
        }

        //get server statistics
        mbeansStatsData = mbeansStats.getMbeansStatsData();

        metaData = getMetaData();
        payLoadData = getServerStatsPayLoadData(mbeansStatsData);



        try {
            loadBalancingDataPublisher.publish(serverStatsStreamDef.getName(), serverStatsStreamDef.getVersion(),
                    getObjectArray(metaData), null,
                    getObjectArray(payLoadData));
        } catch (AgentException e) {
            e.printStackTrace();
        }


    }

    public void sendMBStats() throws MalformedObjectNameException, ReflectionException, IOException,
            InstanceNotFoundException, AttributeNotFoundException, MBeanException {


        metaData = getMetaData();
        try {
            payLoadData = getMBStatsPayLoadData();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            loadBalancingDataPublisher.publish(mbStatsStreamDef.getName(), mbStatsStreamDef.getVersion(),
                    getObjectArray(metaData), null,
                    getObjectArray(payLoadData));
        } catch (AgentException e) {
            e.printStackTrace();
        }


    }

    public void sendMessageStats(AndesMessageMetadata message, int subscribers) throws MalformedObjectNameException, ReflectionException, IOException,
            InstanceNotFoundException, AttributeNotFoundException, MBeanException {


        metaData = getMetaData();
        try {
            payLoadData = getMessageStatsPayLoadData(message, subscribers);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            loadBalancingDataPublisher.publish(messageStatsStreamDef.getName(), messageStatsStreamDef.getVersion(),
                    getObjectArray(metaData), null,
                    getObjectArray(payLoadData));
        } catch (AgentException e) {
            e.printStackTrace();
        }


    }


    public void sendAckStats(AndesAckData message) throws MalformedObjectNameException, ReflectionException, IOException,
            InstanceNotFoundException, AttributeNotFoundException, MBeanException {


        metaData = getMetaData();
        try {
            payLoadData = getAckStatsPayLoadData(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            loadBalancingDataPublisher.publish(ackStatsStreamDef.getName(), ackStatsStreamDef.getVersion(),
                    getObjectArray(metaData), null,
                    getObjectArray(payLoadData));
        } catch (AgentException e) {
            e.printStackTrace();
        }


    }

    private Object[] getObjectArray(List<Object> list) {
        if (list.size() > 0) {
            return list.toArray();
        }
        return null;
    }

    public  List<Object> getMetaData() {
        List<Object> metaData = new ArrayList<Object>(1);

        //todo check whether this value is correct or not
        metaData.add(statPublisherConfiguration.getNodeURL());
        return metaData;
    }


    public  List<Object> getServerStatsPayLoadData(MbeansStatsData mbeansStatsData) {
        List<Object> payloadData = new ArrayList<Object>(4);
        payloadData.add(mbeansStatsData.getHeapMemoryUsage());
        payloadData.add(mbeansStatsData.getNonHeapMemoryUsage());
        payloadData.add(mbeansStatsData.getCPULoadAverage());
        payloadData.add(getCurrentTimeStamp());
        return payloadData;
    }

    public  List<Object> getMBStatsPayLoadData() throws Exception {
        List<Object> payloadData = new ArrayList<Object>(3);

        payloadData.add(getTotalSubscriptions());
        payloadData.add(getTopicList().size());
        payloadData.add(getCurrentTimeStamp());


        return payloadData;
    }

    public  List<Object> getMessageStatsPayLoadData(AndesMessageMetadata message, int subscribers) throws Exception {
        List<Object> payloadData = new ArrayList<Object>(6);

        payloadData.add(message.getMessageID());
        payloadData.add(message.getDestination());
        payloadData.add(message.getMessageContentLength());
        payloadData.add(message.getExpirationTime());
        payloadData.add(subscribers);
        payloadData.add(getCurrentTimeStamp());


        return payloadData;
    }


    public  List<Object> getAckStatsPayLoadData(AndesAckData message) throws Exception {
        List<Object> payloadData = new ArrayList<Object>(3);

        payloadData.add(message.messageID);
        payloadData.add(message.qName);
        payloadData.add(getCurrentTimeStamp());

        return payloadData;
    }

    private  long getCurrentTimeStamp(){

        return System.currentTimeMillis();

    }
     List<String> topics;
    private   int getTotalSubscriptions() throws Exception {

        int totalSubscribers = 0;
        topics = getTopicList();

        for (String topic : topics) {
         subscriptionsList = subscriptionStore.getActiveClusterSubscribersForDestination(topic, true);
            totalSubscribers += subscriptionsList.size();
        }

        return totalSubscribers;

    }

    private  List<String> getTopicList() throws Exception {

        MessagingEngine messagingEngine = MessagingEngine.getInstance();
        subscriptionStore = messagingEngine.getSubscriptionStore();
        List<String> topics = subscriptionStore.getTopics();

        return topics;


    }


}
