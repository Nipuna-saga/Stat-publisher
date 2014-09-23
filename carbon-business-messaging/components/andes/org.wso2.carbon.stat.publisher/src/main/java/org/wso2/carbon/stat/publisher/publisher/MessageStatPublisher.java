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

import org.wso2.andes.kernel.AndesAckData;
import org.wso2.andes.kernel.AndesMessageMetadata;
import org.wso2.andes.kernel.StatPublisherGetMessage;
import org.wso2.carbon.stat.publisher.conf.MessageStat;
import org.wso2.carbon.stat.publisher.conf.StreamConfiguration;
import org.wso2.carbon.stat.publisher.exception.StatPublisherConfigurationException;
import org.wso2.carbon.stat.publisher.internal.ds.ServiceValueHolder;
import org.wso2.carbon.stat.publisher.util.XMLConfigurationReader;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;


//TODO need to remove umberOfMessages ,numberOfAckMessages variables because they use to identify number of messages going to our component

/**
 * This class will handle message stat publishing all events
 * messages and Ack messages hold in one queue in processing part they identify using boolean value of message variable
 */

public class MessageStatPublisher implements StatPublisherGetMessage {


    //This is the Queue that use to hold message details
    private static final int NumberOfQueueSlots = 20;
    private BlockingQueue<MessageStat> messageQueue = new LinkedBlockingQueue<MessageStat>(NumberOfQueueSlots);
    //Thread pool
    private static final int NumberOfThreads = 5;
    private ExecutorService executor = Executors.newFixedThreadPool(NumberOfThreads);
    private static MessageStatPublisher messageStatPublisher = new MessageStatPublisher();
    private static int numberOfMessages = 0;
    private static int numberOfAckMessages = 0;
    MessageStat messageStat = new MessageStat();
    StreamConfiguration streamConfiguration;
    XMLConfigurationReader xmlConfigurationReader;

    private String domain;

    //private constructor
    private MessageStatPublisher() {
        xmlConfigurationReader = new XMLConfigurationReader();
        try {
             streamConfiguration = xmlConfigurationReader.readStreamConfiguration();
        } catch (StatPublisherConfigurationException e) {
            e.printStackTrace();
        }


    }

    //get MessageStatPublisher instance
    public static MessageStatPublisher getInstance() {
        return messageStatPublisher;
    }

    /**
     * This method will get all messages that received to MessagingEngine class's messageReceived
     * this method will handle message stat publishing
     */

    @Override
    public void getMessageDetails(AndesMessageMetadata andesMessageMetadata, int noOfSubscribers) {

//get tenant domain of message by splitting destination
        if (andesMessageMetadata.getDestination().split("/").length == 1) {

            domain = "carbon.super";

        } else {
            domain = andesMessageMetadata.getDestination().split("/")[0];
        }
        //check message's tenant  activate or not message stat Publisher by checking MessageStatEnableMap
        if (ServiceValueHolder.getInstance().getStatPublisherManagerService().getMessageStatEnableMap().contains(domain)) {
            try {
                //if it's enable add message details to message stat object
                messageStat.setAndesMessageMetadata(andesMessageMetadata);
                messageStat.setDomain(domain);
                messageStat.setNoOfSubscribers(noOfSubscribers);
                messageStat.setMessage(true);
                //add message stat object to message queue
                messageQueue.put(messageStat);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //start a thread in from thread pool
        Runnable worker = new AsyncMessageStatPublisher(numberOfMessages,streamConfiguration);
        executor.execute(worker);
        numberOfMessages++;


    }

    /**
     * This method will get all Ack messages that received to MessagingEngine class's ackReceived
     * this method will handle Ack message stat publishing
     */
    @Override
    public void getAckMessageDetails(AndesAckData andesAckData) {

        //get tenant domain of Ack message by splitting qName

        if (andesAckData.qName.split("/").length == 1) {

            domain = "carbon.super";

        } else {
            domain = andesAckData.qName.split("/")[0];
        }
        //check message's tenant  activate or not message stat Publisher by checking MessageStatEnableMap
        if (ServiceValueHolder.getInstance().getStatPublisherManagerService().getMessageStatEnableMap().contains(domain)) {
            try {
                //if it's enable add message details to message stat object
                messageStat.setAndesAckData(andesAckData);
                messageStat.setDomain(domain);
                messageStat.setMessage(false);
                //add message stat object to message queue
                messageQueue.put(messageStat);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //start thread in from thread pool
        Runnable worker = new AsyncMessageStatPublisher(numberOfAckMessages,streamConfiguration);
        executor.execute(worker);
        numberOfAckMessages++;


    }

    public BlockingQueue<MessageStat> getMessageQueue() {
        return messageQueue;
    }


}
