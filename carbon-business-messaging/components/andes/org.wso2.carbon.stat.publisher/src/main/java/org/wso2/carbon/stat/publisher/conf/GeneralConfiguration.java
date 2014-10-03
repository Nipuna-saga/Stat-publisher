package org.wso2.carbon.stat.publisher.conf;

/**
 *  Read general configuration values from mbStatConfiguration.xml file.
 */
public class GeneralConfiguration {
    //timeInterval for timerTask to publish statistics
    private int timeInterval;

    //number of queue slots to store message/ack details
    private int asyncMessagePublisherBufferTime;

    /**
     * Set value of timeInterval node
     * @param timeInterval - int value
     */
    public void setTimeInterval(int timeInterval) {
        this.timeInterval = timeInterval;
    }

    /**
     * Get value of timeInterval node
     * @return timeInterval
     */
    public int getTimeInterval() {
        return timeInterval;
    }

    /**
     * Set value of numberOfQueueSlots node
     * @param asyncMessagePublisherBufferTime - int value
     */
    public void setAsyncMessagePublisherBufferTime(int asyncMessagePublisherBufferTime) {
        this.asyncMessagePublisherBufferTime = asyncMessagePublisherBufferTime;
    }

    /**
     * Get value of numberOfQueueSlots node
     * @return numberOfQueueSlots
     */
    public int getAsyncMessagePublisherBufferTime() {
        return asyncMessagePublisherBufferTime;
    }

}
