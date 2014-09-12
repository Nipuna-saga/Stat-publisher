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

package org.wso2.carbon.stat.publisher.DTO;

import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.stat.publisher.data.StatConfiguration;
import org.wso2.carbon.stat.publisher.util.StatPublisherException;
import org.wso2.carbon.utils.xml.StringUtils;

/**
 * Handle Registry while store and retrieve data sent from User Interface
 */
public class StatConfigurationDTO {

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
     * Updates the Registry with given configuration data.
     * @param statConfigurationWriteObject - eventing configuration data
     * @param tenantId                     - get tenantID
     */
    public void storeConfigurationData(StatConfiguration statConfigurationWriteObject, int tenantId)
            throws StatPublisherException {
        try {
            Registry registry = registryService.getConfigSystemRegistry(tenantId);
            if (statConfigurationWriteObject.isEnableStatPublisher()) {
                updateConfigProperty(StatConfigurationConstants.ENABLE_STAT_PUBLISHER,
                                     statConfigurationWriteObject.isEnableStatPublisher(), registry);
                updateConfigProperty(StatConfigurationConstants.USER_NAME,
                                     statConfigurationWriteObject.getUsername(), registry);
                updateConfigProperty(StatConfigurationConstants.PASSWORD,
                                     statConfigurationWriteObject.getPassword(), registry);
                updateConfigProperty(StatConfigurationConstants.URL,
                                     statConfigurationWriteObject.getURL(), registry);
                updateConfigProperty(StatConfigurationConstants.MB_STAT_ENABLE,
                                     statConfigurationWriteObject.isMB_statEnable(), registry);
                updateConfigProperty(StatConfigurationConstants.MESSAGE_STAT_ENABLE,
                                     statConfigurationWriteObject.isMessage_statEnable(), registry);
                updateConfigProperty(StatConfigurationConstants.SYSTEM_STAT_ENABLE,
                                     statConfigurationWriteObject.isSystem_statEnable(), registry);
            } else {
                String userName =
                        getConfigurationProperty(StatConfigurationConstants.USER_NAME, registry);
                String password =
                        getConfigurationProperty(StatConfigurationConstants.PASSWORD, registry);
                String url =
                        getConfigurationProperty(StatConfigurationConstants.URL, registry);
                String mbStatEnable =
                        getConfigurationProperty(StatConfigurationConstants.MB_STAT_ENABLE, registry);
                String messageStatEnable =
                        getConfigurationProperty(StatConfigurationConstants.MESSAGE_STAT_ENABLE, registry);
                String systemStatEnable =
                        getConfigurationProperty(StatConfigurationConstants.SYSTEM_STAT_ENABLE, registry);

                if (!StringUtils.isEmpty(url) && !StringUtils.isEmpty(userName) && !StringUtils.isEmpty(password)) {
                    statConfigurationWriteObject.setURL(url);
                    statConfigurationWriteObject.setUsername(userName);
                    statConfigurationWriteObject.setPassword(password);
                    statConfigurationWriteObject.setMB_statEnable(Boolean.parseBoolean(mbStatEnable));
                    statConfigurationWriteObject.setMessage_statEnable(Boolean.parseBoolean(messageStatEnable));
                    statConfigurationWriteObject.setSystem_statEnable(Boolean.parseBoolean(systemStatEnable));
                }}
                updateConfigProperty(StatConfigurationConstants.ENABLE_STAT_PUBLISHER,
                                     statConfigurationWriteObject.isEnableStatPublisher(), registry);


        } catch (RegistryException e) {
            throw new StatPublisherException("Could not update registry", e);
        }
    }

    /**
     * Update the properties
     * @param propertyName - registry property name which store value
     * @param value        - value to store
     * @param registry     - load registry
     * @throws StatPublisherException
     */
    public void updateConfigProperty(String propertyName, Object value, Registry registry)
            throws StatPublisherException {
        String resourcePath = StatConfigurationConstants.MEDIATION_STATISTICS_REG_PATH + propertyName;
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
                throw new StatPublisherException("Could not update property in registry!",e);
            }
        }
    }

    /**
     * Load configuration from Registry.
     * @param tenantId - get tenantID
     * @return statConfigurationObject - statConfiguration class object with retrieved values from
     *                                   registry
     */
    public StatConfiguration loadConfigurationData(int tenantId) throws StatPublisherException {

        StatConfiguration statConfigurationReadObject = new StatConfiguration();

        // First set it to defaults, but do not persist
        statConfigurationReadObject.setEnableStatPublisher(false);
        statConfigurationReadObject.setUsername(EMPTY_STRING);
        statConfigurationReadObject.setPassword(EMPTY_STRING);
        statConfigurationReadObject.setURL(EMPTY_STRING);
        statConfigurationReadObject.setMB_statEnable(false);
        statConfigurationReadObject.setMessage_statEnable(false);
        statConfigurationReadObject.setSystem_statEnable(false);

        // Then load values from registry
        try {
            Registry registry = registryService.getConfigSystemRegistry(tenantId);
            String enableStatPublisher =
                    getConfigurationProperty(StatConfigurationConstants.ENABLE_STAT_PUBLISHER, registry);
            String userName =
                    getConfigurationProperty(StatConfigurationConstants.USER_NAME, registry);
            String password =
                    getConfigurationProperty(StatConfigurationConstants.PASSWORD, registry);
            String url =
                    getConfigurationProperty(StatConfigurationConstants.URL, registry);
            String mbStatEnable =
                    getConfigurationProperty(StatConfigurationConstants.MB_STAT_ENABLE, registry);
            String messageStatEnable =
                    getConfigurationProperty(StatConfigurationConstants.MESSAGE_STAT_ENABLE, registry);
            String systemStatEnable =
                    getConfigurationProperty(StatConfigurationConstants.SYSTEM_STAT_ENABLE, registry);

            if (!StringUtils.isEmpty(url) && !StringUtils.isEmpty(userName) && !StringUtils.isEmpty(password)) {

                statConfigurationReadObject.setEnableStatPublisher(Boolean.parseBoolean(enableStatPublisher));
                statConfigurationReadObject.setURL(url);
                statConfigurationReadObject.setUsername(userName);
                statConfigurationReadObject.setPassword(password);
                statConfigurationReadObject.setMB_statEnable(Boolean.parseBoolean(mbStatEnable));
                statConfigurationReadObject.setMessage_statEnable(Boolean.parseBoolean(messageStatEnable));
                statConfigurationReadObject.setSystem_statEnable(Boolean.parseBoolean(systemStatEnable));
            }
        } catch (RegistryException e) {
            throw new StatPublisherException("Could not load values from registry", e);
        }
        return statConfigurationReadObject;
    }

    /**
     * Read the resource from registry
     * @param propertyName - get resource name
     * @param registry     - load registry
     * @return value       - stored value in registry
     * @throws StatPublisherException
     */
    public String getConfigurationProperty(String propertyName, Registry registry)
            throws StatPublisherException {
        String resourcePath = StatConfigurationConstants.MEDIATION_STATISTICS_REG_PATH + propertyName;
        String value = null;
        if (registry != null) {
            try {
                if (registry.resourceExists(resourcePath)) {
                    Resource resource = registry.get(resourcePath);
                    value = resource.getProperty(propertyName);
                }
            } catch (RegistryException e) {
                throw new StatPublisherException("Error in retrieving property from registry", e);
            }
        }
        return value;
    }

}


