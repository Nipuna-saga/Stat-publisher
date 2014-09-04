package org.wso2.carbon.stat.publisher.internal.publisher;

import org.wso2.andes.kernel.AndesAckData;
import org.wso2.andes.kernel.AndesMessageMetadata;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.stat.publisher.internal.DTO.StatConfigurationDTO;
import org.wso2.carbon.stat.publisher.internal.data.StatConfiguration;
import org.wso2.carbon.stat.publisher.internal.util.URLOperations;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by nipuna on 8/18/14.
 */
public class PublisherObserver {

    public static Timer timer ;
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


        TimerTask taskPublishStat = new TimerTask() {


            @Override

            public void run() {
                try {
                    PrivilegedCarbonContext.startTenantFlow();
                    PrivilegedCarbonContext.getThreadLocalCarbonContext().setTenantId(tenantID,true);

<<<<<<< HEAD


                statConfigurationInstance = statConfigurationDTOObject.ReadRegistry(tenantID); //get statConfiguration Instance according to tenant ID

                    System.out.println("timer task activated" + tenantID);
=======
                //    statConfigurationDTOObject = new StatConfigurationDTO();

                statConfigurationInstance = statConfigurationDTOObject.ReadRegistry(tenantID); //get statConfiguration Instance according to tenant ID


                //if (statConfigurationInstance.isEnableStatPublisher()) { //check Stat publisher Enable

                    dataAgentInstance = DataAgent.getObjectDataAgent();
                //todo uncomment this line
//String URL = statConfigurationInstance.getURL();
>>>>>>> 26a9d8ce475da9ac4763a33d4c4965ad6246c012

//todo remove this line
                String URLList = "tcp://localhost:7611";

                URLOperations urlOperations = new URLOperations();
                String URLArray[] = urlOperations.URLSplitter(URLList);
                String[] credentials={"admin","admin"};

<<<<<<< HEAD
                //    dataAgentInstance = DataAgent.getObjectDataAgent();
=======
                for(String URL : URLArray) {
>>>>>>> 26a9d8ce475da9ac4763a33d4c4965ad6246c012

                    //if (statConfigurationInstance.isSystem_statEnable()) {//check system stat enable configuration

                    //System.out.println("System stat Publishing activated" + tenantID);

                    System.out.println(URL);
                    dataAgentInstance.sendSystemStats(URL,credentials);

                    //}
                    //if (statConfigurationInstance.isMB_statEnable()) {//check MB stat enable configuration

                   // System.out.println("MB stat Publishing activated" + tenantID);

                    //   dataAgentInstance.sendMBStatistics(statConfigurationInstance);
                    // }
                }


<<<<<<< HEAD
                }
                } catch (Exception e) {
                    //  log.error("failed to update statics from BAM publisher", e);
                } finally {
                    PrivilegedCarbonContext.endTenantFlow();
                }   }


=======
                //}
            }
>>>>>>> 26a9d8ce475da9ac4763a33d4c4965ad6246c012
        };

        timer = new Timer();

        // scheduling the task at fixed rate
        timer.scheduleAtFixedRate(taskPublishStat, new Date(), timeInterval);
    }

    //method to publish message statistics
    public void messageStatPublisherTask(AndesMessageMetadata message) {

<<<<<<< HEAD

       if (statConfigurationInstance.isEnableStatPublisher()) { //check Stat publisher Enable

           if (statConfigurationInstance.isMessage_statEnable()) { //check message stat enable configuration
=======
//todo enable if
       // if (statConfigurationInstance.isEnableStatPublisher()) { //check Stat publisher Enable
//todo enable if
           // if (statConfigurationInstance.isMessage_statEnable()) { //check message stat enable configuration
>>>>>>> 26a9d8ce475da9ac4763a33d4c4965ad6246c012

                System.out.println("Message stat Publishing activated" + tenantID + message.getDestination());

                //   dataAgentInstance=DataAgent.getObjectDataAgent();
                //   dataAgentInstance.sendMessageStatistics(statConfigurationInstance,message);
<<<<<<< HEAD
                PublisherObserver publisherObserverInstance = new PublisherObserver();
=======
        //todo move this to activator method
               PublisherObserver publisherObserverInstance = new PublisherObserver();
>>>>>>> 26a9d8ce475da9ac4763a33d4c4965ad6246c012
                publisherObserverInstance.statPublisherTimerTask();


<<<<<<< HEAD
       }
=======
>>>>>>> 26a9d8ce475da9ac4763a33d4c4965ad6246c012

           // }

//        }


    }


    //method to publish message statistics
    public void messageAckStatPublisherTask(AndesAckData ack) {


        if (statConfigurationInstance.isEnableStatPublisher()) { //check Stat publisher Enable

            if (statConfigurationInstance.isMessage_statEnable()) { //check message stat enable configuration

                System.out.println("Message stat Ack Publishing activated" + tenantID + ack.qName);


            }
<<<<<<< HEAD
        }
    }
=======
>>>>>>> 26a9d8ce475da9ac4763a33d4c4965ad6246c012

        }
    }
}
