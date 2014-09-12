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
import org.wso2.carbon.stat.publisher.internal.DTO.StatConfigurationDTO;
import org.wso2.carbon.stat.publisher.internal.data.StatConfiguration;
import org.wso2.carbon.stat.publisher.internal.publisher.PublisherObserver;
import org.wso2.carbon.stat.publisher.internal.util.StatPublisherException;

public class StatPublisherService {

    private static Logger logger = Logger.getLogger(StatPublisherService.class);
    private StatConfigurationDTO StatConfigurationDTOObject;

    //StatConfiguration details get method
    public StatConfiguration getStatConfiguration() throws StatPublisherException {
        int tenantID = CarbonContext.getThreadLocalCarbonContext().getTenantId();//get tenant ID

        StatConfigurationDTOObject = new StatConfigurationDTO();

        return StatConfigurationDTOObject.LoadConfigurationData(tenantID);

    }

    //StatConfiguration details set method
    public void setStatConfiguration(StatConfiguration StatConfigurationData) {
        int tenantID = CarbonContext.getThreadLocalCarbonContext().getTenantId();//get tenant ID
        StatConfigurationDTOObject = new StatConfigurationDTO();

        PublisherObserver.statConfigurationInstance = StatConfigurationData;

        //check SystemStat,MessageBrokerStat and Stat Publisher enable or not
        if ((StatConfigurationData.isSystem_statEnable() || StatConfigurationData.isMB_statEnable()) &&
                StatConfigurationData.isEnableStatPublisher()) {

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
            StatConfigurationDTOObject.StoreConfigurationData(StatConfigurationData, tenantID);
        } catch (StatPublisherException e) {
            e.printStackTrace();
        }


    }


}
