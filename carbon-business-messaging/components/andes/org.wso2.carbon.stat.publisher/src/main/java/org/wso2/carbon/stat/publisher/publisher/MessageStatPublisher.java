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
import org.wso2.carbon.stat.publisher.internal.ds.ServiceValueHolder;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.user.core.tenant.TenantManager;

/**
 * This class will handle message stat publishing all events
 */

public class MessageStatPublisher implements StatPublisherGetMessage {

    private static final Logger LOGGER = Logger.getLogger(StatPublisherObserver.class);
    private static MessageStatPublisher messageStatPublisher = new MessageStatPublisher();
    private StatPublisherManager statPublisherManager =
            ServiceValueHolder.getInstance().getStatPublisherManagerService();

    private int tenantID;

    //private constructor
    private MessageStatPublisher() {
    }

    //get MessageStatPublisher instance
    public static MessageStatPublisher getInstance() {
        return messageStatPublisher;
    }

    @Override
    public void getMessageDetails(AndesMessageMetadata andesMessageMetadata, int noOfSubscribers) {

        String[] messageDestinationParts = andesMessageMetadata.getDestination().split("/");

        if (messageDestinationParts.length == 1) {
            tenantID = -1234;
        } else {
            TenantManager tenantManager = ServiceValueHolder.getInstance().getRealmService().getTenantManager();
            try {
                tenantID = tenantManager.getTenantId(messageDestinationParts[1]);
            } catch (UserStoreException e) {
                LOGGER.error("Error occurs while try to get tenant ID of " + messageDestinationParts[1]);
            }

        }


        if (statPublisherManager.getMessageStatEnableFlag(tenantID)) {

            System.out.print("*******************" + andesMessageMetadata.getDestination() + noOfSubscribers + "************");
        }
    }

    @Override
    public void getAckMessageDetails(AndesAckData andesAckData) {
        if (statPublisherManager.getMessageStatEnableFlag(tenantID)) {
            System.out.print("*******************" + andesAckData.qName + "************");
        }
    }
}
