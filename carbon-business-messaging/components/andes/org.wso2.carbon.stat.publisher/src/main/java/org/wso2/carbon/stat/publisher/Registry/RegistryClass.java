package org.wso2.carbon.stat.publisher.Registry;

import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.stat.publisher.conf.StatPublisherConfiguration;
import org.wso2.carbon.stat.publisher.exception.StatPublisherConfigurationException;
import org.wso2.carbon.stat.publisher.internal.ds.ServiceValueHolder;
import org.wso2.carbon.stat.publisher.util.StatPublisherConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class RegistryClass {
    /**
     * Updates the Registry with given configuration data.
     * @param statPublisherConfigurationWriteObject - eventing configuration data
     * @param tenantId                     - get tenantID
     */
    public void storeConfigurationData(StatPublisherConfiguration statPublisherConfigurationWriteObject, int tenantId)
            throws StatPublisherConfigurationException {
        try {
            Registry registry = ServiceValueHolder.getInstance().getRegistryService().getConfigSystemRegistry(tenantId);
            if (statPublisherConfigurationWriteObject.isEnableStatPublisher()) {
                String resourcePath = StatPublisherConstants.MEDIATION_STATISTICS_REG_PATH + StatPublisherConstants.RESOURCE;
                Property[] propertiesDTO = statPublisherConfigurationWriteObject.getProperties();
                if (propertiesDTO != null) {
                    Properties properties = new Properties();
                    for(int i = 0; i < propertiesDTO.length; i++) {
                        Property property = propertiesDTO[i];
                        List<String> valueList = new ArrayList<String>();
                        valueList.add(property.getValue());
                        properties.put(property.getKey(), valueList);
                    }
                    if (registry != null) {
                        // Always creating a new resource because properties should be replaced and overridden
                        Resource resource = registry.newResource();
                        resource.setProperties(properties);
                        registry.put(resourcePath, resource);
                    }
                }
            }
        } catch (RegistryException e) {
            throw new StatPublisherConfigurationException("Could not update registry", e);
        }
    }

    /**
     * Load configuration from Registry.
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

            Properties properties = null;
            Properties filterProperties = null;
            if (registry.resourceExists(resourcePath)) {
                Resource resource = registry.get(resourcePath);
                properties = resource.getProperties();
                if (properties != null) {
                    filterProperties = new Properties();
                    for (Map.Entry<Object, Object> keyValuePair : properties.entrySet()) {
                        //When using mounted registry it keeps some properties starting with "registry." we don't need it.
                        if (!keyValuePair.getKey().toString().startsWith(StatPublisherConstants.PREFIX_FOR_REGISTRY_HIDDEN_PROPERTIES)) {
                            filterProperties.put(keyValuePair.getKey(), keyValuePair.getValue());
                        }
                    }
                }
            }
            if (filterProperties != null) {
                List<Property> propertyDTOList = new ArrayList<Property>();
                String[] keys = filterProperties.keySet().toArray(new String[filterProperties.size()]);
                for (int i = keys.length - 1; i >= 0; i--) {
                    String key = keys[i];
                    Property propertyDTO = new Property();
                    propertyDTO.setKey(key);
                    propertyDTO.setValue(((List<String>) filterProperties.get(key)).get(0));
                    propertyDTOList.add(propertyDTO);
                }
                statPublisherConfigurationReadObject.setProperties(propertyDTOList.toArray(new Property[propertyDTOList.size()]));
            }
            } catch (RegistryException e) {
            throw new StatPublisherConfigurationException("Could not load values from registry", e);
        }
        return statPublisherConfigurationReadObject;
    }


}

