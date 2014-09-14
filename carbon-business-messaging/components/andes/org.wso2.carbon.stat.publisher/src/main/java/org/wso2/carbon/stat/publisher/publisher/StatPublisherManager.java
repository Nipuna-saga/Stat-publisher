package org.wso2.carbon.stat.publisher.publisher;

import org.wso2.carbon.stat.publisher.conf.JMXConfiguration;
import org.wso2.carbon.stat.publisher.conf.ReadStreamConfiguration;
import org.wso2.carbon.stat.publisher.exception.StatPublisherConfigurationException;

import java.util.HashMap;

/**
 * StatPublisherManager manage all statPublisherObservers and store them in Hash map
 * if bundle activation or UI data storing events occurs StatPublisherObserver will change in Hash map
 */
public class StatPublisherManager {

    private JMXConfiguration jmxConfiguration;
    private ReadStreamConfiguration readStreamConfiguration;

    private StatPublisherObserver statPublisherObserver;
    private static HashMap<Integer, StatPublisherObserver> statPublisherObserverHashMap =new HashMap<Integer, StatPublisherObserver>();;


    public StatPublisherManager() {
        //TODO get JMX configuration and StreamConfiguration
    //    statPublisherObserverHashMap = new HashMap<Integer, StatPublisherObserver>();

    }



    /**
     * Create new StatPublisherObserver Instance and store it in Hash map by using tenant ID as key value
     */

    public void onStart(int tenantID) throws StatPublisherConfigurationException {

        statPublisherObserver = new StatPublisherObserver(jmxConfiguration, readStreamConfiguration, tenantID);
        statPublisherObserver.startMonitor();
        statPublisherObserverHashMap.put(tenantID, statPublisherObserver);

    }


    public void onStop(int tenantID) throws StatPublisherConfigurationException {

        statPublisherObserver = statPublisherObserverHashMap.get(tenantID);
        statPublisherObserver.stopMonitor();

    }

}
