package org.wso2.carbon.stat.publisher.internal.internal;

import org.apache.log4j.Logger;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.stat.publisher.StatPublisherService;
import org.wso2.carbon.stat.publisher.internal.DTO.StatConfigurationDTO;
import org.wso2.carbon.stat.publisher.internal.data.StatConfiguration;
import org.wso2.carbon.stat.publisher.internal.publisher.PublisherObserver;
import org.wso2.carbon.user.core.UserRealm;
import org.wso2.carbon.user.core.UserStoreException;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.carbon.utils.ConfigurationContextService;


/**
 * @scr.component name="org.wso2.carbon.stat.publisher" immediate="true"
 * @scr.reference name="configurationContext.service"
 * interface="org.wso2.carbon.utils.ConfigurationContextService" cardinality="1..1"
 * policy="dynamic" bind="setConfigurationContextService" unbind="unsetConfigurationContextService"
 * @scr.reference name="org.wso2.carbon.registry.service"
 * interface="org.wso2.carbon.registry.core.service.RegistryService" cardinality="1..1"
 * policy="dynamic" bind="setRegistryService" unbind="unsetRegistryService"
 * @scr.reference name="realm.service" interface="org.wso2.carbon.user.core.service.RealmService"
 * cardinality="1..1" policy="dynamic" bind="setRealmService" unbind="unsetRealmService"
 */

public class StatisticComponent {


    private static Logger logger = Logger.getLogger(StatisticComponent.class);

    public StatConfigurationDTO statConfigurationDTOObject;
    public StatConfiguration statConfigurationInstance;

    private ServiceRegistration statAdminServiceRegistration;
    private RealmService realmService;

    protected void activate(ComponentContext context) {
        try {


            StatPublisherService Service = StatPublisherBuilder.createMediationService();
            context.getBundleContext().registerService(StatPublisherService.class.getName(),
                    Service, null);

            logger.info("Successfully created the stat publisher service");


        } catch (RuntimeException e) {
            logger.error("Can not create stat publisher service ", e);
        }
        statConfigurationDTOObject = new StatConfigurationDTO();


        statConfigurationInstance = statConfigurationDTOObject.ReadRegistry(CarbonContext.getThreadLocalCarbonContext().getTenantId());

        PublisherObserver.statConfigurationInstance = statConfigurationInstance;

        PublisherObserver.timerFlag = false;

        if ((statConfigurationInstance.isSystem_statEnable() || statConfigurationInstance.isMB_statEnable()) && statConfigurationInstance.isEnableStatPublisher()) {

            PublisherObserver publisherObserverInstance = new PublisherObserver();
            publisherObserverInstance.statPublisherTimerTask();
            PublisherObserver.timerFlag = true;


        }
        try {
            StatConfiguration statConfiguration = new StatConfiguration();
            UserRealm realm = realmService.getBootstrapRealm();
            String userName = realm.getRealmConfiguration().getAdminUserName();
            statConfiguration.setAdminUserName(userName);
            String password = realm.getRealmConfiguration().getAdminPassword();
            statConfiguration.setAdminPassword(password);

        } catch (UserStoreException e) {
            logger.error("Error in realmService", e);
        }

    }

    protected void deactivate(ComponentContext context) {
        // unregistered MBStatsPublisherAdmin service from the OSGi Service Register.
        statAdminServiceRegistration.unregister();
        if (logger.isDebugEnabled()) {
            logger.debug("MB statistics publisher bundle is deactivated");
        }
    }

    protected void setConfigurationContextService(
            ConfigurationContextService configurationContextService) {
        ServiceValueHolder.getInstance().registerConfigurationContextService(configurationContextService);
    }

    protected void unsetConfigurationContextService(
            ConfigurationContextService configurationContextService) {

    }

    protected void setRegistryService(RegistryService registryService) {
        try {
            StatConfigurationDTO.setRegistryService(registryService);
        } catch (Exception e) {
            logger.error("Cannot retrieve System Registry", e);
        }
    }

    protected void unsetRegistryService(RegistryService registryService) {
        StatConfigurationDTO.setRegistryService(null);
    }

    protected void setRealmService(RealmService realmService) {
        this.realmService = realmService;
    }

    protected void unsetRealmService(RealmService realmService) {
        if (this.realmService != null) {
            this.realmService = null;
        }
    }

}
