package org.wso2.carbon.stat.publisher.internal.internal;

import org.wso2.carbon.utils.ConfigurationContextService;

public class ServiceValueHolder {
    private static ServiceValueHolder instance = new ServiceValueHolder();
    private ConfigurationContextService configurationContextService;

    public static ServiceValueHolder getInstance() {
        return instance;
    }

    public ConfigurationContextService getConfigurationContextService() {
        return configurationContextService;
    }

    public void setConfigurationContextService(
            ConfigurationContextService configurationContextService) {
        this.configurationContextService = configurationContextService;
    }
}
