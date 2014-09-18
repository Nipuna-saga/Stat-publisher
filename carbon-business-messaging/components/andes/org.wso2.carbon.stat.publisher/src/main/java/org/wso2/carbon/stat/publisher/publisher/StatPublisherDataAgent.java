package org.wso2.carbon.stat.publisher.publisher;

import org.apache.log4j.Logger;
import org.wso2.carbon.stat.publisher.conf.JMXConfiguration;
import org.wso2.carbon.stat.publisher.conf.StatPublisherConfiguration;
import org.wso2.carbon.stat.publisher.conf.StreamConfiguration;
import org.wso2.carbon.stat.publisher.serverStats.MbeansStats;
import org.wso2.carbon.stat.publisher.serverStats.data.MbeansStatsData;

import javax.management.*;
import java.io.IOException;


public class StatPublisherDataAgent {

    private static JMXConfiguration jmxConfiguration;
    private static StreamConfiguration streamConfiguration;
    private static StatPublisherConfiguration statPublisherConfiguration;

    private  MbeansStats mbeansStats;
    MbeansStatsData mbeansStatsData;

    private static Logger logger = Logger.getLogger(StatPublisherDataAgent.class);

    public StatPublisherDataAgent(JMXConfiguration jmxConfiguration,
                                  StreamConfiguration streamConfiguration,
                                  StatPublisherConfiguration statPublisherConfiguration) {

        this.jmxConfiguration = jmxConfiguration;
        this.streamConfiguration = streamConfiguration;
        this.statPublisherConfiguration = statPublisherConfiguration;



    }


    public void sendSystemStats() throws MalformedObjectNameException, ReflectionException, IOException,
            InstanceNotFoundException, AttributeNotFoundException, MBeanException {

        mbeansStats =   new MbeansStats(jmxConfiguration);


            mbeansStatsData = mbeansStats.getMbeansStatsData();
            System.out.println(mbeansStatsData.getNonHeapMemoryUsage());
            System.out.println(mbeansStatsData.getHeapMemoryUsage());
            System.out.println(mbeansStatsData.getCPULoadAverage());


    }


}
