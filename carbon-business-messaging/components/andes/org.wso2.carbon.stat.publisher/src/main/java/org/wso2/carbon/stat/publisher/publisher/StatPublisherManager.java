/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

package org.wso2.carbon.stat.publisher.publisher;

import org.wso2.carbon.stat.publisher.conf.JMXConfiguration;
import org.wso2.carbon.stat.publisher.conf.StatPublisherConfiguration;
import org.wso2.carbon.stat.publisher.conf.StreamConfiguration;
import org.wso2.carbon.stat.publisher.exception.StatPublisherConfigurationException;
import org.wso2.carbon.stat.publisher.util.XMLConfigurationReader;
import org.wso2.carbon.user.api.UserStoreException;

import java.util.HashMap;
import java.util.HashSet;

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

    public HashSet<String> getMessageStatEnableMap() {
        return messageStatEnableMap;
    }

    private HashSet<String> messageStatEnableMap = new HashSet<String>();

    public StatPublisherManager() throws StatPublisherConfigurationException {

        xmlConfigurationReader = new XMLConfigurationReader();
        jmxConfiguration = xmlConfigurationReader.readJMXConfiguration();
        streamConfiguration = xmlConfigurationReader.readStreamConfiguration();

    }

    /**
     * Create new StatPublisherObserver Instance and store it in Hash map by using tenant ID as key value
     */

    public void onCreate(int tenantID) throws StatPublisherConfigurationException {

        statPublisherObserver = new StatPublisherObserver(jmxConfiguration, streamConfiguration, tenantID);
        try {
            //start monitoring process
            statPublisherObserver.startMonitor();
        } catch (UserStoreException e) {
            e.printStackTrace();
        }
        //Add observer to Hash map
        statPublisherObserverHashMap.put(tenantID, statPublisherObserver);

        if (statPublisherObserver.getTenantDomain() != null) {
            //if message statPublisher is enable it's relevant tenant domain add to hash map
            messageStatEnableMap.add(statPublisherObserver.getTenantDomain());

        }

        System.out.println(statPublisherObserverHashMap + "" + messageStatEnableMap);

    }

    /**
     * Stop monitoring process
     */

    public void onUpdate(int tenantID) throws StatPublisherConfigurationException {

        statPublisherObserver = statPublisherObserverHashMap.get(tenantID);


        if (statPublisherObserver != null) {
            //stop monitoring process
            statPublisherObserver.stopMonitor();
            //remove tenant domain from hash map
            messageStatEnableMap.remove(statPublisherObserver.getTenantDomain());
        }


    }

    /**
     * get Message stat Enable flag value
     */


    public void onRemove(int tenantID) throws StatPublisherConfigurationException {

        statPublisherObserver = statPublisherObserverHashMap.get(tenantID);
        if (statPublisherObserver != null) {
            //remove tenant domain from hash map
            messageStatEnableMap.remove(statPublisherObserver.getTenantDomain());
            //remove publisher observer from hash map
            statPublisherObserverHashMap.remove(tenantID);
        }
        System.out.println(statPublisherObserverHashMap);
    }

    //This message use to get statPublisherConfiguration of specific tenant
    public StatPublisherConfiguration getStatPublisherConfiguration(int tenantID) {
        statPublisherObserver = statPublisherObserverHashMap.get(tenantID);
//TODO check sometimes this method will occur null pointer exception
        return statPublisherObserver.getStatPublisherConfiguration();


    }


}
