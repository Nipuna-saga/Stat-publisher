package org.wso2.carbon.stat.publisher.conf;

import org.wso2.andes.kernel.AndesMessageMetadata;

public class MessageStat {


    private AndesMessageMetadata andesMessageMetadata;
    private int noOfSubscribers;
    private String domain;

    public AndesMessageMetadata getAndesMessageMetadata() {
        return andesMessageMetadata;
    }

    public void setAndesMessageMetadata(AndesMessageMetadata andesMessageMetadata) {
        this.andesMessageMetadata = andesMessageMetadata;
    }

    public int getNoOfSubscribers() {
        return noOfSubscribers;
    }

    public void setNoOfSubscribers(int noOfSubscribers) {
        this.noOfSubscribers = noOfSubscribers;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
