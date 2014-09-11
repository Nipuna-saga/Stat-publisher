package org.wso2.carbon.stat.publisher.internal.internal;

import org.wso2.carbon.stat.publisher.StatPublisherService;

public class StatPublisherBuilder {
    public static StatPublisherService createMediationService(){
        return new StatPublisherService();
    }
}
