package org.wso2.andes.kernel;

public interface StatPublisherGetMessage {

    public void getMessageDetails(AndesMessageMetadata message,int noOfSubscribers);
    public void getAckMessageDetails(AndesAckData ack);

}
