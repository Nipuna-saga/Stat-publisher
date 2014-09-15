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

package org.wso2.carbon.stat.publisher.internal.ds;

import org.apache.log4j.Logger;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.stat.publisher.conf.StatPublisherConfiguration;
import org.wso2.carbon.stat.publisher.exception.StatPublisherConfigurationException;
import org.wso2.carbon.stat.publisher.publisher.StatPublisherManager;
import org.wso2.carbon.stat.publisher.registry.RegistryPersistenceManager;
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

public class StatPublisherDS {

    private static Logger logger = Logger.getLogger(StatPublisherDS.class);
    public RegistryPersistenceManager registryPersistenceManagerObject;
    public StatPublisherConfiguration statPublisherConfigurationInstance;


    protected void activate(ComponentContext context) throws StatPublisherConfigurationException {
        System.out.println("=====================Activating bundle=====================");
        StatPublisherManager statPublisherManager = new StatPublisherManager();
        statPublisherManager.onStart(CarbonContext.getThreadLocalCarbonContext().getTenantId());
        System.out.println("=====================Activated bundle=====================");

    }
//

    protected void deactivate(ComponentContext context) {
    }

    protected void setConfigurationContextService(
            ConfigurationContextService configurationContextService) {
        ServiceValueHolder.getInstance().setConfigurationContextService(configurationContextService);
    }

    protected void unsetConfigurationContextService(
            ConfigurationContextService configurationContextService) {
        ServiceValueHolder.getInstance().setConfigurationContextService(null);
    }

    protected void setRegistryService(RegistryService registryService) {
        ServiceValueHolder.getInstance().setRegistryService(registryService);
    }

    protected void unsetRegistryService(RegistryService registryService) {
        ServiceValueHolder.getInstance().setRegistryService(null);
    }

    protected void setRealmService(RealmService realmService) {
        ServiceValueHolder.getInstance().setRealmService(realmService);
    }

    protected void unsetRealmService(RealmService realmService) {
        ServiceValueHolder.getInstance().setRealmService(null);
    }

}
