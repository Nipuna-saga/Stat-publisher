package org.wso2.carbon.stat.publisher.publisher;

import org.apache.log4j.Logger;
import org.wso2.andes.kernel.MessagingEngine;
import org.wso2.andes.kernel.SubscriptionStore;
import org.wso2.andes.kernel.Subscrption;
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

    private static JMXConfiguration jmxConfiguration;
    private static StreamConfiguration streamConfiguration;
    private static StatPublisherConfiguration statPublisherConfiguration;

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
    private static SubscriptionStore subscriptionStore;
    static List<Subscrption> subscriptionsList;

    private static Logger logger = Logger.getLogger(StatPublisherDataAgent.class);

    public StatPublisherDataAgent(JMXConfiguration jmxConfiguration,
                                  StreamConfiguration streamConfiguration,
                                  StatPublisherConfiguration statPublisherConfiguration) {

        this.jmxConfiguration = jmxConfiguration;
        this.streamConfiguration = streamConfiguration;
        this.statPublisherConfiguration = statPublisherConfiguration;

        try {
            serverStatsStreamDef = StreamDefinitionCreator.getServerStatsStreamDef(streamConfiguration);
            mbStatsStreamDef = StreamDefinitionCreator.getMBStatsStreamDef(streamConfiguration);
        } catch (MalformedStreamDefinitionException e) {
            e.printStackTrace();
        }
        ArrayList<ReceiverGroup> allReceiverGroups = new ArrayList<ReceiverGroup>();

        ArrayList<DataPublisherHolder> dataPublisherHolders = new ArrayList<DataPublisherHolder>();


        String[] urls = {statPublisherConfiguration.getUrl()};

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

        messagingEngine = MessagingEngine.getInstance();
        subscriptionStore = messagingEngine.getSubscriptionStore();

    }


    public void sendSystemStats() throws MalformedObjectNameException, ReflectionException, IOException,
            InstanceNotFoundException, AttributeNotFoundException, MBeanException {

        if(mbeansStats==null) {
            mbeansStats = new MbeansStats(jmxConfiguration);
        }

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


    private Object[] getObjectArray(List<Object> list) {
        if (list.size() > 0) {
            return list.toArray();
        }
        return null;
    }

    public static List<Object> getMetaData() {
        List<Object> metaData = new ArrayList<Object>(1);

        //todo check whether this value is correct or not
        metaData.add(statPublisherConfiguration.getNodeURL());
        return metaData;
    }


    public static List<Object> getServerStatsPayLoadData(MbeansStatsData mbeansStatsData) {
        List<Object> payloadData = new ArrayList<Object>(4);
        payloadData.add(mbeansStatsData.getHeapMemoryUsage());
        payloadData.add(mbeansStatsData.getNonHeapMemoryUsage());
        payloadData.add(mbeansStatsData.getCPULoadAverage());
        payloadData.add(getCurrentTimeStamp());
        return payloadData;
    }

    public static List<Object> getMBStatsPayLoadData() throws Exception {
        List<Object> payloadData = new ArrayList<Object>(3);

        payloadData.add(getTotalSubscriptions());
        payloadData.add(getTopicList().size());
        payloadData.add(getCurrentTimeStamp());


        return payloadData;
    }

    private static long getCurrentTimeStamp(){

        return System.currentTimeMillis();

    }
    static List<String> topics;
    private static  int getTotalSubscriptions() throws Exception {

        int totalSubscribers = 0;
        topics = getTopicList();

        for (String topic : topics) {
         subscriptionsList = subscriptionStore.getActiveClusterSubscribersForDestination(topic, true);
            totalSubscribers += subscriptionsList.size();
        }

        return totalSubscribers;

    }

    private static List<String> getTopicList() throws Exception {

        MessagingEngine messaginEngine = MessagingEngine.getInstance();
        subscriptionStore = messaginEngine.getSubscriptionStore();
        List<String> topics = subscriptionStore.getTopics();

        return topics;


    }


}
