package org.wso2.carbon.stat.publisher.publisher;

import org.apache.log4j.Logger;
import org.wso2.andes.kernel.AndesAckData;
import org.wso2.andes.kernel.AndesMessageMetadata;
import org.wso2.andes.kernel.StatPublisherGetMessage;
import org.wso2.carbon.stat.publisher.internal.ds.ServiceValueHolder;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.user.core.tenant.TenantManager;


public class MessageStatPublisher implements StatPublisherGetMessage {
    private static final Logger LOGGER = Logger.getLogger(StatPublisherObserver.class);
    private static MessageStatPublisher messageStatPublisher = new MessageStatPublisher();
    private StatPublisherManager statPublisherManager =
            ServiceValueHolder.getInstance().getStatPublisherManagerService();

    private int tenantID;

    private MessageStatPublisher() {
    }

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
