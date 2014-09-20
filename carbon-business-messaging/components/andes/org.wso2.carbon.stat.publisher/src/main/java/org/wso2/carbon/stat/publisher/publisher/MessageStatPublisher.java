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
import org.wso2.andes.kernel.StatPublisherGetMessage;
import org.wso2.carbon.stat.publisher.conf.MessageStat;
import org.wso2.carbon.stat.publisher.internal.ds.ServiceValueHolder;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class will handle message stat publishing all events
 */

public class MessageStatPublisher implements StatPublisherGetMessage {

    private static final Logger LOGGER = Logger.getLogger(StatPublisherObserver.class);
    private static MessageStatPublisher messageStatPublisher = new MessageStatPublisher();
    private StatPublisherManager statPublisherManager =
            ServiceValueHolder.getInstance().getStatPublisherManagerService();

    public BlockingQueue<MessageStat> getMessageQueue() {
        return messageQueue;
    }

    private BlockingQueue<MessageStat> messageQueue = new LinkedBlockingQueue<MessageStat>(10);

    private static final int NumberOfThreads = 5;
    private static int numberofmsg=0;

    private ExecutorService executor = Executors.newFixedThreadPool(NumberOfThreads);
    MessageStat messageStat = new MessageStat();


    //private constructor
    private MessageStatPublisher() {


    }

    //get MessageStatPublisher instance
    public static MessageStatPublisher getInstance() {
        return messageStatPublisher;
    }

    @Override
    public void getMessageDetails(AndesMessageMetadata andesMessageMetadata, int noOfSubscribers) {

        String domain;
        if (andesMessageMetadata.getDestination().split("/").length == 1) {

            domain = "carbon.super";

        } else {
            domain = andesMessageMetadata.getDestination().split("/")[0];
        }

        if (ServiceValueHolder.getInstance().getStatPublisherManagerService().getMessageStatEnableMap().contains(domain))
            try {
                messageStat.setAndesMessageMetadata(andesMessageMetadata);
                messageStat.setDomain(domain);
                messageStat.setNoOfSubscribers(noOfSubscribers);


                messageQueue.put(messageStat);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        Runnable worker = new AsyncMessageStatPublisher(numberofmsg);
        executor.execute(worker);
        numberofmsg++;



    }


    @Override
    public void getAckMessageDetails(AndesAckData andesAckData) {

    }


}
