package org.wso2.carbon.stat.publisher.internal.DTO;

import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.stat.publisher.internal.data.StatConfiguration;
import org.wso2.carbon.stat.publisher.internal.util.StatPublisherException;
import org.wso2.carbon.utils.xml.StringUtils;

public class StatConfigurationDTO {

    public static final String EMPTY_STRING = "";
    private static RegistryService registryService;

    public static void setRegistryService(RegistryService registryServiceParam) {
        registryService = registryServiceParam;
    }

    /**
     * Updates the Registry with given config data.
     *
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

                if (StringUtils.isEmpty(url) && StringUtils.isEmpty(userName) && StringUtils.isEmpty(password)) {
                    statConfigurationWriteObject.setURL(url);
                    statConfigurationWriteObject.setUsername(userName);
                    statConfigurationWriteObject.setPassword(password);
                    statConfigurationWriteObject.setMB_statEnable(Boolean.parseBoolean(mbStatEnable));
                    statConfigurationWriteObject.setMessage_statEnable(Boolean.parseBoolean(messageStatEnable));
                    statConfigurationWriteObject.setSystem_statEnable(Boolean.parseBoolean(systemStatEnable));
                }
                updateConfigProperty(StatConfigurationConstants.ENABLE_STAT_PUBLISHER,
                                     statConfigurationWriteObject.isEnableStatPublisher(), registry);
            }
        } catch (RegistryException e) {
            throw new StatPublisherException("Could not update registry", e);
        }
    }

    /**
     * Update the properties
     *
     * @param propertyName - resource name
     * @param value        - value to store
     * @param registry     - load registry
     * @throws RegistryException
     * @throws StatPublisherException
     */
    public void updateConfigProperty(String propertyName, Object value, Registry registry)
            throws RegistryException, StatPublisherException {
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
            } catch (Exception e) {
                throw new StatPublisherException("Error while accessing registry", e);
            }
        }
    }

    /**
     * Load configuration from Registry.
     *
     * @param tenantId - get tenantID
     * @return statConfigurationReadObject
     */
    public StatConfiguration LoadConfigurationData(int tenantId) throws StatPublisherException {

        StatConfiguration statConfigurationReadObject = new StatConfiguration();

        // First set it to defaults, but do not persist
        statConfigurationReadObject.setEnableStatPublisher(false);
        statConfigurationReadObject.setUsername(EMPTY_STRING);
        statConfigurationReadObject.setPassword(EMPTY_STRING);
        statConfigurationReadObject.setURL(EMPTY_STRING);
        statConfigurationReadObject.setMB_statEnable(false);
        statConfigurationReadObject.setMessage_statEnable(false);
        statConfigurationReadObject.setSystem_statEnable(false);

        // then load it from registry
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
            if (StringUtils.isEmpty(url)) {
                statConfigurationReadObject.setEnableStatPublisher(Boolean.parseBoolean(enableStatPublisher));
                statConfigurationReadObject.setURL(url);
                statConfigurationReadObject.setUsername(userName);
                statConfigurationReadObject.setPassword(password);
                statConfigurationReadObject.setMB_statEnable(Boolean.parseBoolean(mbStatEnable));
                statConfigurationReadObject.setMessage_statEnable(Boolean.parseBoolean(messageStatEnable));
                statConfigurationReadObject.setSystem_statEnable(Boolean.parseBoolean(systemStatEnable));
            }
        } catch (Exception e) {
            throw new StatPublisherException("Could not load values from registry", e);
        }
        return statConfigurationReadObject;
    }

    /**
     * Read the resource from registry
     *
     * @param propertyName - get resource name
     * @param registry     - load registry
     */
    public String getConfigurationProperty(String propertyName, Registry registry)
            throws RegistryException, StatPublisherException {
        String resourcePath = StatConfigurationConstants.MEDIATION_STATISTICS_REG_PATH + propertyName;
        String value = null;
        if (registry != null) {
            try {
                if (registry.resourceExists(resourcePath)) {
                    Resource resource = registry.get(resourcePath);
                    value = resource.getProperty(propertyName);
                }
            } catch (Exception e) {
                throw new StatPublisherException("Error while accessing registry", e);
            }
        }
        return value;
    }

}


