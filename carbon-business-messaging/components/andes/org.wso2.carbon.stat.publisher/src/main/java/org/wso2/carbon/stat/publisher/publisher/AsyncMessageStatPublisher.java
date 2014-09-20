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


import org.wso2.carbon.stat.publisher.conf.MessageStat;
import org.wso2.carbon.stat.publisher.conf.StatPublisherConfiguration;
import org.wso2.carbon.stat.publisher.conf.StreamConfiguration;
import org.wso2.carbon.stat.publisher.internal.ds.ServiceValueHolder;
import org.wso2.carbon.user.api.TenantManager;
import org.wso2.carbon.user.api.UserStoreException;

import java.util.concurrent.BlockingQueue;

public class AsyncMessageStatPublisher implements Runnable {

    private MessageStat messageStat;
    private int tenantID;
    private StatPublisherConfiguration statPublisherConfiguration;
    private TenantManager tenantManager;
    private BlockingQueue<MessageStat> messageQueue = MessageStatPublisher.getInstance().getMessageQueue();
    int msg;
    private StreamConfiguration streamConfiguration;

    public AsyncMessageStatPublisher(int msg, StreamConfiguration streamConfiguration) {
//TODO need to remove msg variable it's just use for testing purposes
        this.msg = msg;
        this.streamConfiguration = streamConfiguration;
    }


    @Override
    public void run() {
        //check message Queue has any object
        while (messageQueue.size() > 0) {
            try {
                //get object from queue
                messageStat = MessageStatPublisher.getInstance().getMessageQueue().take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            tenantManager = ServiceValueHolder.getInstance().getRealmService().getTenantManager();
            try {
                //get tenant ID from tenant domain
                tenantID = tenantManager.getTenantId(messageStat.getDomain());
            } catch (UserStoreException e) {
                e.printStackTrace();
            }
            //get statPublisher Configuration relevant to tenantID
            statPublisherConfiguration = ServiceValueHolder.
                    getInstance().getStatPublisherManagerService().getStatPublisherConfiguration(tenantID);
            //check is it a message or Ack message
            if (messageStat.isMessage()) {

                //TODO you can get StreamConfiguration and statPublisherConfiguration for Data Agent

                //TODO add message statPublishing code here you can get message by using messageStat.getAndesMessageMetadata() and No of subs   messageStat.setNoOfSubscribers()
                System.out.print(msg + "+++++++++++++++++++++++ Message Stat Publisher Activated" + Thread.currentThread().getName());
            } else {

                //TODO add message statPublishing code here you can get message by using messageStat.getAndesAckData()

                System.out.print(msg + "+++++++++++++++++++++++ Message Ack Stat Publisher Activated" + Thread.currentThread().getName());


            }

        }
    }

}
