package org.wso2.carbon.stat.publisher.publisher;

import org.apache.log4j.Logger;
import org.wso2.carbon.bam.message.tracer.handler.stream.StreamDefCreator;
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
    MbeansStatsData mbeansStatsData;

    StreamDefinition ServerStatsStreamDef;


    private static Logger logger = Logger.getLogger(StatPublisherDataAgent.class);

    public StatPublisherDataAgent(JMXConfiguration jmxConfiguration,
                                  StreamConfiguration streamConfiguration,
                                  StatPublisherConfiguration statPublisherConfiguration) {

        this.jmxConfiguration = jmxConfiguration;
        this.streamConfiguration = streamConfiguration;
        this.statPublisherConfiguration = statPublisherConfiguration;

        try {
            ServerStatsStreamDef = StreamDefCreator.getStreamDef();
        } catch (MalformedStreamDefinitionException e) {
            e.printStackTrace();
        }

    }


    public void sendSystemStats() throws MalformedObjectNameException, ReflectionException, IOException,
            InstanceNotFoundException, AttributeNotFoundException, MBeanException {


        if(mbeansStats==null){
            mbeansStats =   new MbeansStats(jmxConfiguration);
        }

        mbeansStatsData = mbeansStats.getMbeansStatsData();

        ArrayList<ReceiverGroup> allReceiverGroups = new ArrayList<ReceiverGroup>();

        ArrayList<DataPublisherHolder> dataPublisherHolders = new ArrayList<DataPublisherHolder>();

        String[] urls = {"tcp://localhost:7611"};

        for (String aUrl : urls) {

           DataPublisherHolder aNode = new DataPublisherHolder(null, aUrl.trim(), "admin","admin");
            dataPublisherHolders.add(aNode);
        }


        ReceiverGroup group = new ReceiverGroup(dataPublisherHolders);
        allReceiverGroups.add(group);

        LoadBalancingDataPublisher loadBalancingDataPublisher = new LoadBalancingDataPublisher(allReceiverGroups);



        List<Object> metaData = getMetaData();
        List<Object> correlationData = getCorrelationData();
        List<Object> payLoadData = getPayLoadData();


        loadBalancingDataPublisher.addStreamDefinition(ServerStatsStreamDef);

        try {
            loadBalancingDataPublisher.publish(ServerStatsStreamDef.getName(), ServerStatsStreamDef.getVersion(),
                    getObjectArray(metaData), getObjectArray(correlationData),
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
        List<Object> metaData = new ArrayList<Object>(7);
        metaData.add("127.0.0.1");
        return metaData;
    }
    public static List<Object> getCorrelationData() {
        List<Object> correlationData = new ArrayList<Object>(1);
        //correlationData.add(tracingInfo.getActivityId());
        return correlationData;
    }
    public static List<Object> getPayLoadData() {
        List<Object> payloadData = new ArrayList<Object>(7);
        payloadData.add("asdf");
        payloadData.add("asdfasd");
        payloadData.add("asdfas");
        payloadData.add(23423423);


        return payloadData;
    }


}
