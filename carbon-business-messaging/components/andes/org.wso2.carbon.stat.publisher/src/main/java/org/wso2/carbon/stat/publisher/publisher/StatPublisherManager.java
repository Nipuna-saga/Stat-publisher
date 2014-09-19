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

    public void onCreate(int tenantID) throws StatPublisherConfigurationException {

        statPublisherObserver = new StatPublisherObserver(jmxConfiguration, streamConfiguration, tenantID);
        statPublisherObserver.startMonitor();
        statPublisherObserverHashMap.put(tenantID, statPublisherObserver);
        System.out.println(statPublisherObserverHashMap);
    }

    /**
     * Stop monitoring process
     */

    public void onUpdate(int tenantID) throws StatPublisherConfigurationException {

        statPublisherObserver = statPublisherObserverHashMap.get(tenantID);
        if (statPublisherObserver != null) {
            statPublisherObserver.stopMonitor();
        }
    }

    /**
     * get Message stat Enable flag value
     */

    public boolean getMessageStatEnableFlag(int tenantID) {

        statPublisherObserver = statPublisherObserverHashMap.get(tenantID);
        return statPublisherObserver.getEnable();

    }

    public void onRemove(int tenantID) throws StatPublisherConfigurationException {

        statPublisherObserverHashMap.remove(tenantID);
        System.out.println(statPublisherObserverHashMap);
    }

}
