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


import org.apache.log4j.Logger;
import org.wso2.carbon.stat.publisher.conf.MessageStat;
import org.wso2.carbon.stat.publisher.conf.StreamConfiguration;
import org.wso2.carbon.stat.publisher.exception.StatPublisherRuntimeException;
import org.wso2.carbon.stat.publisher.internal.ds.StatPublisherValueHolder;

import org.wso2.carbon.user.api.TenantManager;
import org.wso2.carbon.user.api.UserStoreException;

import javax.management.*;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
//TODO messageStatPublisher
public class AsyncMessageStatPublisher implements Runnable {

    private BlockingQueue<MessageStat> messageQueue = StatPublisherMessageListenerImpl.messageQueue;

    private static final Logger logger = Logger.getLogger(StatPublisherObserver.class);


    public AsyncMessageStatPublisher(StreamConfiguration streamConfiguration) {
        StreamConfiguration streamConfiguration1 = streamConfiguration;
        //this.statPublisherDataAgent=statPublisherDataAgent;
    }


    @Override
    public void run() {

        //check message Queue has any object
        //TODO always true
        while (messageQueue.size() > 0) {
            MessageStat messageStat;
            try {
                //get object from queue
                messageStat = messageQueue.take();
            } catch (InterruptedException e) {
                throw new StatPublisherRuntimeException(e);
            }

            TenantManager tenantManager = StatPublisherValueHolder.getRealmService().getTenantManager();
            int tenantID;
            try {
                //get tenant ID from tenant domain
                tenantID = tenantManager.getTenantId(messageStat.getDomain());
            } catch (UserStoreException e) {
                throw new StatPublisherRuntimeException(e);
            }

            StatPublisherObserver statPublisherObserver = StatPublisherValueHolder.getStatPublisherManager().
                    getStatPublisherObserver(tenantID);
            //check is it a message or Ack message


            //TODO you can get StreamConfiguration and statPublisherConfiguration for Data Agent

            //TODO add message statPublishing code here you can get message by using messageStat.getAndesMessage
            if (messageStat.isMessage()) {
                try {
                    statPublisherObserver.statPublisherDataAgent.sendMessageStats(messageStat.getAndesMessageMetadata(),
                            messageStat.getNoOfSubscribers());
                    logger.info("Sent message stats");
                } catch (MalformedObjectNameException e) {
                    throw new StatPublisherRuntimeException(e);
                } catch (ReflectionException e) {
                    throw new StatPublisherRuntimeException(e);
                } catch (IOException e) {
                    throw new StatPublisherRuntimeException(e);
                } catch (InstanceNotFoundException e) {
                    throw new StatPublisherRuntimeException(e);
                } catch (AttributeNotFoundException e) {
                    throw new StatPublisherRuntimeException(e);
                } catch (MBeanException e) {
                    throw new StatPublisherRuntimeException(e);
                }

            } else {


                System.out.print("+++++++++++++++++++++++ Message Ack Stat Publisher Activated" +
                        Thread.currentThread().getName());

                try {
                    statPublisherObserver.statPublisherDataAgent.sendAckStats(messageStat.getAndesAckData());
                } catch (MalformedObjectNameException e) {
                    throw new StatPublisherRuntimeException(e);
                } catch (ReflectionException e) {
                    throw new StatPublisherRuntimeException(e);
                } catch (IOException e) {
                    throw new StatPublisherRuntimeException(e);
                } catch (InstanceNotFoundException e) {
                    throw new StatPublisherRuntimeException(e);
                } catch (AttributeNotFoundException e) {
                    throw new StatPublisherRuntimeException(e);
                } catch (MBeanException e) {
                    throw new StatPublisherRuntimeException(e);
                }


            }

        }
    }

}
