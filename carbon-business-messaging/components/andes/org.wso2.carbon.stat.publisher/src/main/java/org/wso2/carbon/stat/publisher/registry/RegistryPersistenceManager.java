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
import org.wso2.carbon.stat.publisher.conf.StatPublisherConfiguration;
import org.wso2.carbon.stat.publisher.exception.StatPublisherConfigurationException;
import org.wso2.carbon.stat.publisher.internal.ds.ServiceValueHolder;
import org.wso2.carbon.stat.publisher.util.StatPublisherConstants;
import org.wso2.carbon.utils.xml.StringUtils;

/**
 * Handle registry while store and retrieve data sent from User Interface
 */
public class RegistryPersistenceManager {

    /**
     * Updates the registry with given configuration data.
     * @param statPublisherConfigurationWriteObject - eventing configuration data
     * @param tenantId                     - get tenantID
     */
    public void storeConfigurationData(StatPublisherConfiguration statPublisherConfigurationWriteObject, int tenantId)
            throws StatPublisherConfigurationException {
        try {
            Registry registry = ServiceValueHolder.getInstance().getRegistryService().getConfigSystemRegistry(tenantId);
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
            String resourcePath = StatPublisherConstants.MEDIATION_STATISTICS_REG_PATH + StatPublisherConstants.RESOURCE;

            if (registry != null) {
                if (registry.resourceExists(resourcePath)) {
                    Resource resource = registry.get(resourcePath);
                    String enableStatPublisher = resource.getProperty(StatPublisherConstants.ENABLE_STAT_PUBLISHER);
                    String userName = resource.getProperty(StatPublisherConstants.USER_NAME);
                    String password = resource.getProperty(StatPublisherConstants.PASSWORD);
                    String url = resource.getProperty(StatPublisherConstants.URL);
                    String mbStatEnable = resource.getProperty(StatPublisherConstants.MB_STAT_ENABLE);
                    String messageStatEnable = resource.getProperty(StatPublisherConstants.MESSAGE_STAT_ENABLE);
                    String systemStatEnable = resource.getProperty(StatPublisherConstants.SYSTEM_STAT_ENABLE);

                    if (!StringUtils.isEmpty(url) && !StringUtils.isEmpty(userName) && !StringUtils.isEmpty(password)) {
                        statPublisherConfigurationReadObject.setEnableStatPublisher(Boolean.parseBoolean(enableStatPublisher));
                        statPublisherConfigurationReadObject.setUrl(url);
                        statPublisherConfigurationReadObject.setUsername(userName);
                        statPublisherConfigurationReadObject.setPassword(password);
                        statPublisherConfigurationReadObject.setMBStatEnable(Boolean.parseBoolean(mbStatEnable));
                        statPublisherConfigurationReadObject.setMessageStatEnable(Boolean.parseBoolean(messageStatEnable));
                        statPublisherConfigurationReadObject.setSystemStatEnable(Boolean.parseBoolean(systemStatEnable));
                    }
                }
            }
        } catch (RegistryException e) {
            throw new StatPublisherConfigurationException("Could not load values from registry", e);
        }
        return statPublisherConfigurationReadObject;

    }


}

