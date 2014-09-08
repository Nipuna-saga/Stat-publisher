package org.wso2.carbon.stat.publisher.internal.internal;

import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.utils.ConfigurationContextService;

public class ServiceValueHolder {
    private RegistryService registryService;
    private ConfigurationContextService configurationContextService;

    private static ServiceValueHolder instance = new ServiceValueHolder();

    public static ServiceValueHolder getInstance() {
        return instance;
    }

    public void registerRegistryService(RegistryService registryService) {
        this.registryService = registryService;
    }

    public RegistryService getRegistryService() {
        return this.registryService;
    }

    public ConfigurationContextService getConfigurationContextService() {
        return configurationContextService;
    }

    public void registerConfigurationContextService(
            ConfigurationContextService configurationContextService) {
        this.configurationContextService = configurationContextService;
    }
}
