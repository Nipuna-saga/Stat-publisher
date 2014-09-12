package org.wso2.carbon.stat.publisher;

import org.apache.log4j.Logger;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.stat.publisher.internal.DTO.StatConfigurationDTO;
import org.wso2.carbon.stat.publisher.internal.data.StatConfiguration;
import org.wso2.carbon.stat.publisher.internal.publisher.PublisherObserver;
import org.wso2.carbon.stat.publisher.internal.util.StatPublisherException;
import org.wso2.carbon.stat.publisher.internal.util.URLOperations;

public class StatPublisherService {

    private static Logger logger = Logger.getLogger(StatPublisherService.class);
    private StatConfigurationDTO StatConfigurationDTOObject;

    //StatConfiguration details get method
    public StatConfiguration getStatConfiguration() throws StatPublisherException {
        int tenantID = CarbonContext.getThreadLocalCarbonContext().getTenantId();//get tenant ID

        StatConfigurationDTOObject = new StatConfigurationDTO();

        return StatConfigurationDTOObject.LoadConfigurationData(tenantID);

    }


    //StatConfiguration details set method
    public boolean setStatConfiguration(StatConfiguration StatConfigurationData) {
        int tenantID = CarbonContext.getThreadLocalCarbonContext().getTenantId();//get tenant ID
        StatConfigurationDTOObject = new StatConfigurationDTO();

        PublisherObserver.statConfigurationInstance = StatConfigurationData;


        if ((StatConfigurationData.isSystem_statEnable() || StatConfigurationData.isMB_statEnable()) && StatConfigurationData.isEnableStatPublisher()) {

            if (!PublisherObserver.timerFlag) {

                logger.info("==================Stat Publishing Activated==================");

                PublisherObserver publisherObserverInstance = new PublisherObserver();
                publisherObserverInstance.statPublisherTimerTask();
                PublisherObserver.timerFlag = true;
            }

        } else {


            if (PublisherObserver.timerFlag) {
                PublisherObserver.timer.cancel();

                PublisherObserver.timerFlag = false;
                logger.info("==================Stat Publishing Deactivated==================");
            }
        }

        try {
            StatConfigurationDTOObject.storeConfigurationData(StatConfigurationData, tenantID);
        } catch (StatPublisherException e) {
            e.printStackTrace();
        }

        return true;
    }

    //util validation method
    public boolean URLValidator(String URLS) {

        URLOperations URLValidatorObject = new URLOperations();
        return URLValidatorObject.URLValidator(URLS);


    }
}
