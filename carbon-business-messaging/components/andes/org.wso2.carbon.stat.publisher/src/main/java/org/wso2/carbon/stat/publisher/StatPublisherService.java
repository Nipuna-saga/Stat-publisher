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
import org.wso2.carbon.stat.publisher.conf.StatPublisherConfiguration;
import org.wso2.carbon.stat.publisher.exception.StatPublisherConfigurationException;
import org.wso2.carbon.stat.publisher.publisher.StatPublisherManager;
<<<<<<< HEAD
import org.wso2.carbon.stat.publisher.registry.RegistryPersistenceManager;
import org.wso2.carbon.stat.publisher.registry.StatConfigurationDTO;
=======
>>>>>>> a0a35303665ece9e33047e29ae468bd71027c771

public class StatPublisherService {

    //TODO statPublisherConfiguration (name change)
    private static Logger logger;

<<<<<<< HEAD
    private RegistryPersistenceManager registryPersistenceManagerObject;
    private StatConfigurationDTO statConfigurationDTO;
=======
    private static Logger logger = Logger.getLogger(StatPublisherService.class);

>>>>>>> a0a35303665ece9e33047e29ae468bd71027c771

    //StatPublisherConfiguration details get method
    public StatPublisherConfiguration getStatConfiguration() throws StatPublisherConfigurationException {
        logger = Logger.getLogger(StatPublisherService.class);
        int tenantID = CarbonContext.getThreadLocalCarbonContext().getTenantId();//get tenant ID
        RegistryPersistenceManager registryPersistenceManagerObject=new RegistryPersistenceManager();

        return registryPersistenceManagerObject.loadConfigurationData(tenantID);

    }

    //StatConfiguration details set method
    public void setStatConfiguration(StatPublisherConfiguration statPublisherConfiguration) throws StatPublisherConfigurationException {
        int tenantID = CarbonContext.getThreadLocalCarbonContext().getTenantId();//get tenant ID
<<<<<<< HEAD
        statConfigurationDTO = new StatConfigurationDTO();

        StatPublisherManager statPublisherManager = new StatPublisherManager();
=======
        RegistryPersistenceManager registryPersistenceManagerObject=new RegistryPersistenceManager();
        StatPublisherManager statPublisherManager=new StatPublisherManager();
>>>>>>> a0a35303665ece9e33047e29ae468bd71027c771
        statPublisherManager.onStop(tenantID);
        try {
             registryPersistenceManagerObject.storeConfigurationData(statPublisherConfiguration, tenantID);
        } catch (StatPublisherConfigurationException e) {
            logger.error("Error occurs while trying to store configurations values to registry", e);
        }
        statPublisherManager.onStart(tenantID);

    }


}
