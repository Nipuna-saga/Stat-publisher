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
import org.wso2.carbon.stat.publisher.util.StatPublisherConstants;
import org.wso2.carbon.utils.xml.StringUtils;

/**
 * Handle registry while store and retrieve data sent from User Interface
 */
public class StatConfigurationDTO {

    //TODO RegistryPersistanceManager (change name)

    public static final String EMPTY_STRING = "";
    private static RegistryService registryService;

    /**
     * Initialize registry service
     * @param registryServiceParam - registry service
     */
    public static void setRegistryService(RegistryService registryServiceParam) {
        registryService = registryServiceParam;
    }

    /**
     * Updates the registry with given configuration data.
     * @param statConfigurationWriteObject - eventing configuration data
     * @param tenantId                     - get tenantID
     */
    public void storeConfigurationData(StatPublisherConfiguration statConfigurationWriteObject, int tenantId)
            throws StatPublisherConfigurationException {
        try {
            Registry registry = registryService.getConfigSystemRegistry(tenantId);
            if (statConfigurationWriteObject.isEnableStatPublisher()) {
                updateConfigProperty(StatPublisherConstants.ENABLE_STAT_PUBLISHER,
                                     statConfigurationWriteObject.isEnableStatPublisher(), registry);
                updateConfigProperty(StatPublisherConstants.USER_NAME,
                                     statConfigurationWriteObject.getUsername(), registry);
                updateConfigProperty(StatPublisherConstants.PASSWORD,
                                     statConfigurationWriteObject.getPassword(), registry);
                updateConfigProperty(StatPublisherConstants.URL,
                                     statConfigurationWriteObject.getUrl(), registry);
                updateConfigProperty(StatPublisherConstants.MB_STAT_ENABLE,
                                     statConfigurationWriteObject.isMBStatEnable(), registry);
                updateConfigProperty(StatPublisherConstants.MESSAGE_STAT_ENABLE,
                                     statConfigurationWriteObject.isMessageStatEnable(), registry);
                updateConfigProperty(StatPublisherConstants.SYSTEM_STAT_ENABLE,
                                     statConfigurationWriteObject.isSystemStatEnable(), registry);
            } else {
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
                    statConfigurationWriteObject.setUrl(url);
                    statConfigurationWriteObject.setUsername(userName);
                    statConfigurationWriteObject.setPassword(password);
                    statConfigurationWriteObject.setMBStatEnable(Boolean.parseBoolean(mbStatEnable));
                    statConfigurationWriteObject.setMessageStatEnable(Boolean.parseBoolean(messageStatEnable));
                    statConfigurationWriteObject.setSystemStatEnable(Boolean.parseBoolean(systemStatEnable));
                }}
                updateConfigProperty(StatPublisherConstants.ENABLE_STAT_PUBLISHER,
                                     statConfigurationWriteObject.isEnableStatPublisher(), registry);


        } catch (RegistryException e) {
            throw new StatPublisherConfigurationException("Could not update registry", e);
        }
    }

    /**
     * Update the properties
     * @param propertyName - registry property name which store value
     * @param value        - value to store
     * @param registry     - load registry
     * @throws org.wso2.carbon.stat.publisher.exception.StatPublisherConfigurationException
     */
    public void updateConfigProperty(String propertyName, Object value, Registry registry)
            throws StatPublisherConfigurationException {
        String resourcePath = StatPublisherConstants.MEDIATION_STATISTICS_REG_PATH + propertyName;
        Resource resource;
        if (registry != null) {
            try {
                if (!registry.resourceExists(resourcePath)) {
                    resource = registry.newResource();
                    resource.addProperty(propertyName, value.toString());
                    registry.put(resourcePath, resource);
                } else {
                    resource = registry.get(resourcePath);
                    resource.setProperty(propertyName, value.toString());
                    registry.put(resourcePath, resource);
                }
            } catch (RegistryException e) {
                throw new StatPublisherConfigurationException("Could not update property in registry!",e);
            }
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

        StatPublisherConfiguration statConfigurationReadObject = new StatPublisherConfiguration();

        // First set it to defaults, but do not persist
        statConfigurationReadObject.setEnableStatPublisher(false);
        statConfigurationReadObject.setUsername(EMPTY_STRING);
        statConfigurationReadObject.setPassword(EMPTY_STRING);
        statConfigurationReadObject.setUrl(EMPTY_STRING);
        statConfigurationReadObject.setMBStatEnable(false);
        statConfigurationReadObject.setMessageStatEnable(false);
        statConfigurationReadObject.setSystemStatEnable(false);

        // Then load values from registry
        try {
            Registry registry = registryService.getConfigSystemRegistry(tenantId);
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

                statConfigurationReadObject.setEnableStatPublisher(Boolean.parseBoolean(enableStatPublisher));
                statConfigurationReadObject.setUrl(url);
                statConfigurationReadObject.setUsername(userName);
                statConfigurationReadObject.setPassword(password);
                statConfigurationReadObject.setMBStatEnable(Boolean.parseBoolean(mbStatEnable));
                statConfigurationReadObject.setMessageStatEnable(Boolean.parseBoolean(messageStatEnable));
                statConfigurationReadObject.setSystemStatEnable(Boolean.parseBoolean(systemStatEnable));
            }
        } catch (RegistryException e) {
            throw new StatPublisherConfigurationException("Could not load values from registry", e);
        }
        return statConfigurationReadObject;
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


