package org.wso2.carbon.stat.publisher.publisher;

import org.wso2.carbon.stat.publisher.conf.JMXConfiguration;
import org.wso2.carbon.stat.publisher.conf.StreamConfiguration;
import org.wso2.carbon.stat.publisher.exception.StatPublisherConfigurationException;
import org.wso2.carbon.stat.publisher.util.XMLConfigurationReader;

import java.util.HashMap;

/**
 * StatPublisherManager manage all statPublisherObservers and store them in Hash map
 * if bundle activation or UI data storing events occurs StatPublisherObserver will change in Hash map
 */
public class StatPublisherManager {

    private JMXConfiguration jmxConfiguration;
    private StreamConfiguration streamConfiguration;
    private XMLConfigurationReader xmlConfigurationReader;

    private StatPublisherObserver statPublisherObserver;
    private HashMap<Integer, StatPublisherObserver> statPublisherObserverHashMap =
            new HashMap<Integer, StatPublisherObserver>();

    //
    public StatPublisherManager() throws StatPublisherConfigurationException {

        xmlConfigurationReader = new XMLConfigurationReader();
        jmxConfiguration = xmlConfigurationReader.readJMXConfiguration();
        streamConfiguration = xmlConfigurationReader.readStreamConfiguration();

    }

    /**
     * Create new StatPublisherObserver Instance and store it in Hash map by using tenant ID as key value
     */

    public void onStart(int tenantID) throws StatPublisherConfigurationException {

        statPublisherObserver = new StatPublisherObserver(jmxConfiguration, streamConfiguration, tenantID);
        statPublisherObserver.startMonitor();
        statPublisherObserverHashMap.put(tenantID, statPublisherObserver);

    }

    /**
     * Stop monitoring process
     */

    public void onStop(int tenantID) throws StatPublisherConfigurationException {

        statPublisherObserver = statPublisherObserverHashMap.get(tenantID);
        statPublisherObserver.stopMonitor();

    }

    /**
     * get Message stat Enable flag
     */

    public boolean getMessageStatEnableFlag(int tenantID) {

        statPublisherObserver = statPublisherObserverHashMap.get(tenantID);
        return statPublisherObserver.getEnable();

    }


}
