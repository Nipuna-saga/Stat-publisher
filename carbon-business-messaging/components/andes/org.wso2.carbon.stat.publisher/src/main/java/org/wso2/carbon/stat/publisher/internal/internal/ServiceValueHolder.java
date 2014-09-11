package org.wso2.carbon.stat.publisher.internal.internal;

import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.utils.ConfigurationContextService;

public class ServiceValueHolder {
    private ConfigurationContextService configurationContextService;

    private static ServiceValueHolder instance = new ServiceValueHolder();

    public static ServiceValueHolder getInstance() {
        return instance;
    }

    public ConfigurationContextService getConfigurationContextService() {
        return configurationContextService;
    }
    public void registerConfigurationContextService(
            ConfigurationContextService configurationContextService) {
        this.configurationContextService = configurationContextService;
    }
}
