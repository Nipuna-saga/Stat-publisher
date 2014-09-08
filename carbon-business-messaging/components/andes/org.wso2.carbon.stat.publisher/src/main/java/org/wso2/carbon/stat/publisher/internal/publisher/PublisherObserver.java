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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class PublisherObserver {

    public static Timer timer;
    public static boolean timerFlag = true;
    public static StatConfiguration statConfigurationInstance;

    private ExecutorService executor;
    private static final int NUMBER_OF_THREADS = 20;


    private DataAgent dataAgentInstance;

    private int tenantID;
    private long timeInterval = 5000; //time interval for scheduled task


    public PublisherObserver() {
        tenantID = CarbonContext.getThreadLocalCarbonContext().getTenantId();//get tenant ID


    }


    ////Timer task to publish system and MB stats
    public void statPublisherTimerTask() {

        executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);


        TimerTask taskPublishStat = new TimerTask() {
            @Override
            public void run() {
                try {
                    //set tenant ID to Carbon context in thread
                    PrivilegedCarbonContext.startTenantFlow();
                    PrivilegedCarbonContext.getThreadLocalCarbonContext().setTenantId(tenantID, true);


                    if (statConfigurationInstance.isEnableStatPublisher()) { //check Stat publisher Enable

                        dataAgentInstance = DataAgent.getObjectDataAgent();


                        String URLList = statConfigurationInstance.getURL();

                        // String URLList = "tcp://localhost:7611";
                        URLOperations urlOperations = new URLOperations();
                        String URLArray[] = urlOperations.URLSplitter(URLList);
                        String[] credentials = {"admin", "admin"};
                        for (String URL : URLArray) {
                            if (statConfigurationInstance.isSystem_statEnable()) {//check system stat enable configuration


                                //  dataAgentInstance.sendSystemStats(URL, credentials);
                                Runnable worker = new SystemStatPublisher(URL, credentials);
                                executor.execute(worker);

                            }
                            if (statConfigurationInstance.isMB_statEnable()) {//check MB stat enable configuration

                                //   dataAgentInstance.sendMBStatistics(URL, credentials);
                                Runnable worker = new MBStatPublisher(URL, credentials);
                                executor.execute(worker);


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
    public void messageStatPublisherTask(AndesMessageMetadata message, int subscribers) {


        if (statConfigurationInstance.isEnableStatPublisher()) { //check Stat publisher Enable

            if (statConfigurationInstance.isMessage_statEnable()) { //check message stat enable configuration

                System.out.println("Message stat Publishing activated" + tenantID + message.getDestination());


                String URLList = statConfigurationInstance.getURL();

                URLOperations urlOperations = new URLOperations();
                String URLArray[] = urlOperations.URLSplitter(URLList);
                String[] credentials = {statConfigurationInstance.getUsername(), statConfigurationInstance.getPassword()};

                for (String URL : URLArray) {


                    dataAgentInstance = DataAgent.getObjectDataAgent();
                    dataAgentInstance.sendMessageStatistics(URL, credentials, message, subscribers);


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

    //System stat publisher inner class with runnable implementation
    private class SystemStatPublisher implements Runnable {

        private String URL;
        private String[] credentials;

        public SystemStatPublisher(String URL, String[] credentials) {
            this.URL = URL;
            this.credentials = credentials;

        }

        @Override
        public void run() {

            System.out.println("System stat Publishing activated " + Thread.currentThread().getName());
            dataAgentInstance.sendSystemStats(URL, credentials);
        }
    }

    //MB stat publisher inner class with runnable implementation

    public class MBStatPublisher implements Runnable {
        private String URL;
        private String[] credentials;

        public MBStatPublisher(String URL, String[] credentials) {
            this.URL = URL;
            this.credentials = credentials;

        }


        @Override
        public void run() {
            System.out.println("MB stat Publishing activated " + Thread.currentThread().getName() +URL);
            dataAgentInstance.sendMBStatistics(URL, credentials);
        }
    }

    //Message stat publisher inner class with runnable implementation
    public class MessageStatPublisher implements Runnable {

        private AndesMessageMetadata message;
        private int subscribers;

        public MessageStatPublisher(AndesMessageMetadata message, int subscribers) {

            this.message = message;
            this.subscribers = subscribers;

        }


        @Override
        public void run() {

            messageStatPublisherTask(message, subscribers);
        }

        //method to publish message statistics
        private void messageStatPublisherTask(AndesMessageMetadata message, int subscribers) {


            if (statConfigurationInstance.isEnableStatPublisher()) { //check Stat publisher Enable

                if (statConfigurationInstance.isMessage_statEnable()) { //check message stat enable configuration

                    System.out.println("Message stat Publishing activated" + tenantID + message.getDestination() + " " + Thread.currentThread().getName());


                    String URLList = statConfigurationInstance.getURL();

                    URLOperations urlOperations = new URLOperations();
                    String URLArray[] = urlOperations.URLSplitter(URLList);
                    String[] credentials = {statConfigurationInstance.getUsername(), statConfigurationInstance.getPassword()};

                    for (String URL : URLArray) {


                        dataAgentInstance = DataAgent.getObjectDataAgent();
                        dataAgentInstance.sendMessageStatistics(URL, credentials, message, subscribers);


                    }
                }
            }
        }

    }

    //Message Ack stat publisher inner class with runnable implementation

    public class MessageAckStatPublisher implements Runnable {
        private AndesAckData ack;

        public MessageAckStatPublisher(AndesAckData ack) {
            this.ack = ack;

        }


        @Override
        public void run() {
            messageAckStatPublisherTask(ack);
        }

        //method to publish message statistics
        private void messageAckStatPublisherTask(AndesAckData ack) {

            if (statConfigurationInstance.isEnableStatPublisher()) { //check Stat publisher Enable

                if (statConfigurationInstance.isMessage_statEnable()) { //check message stat enable configuration

                    System.out.println("Message stat Ack Publishing activated" + tenantID + ack.qName + " " + Thread.currentThread().getName());


                }

            }
        }


    }

}

