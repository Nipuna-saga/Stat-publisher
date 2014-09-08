package org.wso2.carbon.stat.publisher.internal.publisher;

import org.wso2.andes.kernel.AndesAckData;
import org.wso2.andes.kernel.AndesMessageMetadata;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.stat.publisher.internal.data.StatConfiguration;
import org.wso2.carbon.stat.publisher.internal.util.URLOperations;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class PublisherObserver {

    public static Timer timer;
    public static boolean timerFlag = true;


    public static StatConfiguration statConfigurationInstance;


    private DataAgent dataAgentInstance;
    private int tenantID;
    private long timeInterval = 5000; //time interval for scheduled task


//

    public PublisherObserver() {
        tenantID = CarbonContext.getThreadLocalCarbonContext().getTenantId();//get tenant ID


    }


    ////Timer task to publish system and MB stats
    public void statPublisherTimerTask() {
        TimerTask taskPublishStat = new TimerTask() {
            @Override
            public void run() {
                try {
                    PrivilegedCarbonContext.startTenantFlow();
                    PrivilegedCarbonContext.getThreadLocalCarbonContext().setTenantId(tenantID, true);
                    // statConfigurationInstance = statConfigurationDTOObject.ReadRegistry(tenantID); //get statConfiguration Instance according to tenant ID

                    if (statConfigurationInstance.isEnableStatPublisher()) { //check Stat publisher Enable

                        dataAgentInstance = DataAgent.getObjectDataAgent();


                        String URLList = statConfigurationInstance.getURL();
                        //todo remove this line

                        // String URLList = "tcp://localhost:7611";
                        URLOperations urlOperations = new URLOperations();
                        String URLArray[] = urlOperations.URLSplitter(URLList);
                        String[] credentials = {"admin", "admin"};
                        for (String URL : URLArray) {
                            if (statConfigurationInstance.isSystem_statEnable()) {//check system stat enable configuration
                                System.out.println("System stat Publishing activated " + tenantID);

                                dataAgentInstance.sendSystemStats(URL, credentials);
                            }
                            if (statConfigurationInstance.isMB_statEnable()) {//check MB stat enable configuration
                                System.out.println("MB stat Publishing activated " + tenantID);
                                dataAgentInstance.sendMBStatistics(URL, credentials);
                            }
                        }
                    }


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
    public void messageStatPublisherTask(AndesMessageMetadata message,int subscribers) {


        if (statConfigurationInstance.isEnableStatPublisher()) { //check Stat publisher Enable

            if (statConfigurationInstance.isMessage_statEnable()) { //check message stat enable configuration

                System.out.println("Message stat Publishing activated" + tenantID + message.getDestination());

                if (statConfigurationInstance.isEnableStatPublisher()) { //check Stat publisher Enable

                    if (statConfigurationInstance.isMessage_statEnable()) { //check message stat enable configuration

                        //   dataAgentInstance=DataAgent.getObjectDataAgent();
                        //   dataAgentInstance.sendMessageStatistics(statConfigurationInstance,message);

                        String URLList = statConfigurationInstance.getURL();

                        URLOperations urlOperations = new URLOperations();
                        String URLArray[] = urlOperations.URLSplitter(URLList);
                        String[] credentials = {statConfigurationInstance.getUsername(), statConfigurationInstance.getPassword()};

                        for (String URL : URLArray) {


                            dataAgentInstance = DataAgent.getObjectDataAgent();
                            dataAgentInstance.sendMessageStatistics(URL,credentials,message,subscribers);

                        }

                    }

                }
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

