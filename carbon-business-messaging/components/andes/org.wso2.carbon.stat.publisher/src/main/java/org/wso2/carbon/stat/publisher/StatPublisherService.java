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

package org.wso2.carbon.stat.publisher;

import org.apache.log4j.Logger;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.stat.publisher.Registry.RegistryPersistenceManager;
import org.wso2.carbon.stat.publisher.conf.StatPublisherConfiguration;
import org.wso2.carbon.stat.publisher.exception.StatPublisherConfigurationException;
import org.wso2.carbon.stat.publisher.publisher.PublisherObserver;

public class StatPublisherService {

//TODO statPublisherConfiguration (name change)

    private static Logger logger = Logger.getLogger(StatPublisherService.class);
    private RegistryPersistenceManager registryPersistenceManagerObject;

    //StatPublisherConfiguration details get method
    public StatPublisherConfiguration getStatConfiguration() throws StatPublisherConfigurationException {
        int tenantID = CarbonContext.getThreadLocalCarbonContext().getTenantId();//get tenant ID

        registryPersistenceManagerObject = new RegistryPersistenceManager();

        return registryPersistenceManagerObject.loadConfigurationData(tenantID);

    }

    //StatPublisherConfiguration details set method
    public void setStatConfiguration(StatPublisherConfiguration statPublisherConfigurationData) {
        int tenantID = CarbonContext.getThreadLocalCarbonContext().getTenantId();//get tenant ID
        registryPersistenceManagerObject = new RegistryPersistenceManager();

        PublisherObserver.statPublisherConfigurationInstance = statPublisherConfigurationData;
        System.out.println(statPublisherConfigurationData.getNodeURL());

        //check SystemStat,MessageBrokerStat and Stat Publisher enable or not
        if ((statPublisherConfigurationData.isSystemStatEnable() || statPublisherConfigurationData.isMBStatEnable()) &&
                statPublisherConfigurationData.isEnableStatPublisher()) {

            //check timer task already initialized
            if (!PublisherObserver.timerFlag) {

                PublisherObserver publisherObserverInstance = new PublisherObserver();
                publisherObserverInstance.statPublisherTimerTask();
                PublisherObserver.timerFlag = true;
                logger.info("==================Stat Publishing Activated==================");
            }

        } else {
            if (PublisherObserver.timerFlag) {

                PublisherObserver.timer.cancel();
                PublisherObserver.timerFlag = false;
                logger.info("==================Stat Publishing Deactivated==================");
            }
        }
        try {
            registryPersistenceManagerObject.storeConfigurationData(statPublisherConfigurationData, tenantID);
        } catch (StatPublisherConfigurationException e) {
            e.printStackTrace();
        }


    }


}
