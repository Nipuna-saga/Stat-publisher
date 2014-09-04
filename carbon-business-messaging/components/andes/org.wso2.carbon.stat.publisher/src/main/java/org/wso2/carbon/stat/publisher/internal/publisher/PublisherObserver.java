package org.wso2.carbon.stat.publisher.internal.publisher;

import org.wso2.andes.kernel.AndesAckData;
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
    DataAgent dataAgentInstance;
    int tenantID;
    private long timeInterval = 5000; //time interval for scheduled task


    public PublisherObserver() {
        tenantID = CarbonContext.getThreadLocalCarbonContext().getTenantId();//get tenant ID
        statConfigurationDTOObject = new StatConfigurationDTO();

        statConfigurationInstance = statConfigurationDTOObject.ReadRegistry(tenantID);

    }


    //Timer task to publish system and MB stats
    public void statPublisherTimerTask() {
        Timer timer;

        TimerTask taskPublishStat = new TimerTask() {


            @Override

            public void run() {

            //    statConfigurationDTOObject = new StatConfigurationDTO();

            statConfigurationInstance = statConfigurationDTOObject.ReadRegistry(tenantID); //get statConfiguration Instance according to tenant ID





                if (statConfigurationInstance.isEnableStatPublisher()) { //check Stat publisher Enable

                    dataAgentInstance = DataAgent.getObjectDataAgent();

                    if (statConfigurationInstance.isSystem_statEnable()) {//check system stat enable configuration

                        System.out.println("System stat Publishing activated" + tenantID);


                        //   dataAgentInstance.sendSystemStats(statConfigurationInstance);

                    }
                    if (statConfigurationInstance.isMB_statEnable()) {//check MB stat enable configuration

                        System.out.println("MB stat Publishing activated" + tenantID);

                        //   dataAgentInstance.sendMBStatistics(statConfigurationInstance);
                    }

                }
           }
        };

         timer = new Timer();

        // scheduling the task at fixed rate
        timer.scheduleAtFixedRate(taskPublishStat, new Date(), timeInterval);
    }

    //method to publish message statistics
    public void messageStatPublisherTask(AndesMessageMetadata message) {


        if (statConfigurationInstance.isEnableStatPublisher()) { //check Stat publisher Enable

            if (statConfigurationInstance.isMessage_statEnable()) { //check message stat enable configuration

                System.out.println("Message stat Publishing activated" + tenantID + message.getDestination());

                //   dataAgentInstance=DataAgent.getObjectDataAgent();
                //   dataAgentInstance.sendMessageStatistics(statConfigurationInstance,message);
             PublisherObserver publisherObserverInstance = new PublisherObserver();
             publisherObserverInstance.statPublisherTimerTask();

            }

        }


    }

   

<<<<<<< HEAD
=======
    //method to publish message statistics
    public void messageAckStatPublisherTask(AndesAckData ack) {


        if (statConfigurationInstance.isEnableStatPublisher()) { //check Stat publisher Enable

            if (statConfigurationInstance.isMessage_statEnable()) { //check message stat enable configuration

                System.out.println("Message stat Ack Publishing activated" + tenantID + ack.qName);
>>>>>>> d439d0cc58010b1904f5854616d80c5360742a1a




}
