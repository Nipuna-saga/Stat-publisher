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

package org.wso2.carbon.stat.publisher.registry;

import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.stat.publisher.conf.StatPublisherConfiguration;
import org.wso2.carbon.stat.publisher.exception.StatPublisherConfigurationException;
import org.wso2.carbon.stat.publisher.internal.ds.ServiceValueHolder;
import org.wso2.carbon.stat.publisher.util.StatPublisherConstants;
import org.wso2.carbon.utils.xml.StringUtils;

/**
 * Handle registry while store and retrieve data sent from User Interface
 */
public class RegistryPersistenceManager {

    private static RegistryService registryService;

    /**
     * Updates the registry with given configuration data.
     * @param statPublisherConfigurationWriteObject - eventing configuration data
     * @param tenantId                     - get tenantID
     */
    public void storeConfigurationData(StatPublisherConfiguration statPublisherConfigurationWriteObject, int tenantId)
            throws StatPublisherConfigurationException {
        try {
            Registry registry = ServiceValueHolder.getInstance().getRegistryService().getConfigSystemRegistry(tenantId);
            if (statPublisherConfigurationWriteObject.isEnableStatPublisher()) {
                String resourcePath = StatPublisherConstants.MEDIATION_STATISTICS_REG_PATH + StatPublisherConstants.RESOURCE;
                Resource resource;
                if (registry != null) {
                        if (!registry.resourceExists(resourcePath)) {
                            resource = registry.newResource();
                            resource.addProperty(StatPublisherConstants.ENABLE_STAT_PUBLISHER,
                                                 Boolean.toString(statPublisherConfigurationWriteObject.isEnableStatPublisher()));
                            resource.addProperty(StatPublisherConstants.USER_NAME,
                                                 statPublisherConfigurationWriteObject.getUsername());
                            resource.addProperty(StatPublisherConstants.PASSWORD,
                                                 statPublisherConfigurationWriteObject.getPassword());
                            resource.addProperty(StatPublisherConstants.URL,
                                                 statPublisherConfigurationWriteObject.getUrl());
                            resource.addProperty(StatPublisherConstants.MESSAGE_STAT_ENABLE,
                                                 Boolean.toString(statPublisherConfigurationWriteObject.isMessageStatEnable()));
                            resource.addProperty(StatPublisherConstants.SYSTEM_STAT_ENABLE,
                                                 Boolean.toString(statPublisherConfigurationWriteObject.isSystemStatEnable()));
                            resource.addProperty(StatPublisherConstants.MB_STAT_ENABLE,
                                                 Boolean.toString(statPublisherConfigurationWriteObject.isMBStatEnable()));
                            registry.put(resourcePath, resource);
                        } else {
                            resource = registry.get(resourcePath);
                            resource.setProperty(StatPublisherConstants.ENABLE_STAT_PUBLISHER,
                                                 Boolean.toString(statPublisherConfigurationWriteObject.isEnableStatPublisher()));
                            resource.setProperty(StatPublisherConstants.USER_NAME,
                                                 statPublisherConfigurationWriteObject.getUsername());
                            resource.setProperty(StatPublisherConstants.PASSWORD,
                                                 statPublisherConfigurationWriteObject.getPassword());
                            resource.setProperty(StatPublisherConstants.URL,
                                                 statPublisherConfigurationWriteObject.getUrl());
                            resource.setProperty(StatPublisherConstants.MESSAGE_STAT_ENABLE,
                                                 Boolean.toString(statPublisherConfigurationWriteObject.isMessageStatEnable()));
                            resource.setProperty(StatPublisherConstants.SYSTEM_STAT_ENABLE,
                                                 Boolean.toString(statPublisherConfigurationWriteObject.isSystemStatEnable()));
                            resource.setProperty(StatPublisherConstants.MB_STAT_ENABLE,
                                                 Boolean.toString(statPublisherConfigurationWriteObject.isMBStatEnable()));
                            registry.put(resourcePath, resource);
                        }
                }
            }
        } catch (RegistryException e) {
            throw new StatPublisherConfigurationException("Could not update registry", e);
        }
    }



    /**
     * Load configuration from registry.
     * @param tenantId - get tenantID
     * @return statConfigurationObject - statConfiguration class object with retrieved values from
     *                                   registry
     */
    public StatPublisherConfiguration loadConfigurationData(int tenantId) throws
                                                                 StatPublisherConfigurationException {

        StatPublisherConfiguration statPublisherConfigurationReadObject = new StatPublisherConfiguration();
       
        try {
            Registry registry = ServiceValueHolder.getInstance().getRegistryService().getConfigSystemRegistry(tenantId);
            String enableStatPublisher =
                    getConfigurationProperty(StatPublisherConstants.ENABLE_STAT_PUBLISHER, registry);
            String userName =
                    getConfigurationProperty(StatPublisherConstants.USER_NAME, registry);
            String password =
                    getConfigurationProperty(StatPublisherConstants.PASSWORD, registry);
            String url =
                    getConfigurationProperty(StatPublisherConstants.URL, registry);
            String mbStatEnable =
                    getConfigurationProperty(StatPublisherConstants.MB_STAT_ENABLE, registry);
            String messageStatEnable =
                    getConfigurationProperty(StatPublisherConstants.MESSAGE_STAT_ENABLE, registry);
            String systemStatEnable =
                    getConfigurationProperty(StatPublisherConstants.SYSTEM_STAT_ENABLE, registry);

            if (!StringUtils.isEmpty(url) && !StringUtils.isEmpty(userName) && !StringUtils.isEmpty(password)) {

                statPublisherConfigurationReadObject.setEnableStatPublisher(Boolean.parseBoolean(enableStatPublisher));
                statPublisherConfigurationReadObject.setUrl(url);
                statPublisherConfigurationReadObject.setUsername(userName);
                statPublisherConfigurationReadObject.setPassword(password);
                statPublisherConfigurationReadObject.setMBStatEnable(Boolean.parseBoolean(mbStatEnable));
                statPublisherConfigurationReadObject.setMessageStatEnable(Boolean.parseBoolean(messageStatEnable));
                statPublisherConfigurationReadObject.setSystemStatEnable(Boolean.parseBoolean(systemStatEnable));
            }
        } catch (RegistryException e) {
            throw new StatPublisherConfigurationException("Could not load values from registry", e);
        }
        return statPublisherConfigurationReadObject;
    }

    /**
     * Read the resource from registry
     * @param propertyName - get resource name
     * @param registry     - load registry
     * @return value       - stored value in registry
     * @throws org.wso2.carbon.stat.publisher.exception.StatPublisherConfigurationException
     */
    public String getConfigurationProperty(String propertyName, Registry registry)
            throws StatPublisherConfigurationException {
        String resourcePath = StatPublisherConstants.MEDIATION_STATISTICS_REG_PATH + propertyName;
        String value = null;
        if (registry != null) {
            try {
                if (registry.resourceExists(resourcePath)) {
                    Resource resource = registry.get(resourcePath); //TODO StatisticConfiguration (resource name)
                    value = resource.getProperty(propertyName);
                }
            } catch (RegistryException e) {
                throw new StatPublisherConfigurationException("Error in retrieving property from registry", e);
            }
        }
        return value;
    }

}


