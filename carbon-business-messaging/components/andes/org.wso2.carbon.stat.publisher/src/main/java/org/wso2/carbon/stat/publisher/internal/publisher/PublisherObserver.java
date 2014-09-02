package org.wso2.carbon.stat.publisher.internal.publisher;

import org.wso2.andes.kernel.AndesMessageMetadata;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.stat.publisher.internal.DTO.StatConfigurationDTO;
import org.wso2.carbon.stat.publisher.internal.data.StatConfiguration;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by nipuna on 8/18/14.
 */
public class PublisherObserver {


    StatConfigurationDTO statConfigurationDTOObject;
    StatConfiguration statConfigurationInstance;

    int tenantID;
    private long timeInterval = 5000; //time interval for scheduled task


    public PublisherObserver() {
        tenantID = CarbonContext.getThreadLocalCarbonContext().getTenantId();//get tenant ID


    }


    //Timer task to publish system and MB stats
    public void statPublisherTimerTask() {

        TimerTask taskPublishStat = new TimerTask() {


            @Override

            public void run() {

                statConfigurationDTOObject = new StatConfigurationDTO();

                statConfigurationInstance = statConfigurationDTOObject.ReadRegistry(tenantID);
                if (statConfigurationInstance.isEnableStatPublisher()) {

                    if (statConfigurationInstance.isSystem_statEnable()) {

                        System.out.println("System stat Publishing activated" + tenantID);

                    }
                    if (statConfigurationInstance.isMB_statEnable()) {

                        System.out.println("MB stat Publishing activated" + tenantID);

                    }

                }
            }
        };

        Timer timer = new Timer();

        // scheduling the task at fixed rate
        timer.scheduleAtFixedRate(taskPublishStat, new Date(), timeInterval);
    }

    //method to publish message statistics
    public void messageStatPublisherTask(AndesMessageMetadata message) {
        statConfigurationDTOObject = new StatConfigurationDTO();

        statConfigurationInstance = statConfigurationDTOObject.ReadRegistry(tenantID);

        if (statConfigurationInstance.isEnableStatPublisher()) {

            if (statConfigurationInstance.isMessage_statEnable()) {

                System.out.println("Message stat Publishing activated" + tenantID + message.getDestination());

            }

        }


    }

}
