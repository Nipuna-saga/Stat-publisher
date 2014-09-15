package org.wso2.carbon.stat.publisher.publisher;

import org.apache.log4j.Logger;
import org.wso2.carbon.stat.publisher.conf.JMXConfiguration;
import org.wso2.carbon.stat.publisher.conf.StatPublisherConfiguration;
import org.wso2.carbon.stat.publisher.conf.StreamConfiguration;
import org.wso2.carbon.stat.publisher.exception.StatPublisherRuntimeException;
import org.wso2.carbon.stat.publisher.serverStats.MbeansStats;
import org.wso2.carbon.stat.publisher.serverStats.data.MbeansStatsData;

import javax.management.*;
import java.io.IOException;


public class StatPublisherDataAgent {

    private JMXConfiguration jmxConfiguration;
    private StreamConfiguration streamConfiguration;
    private StatPublisherConfiguration statPublisherConfiguration;

    private MbeansStats mbeansStats;
    MbeansStatsData mbeansStatsData;

    private static Logger logger = Logger.getLogger(StatPublisherDataAgent.class);

    public StatPublisherDataAgent(JMXConfiguration jmxConfiguration,
                                  StreamConfiguration streamConfiguration,
                                  StatPublisherConfiguration statPublisherConfiguration) {

        this.jmxConfiguration = jmxConfiguration;
        this.streamConfiguration = streamConfiguration;
        this.statPublisherConfiguration = statPublisherConfiguration;

        try {
            mbeansStats = new MbeansStats(jmxConfiguration);
        } catch (Exception e) {

            throw new StatPublisherRuntimeException("Fail to get Server statistics", e);

        }


    }


    public void sendSystemStats() throws MalformedObjectNameException, ReflectionException, IOException,
            InstanceNotFoundException, AttributeNotFoundException, MBeanException {

        mbeansStatsData = mbeansStats.getMbeansStatsData();
        System.out.println(mbeansStatsData.getNonHeapMemoryUsage());
        System.out.println(mbeansStatsData.getHeapMemoryUsage());
        System.out.println(mbeansStatsData.getCPULoadAverage());


    }


}
