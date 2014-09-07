package org.wso2.carbon.stat.publisher.internal.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.stat.publisher.StatPublisherService;
import org.wso2.carbon.stat.publisher.internal.DTO.StatConfigurationDTO;


import org.wso2.carbon.stat.publisher.internal.data.StatConfiguration;


import org.wso2.carbon.stat.publisher.internal.publisher.PublisherObserver;
import org.wso2.carbon.utils.ConfigurationContextService;

/**
 * Created by dilshani on 9/1/14.
 */

/**
 * @scr.component name="org.wso2.carbon.stat.publisher" immediate="true"
 * @scr.reference name="configurationContext.service"
 * interface="org.wso2.carbon.utils.ConfigurationContextService" cardinality="1..1"
 * policy="dynamic" bind="setConfigurationContextService" unbind="unsetConfigurationContextService"
 * @scr.reference name="org.wso2.carbon.registry.service"
 * interface="org.wso2.carbon.registry.core.service.RegistryService" cardinality="1..1"
 * policy="dynamic" bind="setRegistryService" unbind="unsetRegistryService"
 */

public class StatisticComponent {

    private static final Log log = LogFactory.getLog(StatisticComponent.class);
    private ServiceRegistration statAdminServiceRegistration;
    private StatConfigurationDTO statConfigurationDTOObject;
    private StatConfiguration statConfigurationInstance;


    protected void activate(ComponentContext context) {
        try {
            System.out.println("======================Activating the bundle==================");

            StatPublisherService Service = StatPublisherBuilder.createMediationService();
            context.getBundleContext().registerService(StatPublisherService.class.getName(),
                    Service, null);

            log.info("Successfully created the stat publisher service");
            System.out.println("====================Activated the bundle==================");


        } catch (RuntimeException e) {
            log.error("Can not create stat publisher service ", e);
        }
        statConfigurationDTOObject = new StatConfigurationDTO();


        statConfigurationInstance = statConfigurationDTOObject.ReadRegistry(CarbonContext.getThreadLocalCarbonContext().getTenantId());

        PublisherObserver.statConfigurationInstance = statConfigurationInstance;

        PublisherObserver.timerFlag = false;

        if ((statConfigurationInstance.isSystem_statEnable() || statConfigurationInstance.isMB_statEnable()) && statConfigurationInstance.isEnableStatPublisher()) {

            PublisherObserver publisherObserverInstance = new PublisherObserver();
            publisherObserverInstance.statPublisherTimerTask();
            PublisherObserver.timerFlag = true;
            System.out.println("==================Stat Publishing Activated==================");

        }


    }

    protected void deactivate(ComponentContext context) {
        // unregistered MBStatsPublisherAdmin service from the OSGi Service Register.
        statAdminServiceRegistration.unregister();
        if (log.isDebugEnabled()) {
            log.debug("MB statistics publisher bundle is deactivated");
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
            log.error("Cannot retrieve System Registry", e);
        }
    }

    protected void unsetRegistryService(RegistryService registryService) {
        StatConfigurationDTO.setRegistryService(null);
    }

}
