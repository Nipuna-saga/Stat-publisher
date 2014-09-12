package org.wso2.carbon.stat.publisher.internal.internal;

import org.apache.log4j.Logger;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.stat.publisher.StatPublisherService;
import org.wso2.carbon.stat.publisher.internal.DTO.StatConfigurationDTO;
import org.wso2.carbon.stat.publisher.internal.data.StatConfiguration;
import org.wso2.carbon.stat.publisher.internal.publisher.DataAgent;
import org.wso2.carbon.stat.publisher.internal.publisher.PublisherObserver;
import org.wso2.carbon.stat.publisher.internal.util.StatPublisherException;
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

    protected void activate(ComponentContext context) throws StatPublisherException {

        try {
            StatPublisherService Service = StatPublisherBuilder.createMediationService();
            context.getBundleContext().registerService(StatPublisherService.class.getName(),
                                                       Service, null);
            statConfigurationDTOObject = new StatConfigurationDTO();
            statConfigurationInstance =
                    statConfigurationDTOObject.LoadConfigurationData(CarbonContext.getThreadLocalCarbonContext().getTenantId());
            PublisherObserver.statConfigurationInstance = statConfigurationInstance;
            PublisherObserver.timerFlag = false;

            if ((statConfigurationInstance.isSystem_statEnable() || statConfigurationInstance.isMB_statEnable()) &&
                statConfigurationInstance.isEnableStatPublisher()) {
                PublisherObserver publisherObserverInstance = new PublisherObserver();
                publisherObserverInstance.statPublisherTimerTask();
                PublisherObserver.timerFlag = true;
            }
            logger.info("Successfully created the stat publisher service");
        } catch (RuntimeException e) {
            //TODO log and return
            throw new StatPublisherException("Can not create stat publisher service", e);
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
        ServiceValueHolder.getInstance().setConfigurationContextService(configurationContextService);
    }

    protected void unsetConfigurationContextService(
            ConfigurationContextService configurationContextService) {
        ServiceValueHolder.getInstance().setConfigurationContextService(null);
    }

    protected void setRegistryService(RegistryService registryService)
            throws StatPublisherException {
        try {
            StatConfigurationDTO.setRegistryService(registryService);
        } catch (Exception e) {
            throw new StatPublisherException("Cannot retrieve System Registry", e);
        }
    }

    protected void unsetRegistryService() {
        StatConfigurationDTO.setRegistryService(null);
    }

    protected void setRealmService(RealmService realmService) throws StatPublisherException {
        try {
            DataAgent.setRealmService(realmService);
        } catch (Exception e) {
            throw new StatPublisherException("Cannot retrieve Realm Service", e);
        }
    }

    protected void unsetRealmService() {
        DataAgent.setRealmService(null);
    }

}
