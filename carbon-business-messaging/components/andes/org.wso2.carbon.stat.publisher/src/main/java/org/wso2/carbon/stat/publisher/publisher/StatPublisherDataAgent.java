package org.wso2.carbon.stat.publisher.publisher;

import org.wso2.carbon.stat.publisher.conf.JMXConfiguration;
import org.wso2.carbon.stat.publisher.conf.StatPublisherConfiguration;
import org.wso2.carbon.stat.publisher.conf.StreamConfiguration;


public class StatPublisherDataAgent {

    private JMXConfiguration jmxConfiguration;
    private StreamConfiguration streamConfiguration;
    private StatPublisherConfiguration statPublisherConfiguration;

    public StatPublisherDataAgent(JMXConfiguration jmxConfiguration,
                                  StreamConfiguration streamConfiguration, StatPublisherConfiguration statPublisherConfiguration) {

        this.jmxConfiguration = jmxConfiguration;
        this.streamConfiguration = streamConfiguration;
        this.statPublisherConfiguration = statPublisherConfiguration;


    }


}
