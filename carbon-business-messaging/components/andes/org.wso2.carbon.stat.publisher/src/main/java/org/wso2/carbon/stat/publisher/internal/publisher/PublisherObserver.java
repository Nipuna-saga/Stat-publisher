package org.wso2.carbon.stat.publisher.internal.publisher;

import org.apache.log4j.Logger;
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

    private static Logger logger = Logger.getLogger(PublisherObserver.class);

    public static Timer timer;
    public static boolean timerFlag = true;
    public static StatConfiguration statConfigurationInstance;

    private ExecutorService executor;
    private static final int NUMBER_OF_THREADS = 20;


    private DataAgent dataAgentInstance;

    private int tenantID;


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
                        String[] credentials = {statConfigurationInstance.getUsername(), statConfigurationInstance.getPassword()};
                        for (String URL : URLArray) {
                            if (statConfigurationInstance.isSystem_statEnable()) {//check system stat enable configuration


                                dataAgentInstance.sendSystemStats(URL, credentials);
                                //       logger.info("System stat Publishing activated " + Thread.currentThread().getName());

                            }
                            if (statConfigurationInstance.isMB_statEnable()) {//check MB stat enable configuration

                                dataAgentInstance.sendMBStatistics(URL, credentials);

                                //       logger.info("MB stat Publishing activated " + Thread.currentThread().getName());

                            }
                        }


                    }


                } catch (Exception e) {
                    logger.error("failed to update statics from BAM publisher", e);
                } finally {
                    PrivilegedCarbonContext.endTenantFlow();
                }
            }


        };
        timer = new Timer();
           // scheduling the task at fixed rate
        long timeInterval = 5000;
        timer.scheduleAtFixedRate(taskPublishStat, new Date(), timeInterval);
    }


    //method to publish message statistics
    public void messageStatPublisherTask(AndesMessageMetadata message, int subscribers) {


        if (statConfigurationInstance.isEnableStatPublisher()) { //check Stat publisher Enable

            if (statConfigurationInstance.isMessage_statEnable()) { //check message stat enable configuration

                logger.info("Message stat Publishing activated");


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

                // logger.info("Message stat Ack Publishing activated" + tenantID + ack.qName);


                String URLList = statConfigurationInstance.getURL();

                URLOperations urlOperations = new URLOperations();
                String URLArray[] = urlOperations.URLSplitter(URLList);
                String[] credentials = {statConfigurationInstance.getUsername(), statConfigurationInstance.getPassword()};

                for (String URL : URLArray) {

                    logger.info("Message ack stat Publishing activated");
                    dataAgentInstance = DataAgent.getObjectDataAgent();
                    dataAgentInstance.sendACKStatistics(URL, credentials, ack);

                }


            }

        }
    }
/*
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
            System.out.println("MB stat Publishing activated " + Thread.currentThread().getName() +CarbonContext.getThreadLocalCarbonContext().getUsername());
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
*/
}

