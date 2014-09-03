package org.wso2.carbon.stat.publisher.internal.internal;

import org.wso2.carbon.stat.publisher.StatPublisherService;

/**
 * Created by dilshani on 9/1/14.
 */
public class StatPublisherBuilder {

    public static StatPublisherService createMediationService(){
        StatPublisherService statPublisherService = new StatPublisherService();
        return statPublisherService;
    }
}
