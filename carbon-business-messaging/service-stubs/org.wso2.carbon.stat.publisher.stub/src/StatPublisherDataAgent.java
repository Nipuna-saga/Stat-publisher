package org.wso2.carbon.stat.publisher.publisher;

import org.wso2.carbon.stat.publisher.conf.JMXConfiguration;
import org.wso2.carbon.stat.publisher.conf.ReadStreamConfiguration;
import org.wso2.carbon.stat.publisher.conf.StatConfiguration;


public class StatPublisherDataAgent {

    private JMXConfiguration jmxConfiguration;
    private ReadStreamConfiguration readStreamConfiguration;
    private StatConfiguration statConfiguration;

    public StatPublisherDataAgent(JMXConfiguration jmxConfiguration,
                                  ReadStreamConfiguration readStreamConfiguration, StatConfiguration statConfiguration) {

        this.jmxConfiguration = jmxConfiguration;
        this.readStreamConfiguration = readStreamConfiguration;
        this.statConfiguration = statConfiguration;


    }


}
