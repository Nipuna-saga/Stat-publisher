package org.wso2.carbon.stat.publisher.internal.internal;

import org.wso2.carbon.stat.publisher.StatPublisherService;

public class StatPublisherBuilder {

    public static StatPublisherService createMediationService(){
        StatPublisherService statPublisherService = new StatPublisherService();
        return statPublisherService;
    }
}
