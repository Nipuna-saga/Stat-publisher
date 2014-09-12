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

package org.wso2.carbon.stat.publisher.internal;

import org.apache.log4j.Logger;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.stat.publisher.StatPublisherService;
import org.wso2.carbon.stat.publisher.DTO.StatConfigurationDTO;
import org.wso2.carbon.stat.publisher.data.StatConfiguration;
import org.wso2.carbon.stat.publisher.publisher.DataAgent;
import org.wso2.carbon.stat.publisher.publisher.PublisherObserver;
import org.wso2.carbon.stat.publisher.util.StatPublisherException;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.carbon.utils.ConfigurationContextService;

/**
 * @scr.component name="org.wso2.carbon.stat.publisher" immediate="true"
 * @scr.reference name="configurationContext.service"
 * interface="org.wso2.carbon.utils.ConfigurationContextService" cardinality="1..1"
 * policy="dynamic" bind="setConfigurationContextService" unbind="unsetConfigurationContextService"
 * @scr.reference name="org.wso2.carbon.registry.service"
 * interface="org.wso2.carbon.registry.core.service.RegistryService" cardinality="1..1"
 * policy="dynamic" bind="setRegistryService" unbind="unsetRegistryService"
 * @scr.reference name="realm.service" interface="org.wso2.carbon.user.core.service.RealmService"
 * cardinality="1..1" policy="dynamic" bind="setRealmService" unbind="unsetRealmService"
 */

public class StatisticComponent {

    private static Logger logger = Logger.getLogger(StatisticComponent.class);
    public StatConfigurationDTO statConfigurationDTOObject;
    public StatConfiguration statConfigurationInstance;

    private ServiceRegistration statAdminServiceRegistration;

    protected void activate(ComponentContext context) throws StatPublisherException {

        try {
            StatPublisherService Service = StatPublisherBuilder.createMediationService();
            context.getBundleContext().registerService(StatPublisherService.class.getName(),
                                                       Service, null);
            statConfigurationDTOObject = new StatConfigurationDTO();
            statConfigurationInstance =
                    statConfigurationDTOObject.loadConfigurationData(CarbonContext.getThreadLocalCarbonContext().getTenantId());
            PublisherObserver.statConfigurationInstance = statConfigurationInstance;
            PublisherObserver.timerFlag = false;

            if ((statConfigurationInstance.isSystem_statEnable() || statConfigurationInstance.isMB_statEnable()) &&
                statConfigurationInstance.isEnableStatPublisher()) {
                PublisherObserver publisherObserverInstance = new PublisherObserver();
                publisherObserverInstance.statPublisherTimerTask();
                PublisherObserver.timerFlag = true;
            }
            logger.info("Successfully created the stat publisher service");
        } catch (RuntimeException e) {
            //TODO log and return
            throw new StatPublisherException("Can not create stat publisher service", e);
        }
    }

    protected void deactivate(ComponentContext context) {
        // unregistered MBStatsPublisherAdmin service from the OSGi Service Register.
        statAdminServiceRegistration.unregister();
        if (logger.isDebugEnabled()) {
            logger.debug("MB statistics publisher bundle is deactivated");
        }
    }

    protected void setConfigurationContextService(
            ConfigurationContextService configurationContextService) {
        ServiceValueHolder.getInstance().setConfigurationContextService(configurationContextService);
    }

    protected void unsetConfigurationContextService(
            ConfigurationContextService configurationContextService) {
        ServiceValueHolder.getInstance().setConfigurationContextService(null);
    }

    protected void setRegistryService(RegistryService registryService)
            throws StatPublisherException {
        try {
            StatConfigurationDTO.setRegistryService(registryService);
        } catch (Exception e) {
            throw new StatPublisherException("Cannot retrieve System Registry", e);
        }
    }

    protected void unsetRegistryService(RegistryService registryService) {
        StatConfigurationDTO.setRegistryService(null);
    }

    protected void setRealmService(RealmService realmService) throws StatPublisherException {
        try {
            DataAgent.setRealmService(realmService);
        } catch (Exception e) {
            throw new StatPublisherException("Cannot retrieve Realm Service", e);
        }
    }

    protected void unsetRealmService(RealmService realmService) {
        DataAgent.setRealmService(null);
    }

}
