package org.wso2.carbon.stat.publisher.internal.DTO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.stat.publisher.internal.data.StatConfiguration;
import org.wso2.carbon.stat.publisher.internal.util.PublisherException;

/**
 * Created by dilshani on 8/28/14.
 */
public class StatConfigurationDTO {

    public static final String EMPTY_STRING = "";
    private static Log log = LogFactory.getLog(StatConfigurationDTO.class);
    private static RegistryService registryService;

    public static void setRegistryService(RegistryService registryServiceParam) {
        registryService = registryServiceParam;
    }

    /**
     * Updates the Registry with given config data.
     *
     * @param statConfigurationWriteObject eventing configuration data
     * @param tenantId                     get tenantID
     */
    public void WriteRegistry(StatConfiguration statConfigurationWriteObject, int tenantId) {
        try {
            Registry registry = registryService.getConfigSystemRegistry(tenantId);

            if (statConfigurationWriteObject.isEnableStatPublisher()) {

                updateConfigProperty(org.wso2.carbon.stat.publisher.internal.DTO.Constants.ENABLE_STAT_PUBLISHER,
                        statConfigurationWriteObject.isEnableStatPublisher(), registry);
                updateConfigProperty(org.wso2.carbon.stat.publisher.internal.DTO.Constants.USER_NAME,
                        statConfigurationWriteObject.getUsername(), registry);
                updateConfigProperty(org.wso2.carbon.stat.publisher.internal.DTO.Constants.PASSWORD,
                        statConfigurationWriteObject.getPassword(), registry);
                updateConfigProperty(org.wso2.carbon.stat.publisher.internal.DTO.Constants.URL, statConfigurationWriteObject.getURL(), registry);
                updateConfigProperty(org.wso2.carbon.stat.publisher.internal.DTO.Constants.MB_STAT_ENABLE,
                        statConfigurationWriteObject.isMB_statEnable(), registry);
                updateConfigProperty(org.wso2.carbon.stat.publisher.internal.DTO.Constants.MESSAGE_STAT_ENABLE,
                        statConfigurationWriteObject.isMessage_statEnable(), registry);
                updateConfigProperty(org.wso2.carbon.stat.publisher.internal.DTO.Constants.SYSTEM_STAT_ENABLE,
                        statConfigurationWriteObject.isSystem_statEnable(), registry);
            } else {
                updateConfigProperty(org.wso2.carbon.stat.publisher.internal.DTO.Constants.ENABLE_STAT_PUBLISHER, false, registry);
                updateConfigProperty(org.wso2.carbon.stat.publisher.internal.DTO.Constants.USER_NAME,"", registry);
                updateConfigProperty(org.wso2.carbon.stat.publisher.internal.DTO.Constants.PASSWORD,"", registry);
                updateConfigProperty(org.wso2.carbon.stat.publisher.internal.DTO.Constants.URL,"", registry);
                updateConfigProperty(org.wso2.carbon.stat.publisher.internal.DTO.Constants.MB_STAT_ENABLE,false, registry);
                updateConfigProperty(org.wso2.carbon.stat.publisher.internal.DTO.Constants.MESSAGE_STAT_ENABLE,false, registry);
                updateConfigProperty(org.wso2.carbon.stat.publisher.internal.DTO.Constants.SYSTEM_STAT_ENABLE,false, registry);

            }
        } catch (Exception e) {
            log.error("Could not update the registry", e);
        }
    }

    public void updateConfigProperty(String propertyName, Object value, Registry registry)
            throws RegistryException, PublisherException {
        String resourcePath = org.wso2.carbon.stat.publisher.internal.DTO.Constants.MEDIATION_STATISTICS_REG_PATH + propertyName;
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
                throw new PublisherException("Error while accessing registry", e);
            }
        }
    }

    public StatConfiguration ReadRegistry(int tenantId) {

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

            String enableStatPublisher = getConfigurationProperty(
                    org.wso2.carbon.stat.publisher.internal.DTO.Constants.ENABLE_STAT_PUBLISHER, registry);
            String userName = getConfigurationProperty(org.wso2.carbon.stat.publisher.internal.DTO.Constants.USER_NAME,
                    registry);
            String password = getConfigurationProperty(org.wso2.carbon.stat.publisher.internal.DTO.Constants.PASSWORD,
                    registry);
            String url = getConfigurationProperty(org.wso2.carbon.stat.publisher.internal.DTO.Constants.URL,
                    registry);
            String mbStatEnable = getConfigurationProperty(org.wso2.carbon.stat.publisher.internal.DTO.Constants.MB_STAT_ENABLE,
                    registry);
            String messageStatEnable = getConfigurationProperty(org.wso2.carbon.stat.publisher.internal.DTO.Constants.MESSAGE_STAT_ENABLE,
                    registry);
            String systemStatEnable = getConfigurationProperty(org.wso2.carbon.stat.publisher.internal.DTO.Constants.SYSTEM_STAT_ENABLE,
                    registry);


            Boolean isEnableStatPublisher = Boolean.parseBoolean(enableStatPublisher);

            if (isEnableStatPublisher && url != null && userName != null && password != null) {

                statConfigurationReadObject.setEnableStatPublisher(Boolean.parseBoolean(enableStatPublisher));
                statConfigurationReadObject.setURL(url);
                statConfigurationReadObject.setUsername(userName);
                statConfigurationReadObject.setPassword(password);

                statConfigurationReadObject.setMB_statEnable(Boolean.parseBoolean(mbStatEnable));
                statConfigurationReadObject.setMessage_statEnable(Boolean.parseBoolean(messageStatEnable));
                statConfigurationReadObject.setSystem_statEnable(Boolean.parseBoolean(systemStatEnable));

            }

//            else {
//                // Registry does not have eventing config
//                update(statConfigurationReadObject, tenantId);
//            }
        } catch (Exception e) {
            log.error("Could not load values from registry", e);
        }
        return statConfigurationReadObject;
    }

    public String getConfigurationProperty(String propertyName, Registry registry)
            throws RegistryException, PublisherException {
        String resourcePath = org.wso2.carbon.stat.publisher.internal.DTO.Constants.MEDIATION_STATISTICS_REG_PATH + propertyName;
        String value = null;
        if (registry != null) {
            try {
                if (registry.resourceExists(resourcePath)) {
                    Resource resource = registry.get(resourcePath);
                    value = resource.getProperty(propertyName);
                }
            } catch (Exception e) {
                throw new PublisherException("Error while accessing registry", e);
            }
        }
        return value;
    }

}


