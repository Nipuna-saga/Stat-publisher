package org.wso2.andes.kernel;

/**
 * This interface have methods to implement to get messages from andes to other components
 */

public interface StatPublisherGetMessage {

    public void getMessageDetails(AndesMessageMetadata message, int noOfSubscribers);
    public void getAckMessageDetails(AndesAckData ack);

}
