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

import org.osgi.service.component.ComponentContext;
import org.wso2.andes.kernel.MessagingEngine;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.stat.publisher.exception.StatPublisherConfigurationException;
import org.wso2.carbon.stat.publisher.publisher.MessageStatPublisher;
import org.wso2.carbon.stat.publisher.publisher.StatPublisherManager;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.carbon.utils.ConfigurationContextService;

//import org.wso2.carbon.stat.publisher.internal.ds.StatPublisherInterfaceImplementation;

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


<<<<<<< HEAD
    /**
     * Activate method in stat publisher bundle
     * @param context - Component context
     * @throws StatPublisherConfigurationException
     */
    protected void activate(ComponentContext context) throws StatPublisherConfigurationException {
=======

    protected void activate(ComponentContext context) throws StatPublisherConfigurationException, org.wso2.carbon.user.api.UserStoreException {
>>>>>>> 594361e666497db33cc81e9feafb6207ca65bcc7
        System.out.println("=====================Activating bundle=====================");
        StatPublisherManager statPublisherManager = new StatPublisherManager();
        ServiceValueHolder.getInstance().setStatPublisherManagerService(statPublisherManager);
        ServiceValueHolder.getInstance().getStatPublisherManagerService().
                onStart(CarbonContext.getThreadLocalCarbonContext().getTenantId());
        System.out.println("=====================Activated bundle=====================");

        MessagingEngine.getInstance().setStatPublisherGetMessageInterface(MessageStatPublisher.getInstance());


    }

    /**
     * Deactivate method in stat publisher component
     * @param context - Component context
     */
    protected void deactivate(ComponentContext context) {

    }

    /**
     * Set ConfigurationContextService
     * @param configurationContextService - ConfigurationContextService
     */
    protected void setConfigurationContextService(
            ConfigurationContextService configurationContextService) {
        ServiceValueHolder.getInstance().setConfigurationContextService(configurationContextService);
    }

    /**
     * Remove ConfigurationContextService
     * @param configurationContextService -ConfigurationContextService
     */
    protected void unsetConfigurationContextService(
            ConfigurationContextService configurationContextService) {
        ServiceValueHolder.getInstance().setConfigurationContextService(null);
    }

    /**
     * Set RegistryService
     * @param registryService - RegistryService
     */
    protected void setRegistryService(RegistryService registryService) {
        ServiceValueHolder.getInstance().setRegistryService(registryService);
    }

    /**
     * Remove RegistryService
     * @param registryService -RegistryService
     */
    protected void unsetRegistryService(RegistryService registryService) {
        ServiceValueHolder.getInstance().setRegistryService(null);
    }

    /**
     * Set RealmService
     * @param realmService - RealmService
     */
    protected void setRealmService(RealmService realmService) {
        ServiceValueHolder.getInstance().setRealmService(realmService);
    }

    /**
     * Remove RealmService
     * @param realmService - RealmService
     */
    protected void unsetRealmService(RealmService realmService) {
        ServiceValueHolder.getInstance().setRealmService(null);
    }

}
