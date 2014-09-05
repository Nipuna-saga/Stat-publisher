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

    public static Timer timer;
    public static boolean timerFlag = true;
    StatConfigurationDTO statConfigurationDTOObject;
    StatConfiguration statConfigurationInstance;
    DataAgent dataAgentInstance;
    int tenantID;
    private long timeInterval = 20000; //time interval for scheduled task
//

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
                    PrivilegedCarbonContext.getThreadLocalCarbonContext().setTenantId(tenantID, true);
                    statConfigurationInstance = statConfigurationDTOObject.ReadRegistry(tenantID); //get statConfiguration Instance according to tenant ID

                    if (statConfigurationInstance.isEnableStatPublisher()) { //check Stat publisher Enable
                        System.out.println(" stat Publishing activated");
                        dataAgentInstance = DataAgent.getObjectDataAgent();
//todo uncomment this line


                        //   if (statConfigurationInstance.isEnableStatPublisher()) { //check Stat publisher Enable


                        System.out.println(" stat Publishing activated");
                        dataAgentInstance = DataAgent.getObjectDataAgent();
                        //todo uncomment this line

//String URL = statConfigurationInstance.getURL();
//todo remove this line

                        String URLList = "tcp://localhost:7611";
                        URLOperations urlOperations = new URLOperations();
                        String URLArray[] = urlOperations.URLSplitter(URLList);
                        String[] credentials = {"admin", "admin"};
                        for (String URL : URLArray) {
                            if (statConfigurationInstance.isSystem_statEnable()) {//check system stat enable configuration
                                System.out.println("System stat Publishing activated" + tenantID);
//System.out.println(URL);
                                dataAgentInstance.sendSystemStats(URL, credentials);
                            }
                            if (statConfigurationInstance.isMB_statEnable()) {//check MB stat enable configuration
                                System.out.println("MB stat Publishing activated" + tenantID);
                                dataAgentInstance.sendMBStatistics(URL, credentials);
                            }
                        }
                    }

                    String URLList = "tcp://localhost:7611";

                    URLOperations urlOperations = new URLOperations();
                    String URLArray[] = urlOperations.URLSplitter(URLList);
                    String[] credentials = {"admin", "admin"};

                    for (String URL : URLArray) {

                        //   if (statConfigurationInstance.isSystem_statEnable()) {//check system stat enable configuration

                        System.out.println("System stat Publishing activated" + tenantID);

                        //System.out.println(URL);
                        dataAgentInstance.sendSystemStats(URL, credentials);

                        //  }
                        // if (statConfigurationInstance.isMB_statEnable()) {//check MB stat enable configuration

                        System.out.println("MB stat Publishing activated" + tenantID);

                        dataAgentInstance.sendMBStatistics(URL, credentials);
                        // }
                    }


                    //  }


                } catch (Exception e) {
// log.error("failed to update statics from BAM publisher", e);
                } finally {
                    PrivilegedCarbonContext.endTenantFlow();
                }
            }
        };
        timer = new Timer();
// scheduling the task at fixed rate
        timer.scheduleAtFixedRate(taskPublishStat, new Date(), timeInterval);
    }


    //method to publish message statistics
    public void messageStatPublisherTask(AndesMessageMetadata message) {


//todo enable if
        // if (statConfigurationInstance.isEnableStatPublisher()) { //check Stat publisher Enable
//todo enable if
        // if (statConfigurationInstance.isMessage_statEnable()) { //check message stat enable configuration

        System.out.println("Message stat Publishing activated" + tenantID + message.getDestination());

        if (statConfigurationInstance.isEnableStatPublisher()) { //check Stat publisher Enable

            if (statConfigurationInstance.isMessage_statEnable()) { //check message stat enable configuration

                //   dataAgentInstance=DataAgent.getObjectDataAgent();
                //   dataAgentInstance.sendMessageStatistics(statConfigurationInstance,message);
                //todo move this to activator method


                //   dataAgentInstance=DataAgent.getObjectDataAgent();
                //   dataAgentInstance.sendMessageStatistics(statConfigurationInstance,message);
                //todo move this to activator method
                PublisherObserver publisherObserverInstance = new PublisherObserver();
                publisherObserverInstance.statPublisherTimerTask();


            }

        }
    }

    //method to publish message statistics
    public void messageAckStatPublisherTask(AndesAckData ack) {

        if (statConfigurationInstance.isEnableStatPublisher()) { //check Stat publisher Enable

            if (statConfigurationInstance.isMessage_statEnable()) { //check message stat enable configuration

                System.out.println("Message stat Ack Publishing activated" + tenantID + ack.qName);


            }

        }
    }


}

