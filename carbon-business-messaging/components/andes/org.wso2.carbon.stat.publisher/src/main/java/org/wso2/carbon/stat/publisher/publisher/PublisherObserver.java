/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.wso2.carbon.stat.publisher.publisher;

import org.apache.log4j.Logger;
import org.wso2.andes.kernel.AndesAckData;
import org.wso2.andes.kernel.AndesMessageMetadata;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.stat.publisher.conf.StatConfiguration;
import org.wso2.carbon.stat.publisher.exception.StatPublisherConfigurationException;
import org.wso2.carbon.stat.publisher.util.URLOperations;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class PublisherObserver {

    //TODO rename StatisticObserver

    public static Timer timer;
    public static boolean timerFlag = true;
    public static StatConfiguration statConfigurationInstance;

    private static Logger logger = Logger.getLogger(PublisherObserver.class);
    private DataAgent_old dataAgentInstance;
    private int tenantID;

    //TODO startMonitoring method
    //TODO StatisticManager class


    public PublisherObserver() {
        tenantID = CarbonContext.getThreadLocalCarbonContext().getTenantId();//get tenant ID
    }

    ////Timer task to publish system and MB stats
    public void statPublisherTimerTask() {


        TimerTask taskPublishStat = new TimerTask() {
            @Override
            public void run() {
                try {
                    //set tenant ID to Carbon context in thread
                    PrivilegedCarbonContext.startTenantFlow();
                    PrivilegedCarbonContext.getThreadLocalCarbonContext().setTenantId(tenantID, true);


                    if (statConfigurationInstance.isEnableStatPublisher()) { //check Stat publisher Enable

                        dataAgentInstance = DataAgent_old.getObjectDataAgent();


                        String URLList = statConfigurationInstance.getURL();

//todo tenant loading  update when change tenant conf: not now :) future
                        // String URLList = "tcp://localhost:7611";
                        URLOperations urlOperations = new URLOperations();
                        String URLArray[] = urlOperations.URLSplitter(URLList);
                        String[] credentials =
                                {statConfigurationInstance.getUsername(), statConfigurationInstance.getPassword()};
                        for (String URL : URLArray) {
                            if (statConfigurationInstance.isSystem_statEnable()) {
                                //check system stat enable configuration


                                dataAgentInstance.sendSystemStats(URL, credentials);
                                //logger.info("System stat Publishing activated " + Thread.currentThread().getName());

                            }
                            if (statConfigurationInstance.isMB_statEnable()) {//check MB stat enable configuration

                                dataAgentInstance.sendMBStatistics(URL, credentials);

                                // logger.info("MB stat Publishing activated " + Thread.currentThread().getName());

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
    public void messageStatPublisherTask(AndesMessageMetadata message, int subscribers)
            throws StatPublisherConfigurationException {


        if (statConfigurationInstance.isEnableStatPublisher()) { //check Stat publisher Enable

            if (statConfigurationInstance.isMessage_statEnable()) { //check message stat enable configuration

                logger.info("Message stat Publishing activated");


                String URLList = statConfigurationInstance.getURL();

                URLOperations urlOperations = new URLOperations();
                String URLArray[] = urlOperations.URLSplitter(URLList);
                String[] credentials =
                        {statConfigurationInstance.getUsername(), statConfigurationInstance.getPassword()};

                for (String URL : URLArray) {


                    dataAgentInstance = DataAgent_old.getObjectDataAgent();
                    dataAgentInstance.sendMessageStatistics(URL, credentials, message, subscribers);


                }
            }
        }
    }

    //method to publish message statistics
    public void messageAckStatPublisherTask(AndesAckData ack) throws
                                                              StatPublisherConfigurationException {

        if (statConfigurationInstance.isEnableStatPublisher()) { //check Stat publisher Enable

            if (statConfigurationInstance.isMessage_statEnable()) { //check message stat enable configuration

                String URLList = statConfigurationInstance.getURL();

                URLOperations urlOperations = new URLOperations();
                String URLArray[] = urlOperations.URLSplitter(URLList);
                String[] credentials =
                        {statConfigurationInstance.getUsername(), statConfigurationInstance.getPassword()};

                for (String URL : URLArray) {

                    logger.info("Message ack stat Publishing activated");
                    dataAgentInstance = DataAgent_old.getObjectDataAgent();
                    dataAgentInstance.sendACKStatistics(URL, credentials, ack);

                }


            }

        }
    }

}

