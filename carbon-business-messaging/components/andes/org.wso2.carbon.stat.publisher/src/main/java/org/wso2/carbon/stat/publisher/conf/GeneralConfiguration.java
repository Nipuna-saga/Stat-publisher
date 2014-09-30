package org.wso2.carbon.stat.publisher.conf;


public class GeneralConfiguration {
    //timeInterval for timerTask to publish statistics
    private int timeInterval;

    //number of queue slots to store message/ack details
    private int numberOfQueueSlots;

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
     * @param numberOfQueueSlots - int value
     */
    public void setNumberOfQueueSlots(int numberOfQueueSlots) {
        this.numberOfQueueSlots = numberOfQueueSlots;
    }

    /**
    /**
     * Get value of numberOfQueueSlots node
     * @return numberOfQueueSlots
     */
    public int getNumberOfQueueSlots() {
        return numberOfQueueSlots;
    }

}
