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

package org.wso2.carbon.stat.publisher.internal.publisher;

import org.wso2.andes.kernel.AndesAckData;
import org.wso2.andes.kernel.AndesMessageMetadata;
import org.wso2.andes.kernel.StatPublisherMessageListener;
import org.wso2.carbon.stat.publisher.conf.MessageStatistic;
import org.wso2.carbon.stat.publisher.conf.StreamConfiguration;
import org.wso2.carbon.stat.publisher.exception.StatPublisherConfigurationException;
import org.wso2.carbon.stat.publisher.exception.StatPublisherRuntimeException;
import org.wso2.carbon.stat.publisher.internal.ds.StatPublisherValueHolder;
import org.wso2.carbon.stat.publisher.internal.util.XMLConfigurationReader;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class will handle message stat publishing all events
 * messages and Ack messages hold in one queue in processing part they identify using boolean value of message variable
 */

public class StatPublisherMessageListenerImpl implements StatPublisherMessageListener {

    //This is the Queue that use to hold message details//TODO get values from xml
    private static final int NumberOfQueueSlots = 20;

    //Thread
    public static Thread messageStatPublisherThread;

    //TODO use 1 thread
    private static StatPublisherMessageListenerImpl statPublisherMessageListenerImpl = new StatPublisherMessageListenerImpl();
    //This is the Queue that use to hold message details

    public static BlockingQueue<MessageStatistic> messageQueue;
    //MessageStat Object

    private MessageStatistic messageStatistic = new MessageStatistic();
    private StreamConfiguration streamConfiguration;
    private String tenantDomain;



    //private constructor
    private StatPublisherMessageListenerImpl() {
        StreamConfiguration streamConfiguration;
        try {

            streamConfiguration = XMLConfigurationReader.readStreamConfiguration();
        } catch (StatPublisherConfigurationException e) {
            throw new StatPublisherRuntimeException(e);
        }
        int NumberOfQueueSlots = 0;
        try {
            NumberOfQueueSlots = XMLConfigurationReader.readGeneralConfiguration().getNumberOfQueueSlots();
        } catch (StatPublisherConfigurationException e) {
            e.printStackTrace();
        }
        messageQueue = new LinkedBlockingQueue<MessageStatistic>(NumberOfQueueSlots);
        // Start Message stat publisher thread
        messageStatPublisherThread = new Thread(new AsyncMessageStatPublisher(streamConfiguration));
        messageStatPublisherThread.start();


    }

    //get MessageStatPublisher instance
    public static StatPublisherMessageListenerImpl getInstance() {
        return statPublisherMessageListenerImpl;
    }

    /**
     * This method will get all messages that received to MessagingEngine class's messageReceived
     * this method will handle message stat publishing
     */
    @Override
    public void sendMessageDetails(AndesMessageMetadata andesMessageMetadata, int noOfSubscribers) {

//get tenant tenantDomain of message by splitting destination
        if (andesMessageMetadata.getDestination().split("/").length == 1) {
            tenantDomain = "carbon.super";
        } else {
            tenantDomain = andesMessageMetadata.getDestination().split("/")[0];
        }
        //check message's tenant  activate or not message stat Publisher by checking MessageStatEnableMap
        if (StatPublisherValueHolder.getStatPublisherManager().getMessageStatEnableMap().contains(tenantDomain)) {
            //if it's enable add message details to message stat object
            messageStatistic.setAndesMessageMetadata(andesMessageMetadata);
            messageStatistic.setDomain(tenantDomain);
            messageStatistic.setNoOfSubscribers(noOfSubscribers);
            messageStatistic.setMessage(true);
            //add message stat object to message queue
            messageQueue.offer(messageStatistic);
        }
    }

    /**
     * This method will get all Ack messages that received to MessagingEngine class's ackReceived
     * this method will handle Ack message stat publishing
     */
    @Override
    public void sendAckMessageDetails(AndesAckData andesAckData) {
        //get tenant tenantDomain of Ack message by splitting qName
        if (andesAckData.qName.split("/").length == 1) {
            tenantDomain = "carbon.super";
        } else {
            tenantDomain = andesAckData.qName.split("/")[0];
        }
        //check message's tenant  activate or not message stat Publisher by checking MessageStatEnableMap
        if (StatPublisherValueHolder.getStatPublisherManager().getMessageStatEnableMap().contains(tenantDomain)) {
            //if it's enable add message details to message stat object
            messageStatistic.setAndesAckData(andesAckData);
            messageStatistic.setDomain(tenantDomain);
            messageStatistic.setMessage(false);
            //add message stat object to message queue
            messageQueue.offer(messageStatistic);
        }
    }


}
