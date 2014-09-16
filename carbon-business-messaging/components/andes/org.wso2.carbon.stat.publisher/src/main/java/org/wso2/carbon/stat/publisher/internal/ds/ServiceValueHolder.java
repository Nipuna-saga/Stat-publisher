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

import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.carbon.utils.ConfigurationContextService;

/**
 * Keep values required for component service
 */
public class ServiceValueHolder {

    private RegistryService registryService;
    private RealmService realmService;
    private ServiceValueHolder() {
    }
    private static ServiceValueHolder instance = new ServiceValueHolder();
    private ConfigurationContextService configurationContextService;

    /**
     * Get the instance of ServiceValueHolder class
     * @return instance of ServiceValueHolder
     */
    public static ServiceValueHolder getInstance() {
        return instance;
    }

    /**
     * Get the configuration context of component
     * @return configuration context service of component
     */
    public ConfigurationContextService getConfigurationContextService() {
        return configurationContextService;
    }

    /**
     * Set the configuration context of component
     */
    public void setConfigurationContextService(
            ConfigurationContextService configurationContextService) {
        this.configurationContextService = configurationContextService;
    }
    /**
     * Initialize registry service
     * @param registryServiceParam - registry service
     */
    public void setRegistryService(RegistryService registryServiceParam) {
        this.registryService = registryServiceParam;
    }

    public RegistryService getRegistryService() {
        return registryService;
    }

    /**
     * Initialize realm service
     * @param realmServiceParam - Realm Service
     */
    public void setRealmService(RealmService realmServiceParam) {
        this.realmService = realmServiceParam;
    }

    public RealmService getRealmService() {
        return realmService;
    }


}
