package org.wso2.carbon.stat.publisher.internal.DTO;

/**
 * Created by dilshani on 8/20/14.
 */

import org.wso2.carbon.core.RegistryResources;

public class Constants {
    public static final String ENABLE_STAT_PUBLISHER = "enableStatPublisher";
    public static final String USER_NAME = "username";
    public static final String PASSWORD = "password";
    public static final String URL = "url";
    public static final String MB_STAT_ENABLE = "mbStatEnable";
    public static final String MESSAGE_STAT_ENABLE = "messageStatEnable";
    public static final String SYSTEM_STAT_ENABLE = "systemStatEnable";
    public static final String PREFIX_FOR_REGISTRY_HIDDEN_PROPERTIES="registry.";

    public static final String STATISTICS_PROPERTIES_REG_PATH = RegistryResources.COMPONENTS
            + "org.wso2.carbon.stat.publisher/NewMBStat/properties";
    public static final String MEDIATION_STATISTICS_REG_PATH = RegistryResources.COMPONENTS
            + "org.wso2.carbon.stat.publisher/NewMBStat/stat";
}