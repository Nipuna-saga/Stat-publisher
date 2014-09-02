package org.wso2.carbon.stat.publisher.internal.DTO;

import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.stat.publisher.internal.data.StatConfiguration;
import org.wso2.carbon.stat.publisher.internal.publisher.PublisherObserver;

/**
 * Created by nipuna on 8/15/14.
 */
public class StatConfigurationDTO {


    //write stat configurations to registry
    public boolean WriteRegistry(StatConfiguration statConfigurationObject) {

        int tenantID = CarbonContext.getThreadLocalCarbonContext().getTenantId();
        // TODO:if isEnableStatPublisher is false and getPassword ,getUsername and getURL == null That should be update only Stat publisher field
        System.out.println(statConfigurationObject.isEnableStatPublisher());
        System.out.println(statConfigurationObject.getUsername());
        System.out.println(statConfigurationObject.getPassword());
        System.out.println(statConfigurationObject.getURL());
        System.out.println(statConfigurationObject.isMB_statEnable());
        System.out.println(statConfigurationObject.isMessage_statEnable());
        System.out.println(statConfigurationObject.isSystem_statEnable());
        System.out.println(tenantID);

        PublisherObserver obj = new PublisherObserver();
        obj.statPublisherTimerTask();


        return true;
    }

    //read stat configurations from registry
    public StatConfiguration ReadRegistry(int tenantID) {

        StatConfiguration statConfigurationReadObject = new StatConfiguration();
        if (tenantID == -1234) {
            statConfigurationReadObject.setEnableStatPublisher(false);
            statConfigurationReadObject.setUsername("admin56");
            statConfigurationReadObject.setPassword("admin");
            statConfigurationReadObject.setTenantID(-1234);

            statConfigurationReadObject.setMB_statEnable(false);
            statConfigurationReadObject.setMessage_statEnable(true);
            statConfigurationReadObject.setURL("127.0.0.1:9443;127.0.0.1:9443");
            statConfigurationReadObject.setSystem_statEnable(true);
        } else {

            statConfigurationReadObject.setEnableStatPublisher(false);
            statConfigurationReadObject.setUsername("tenant");
            statConfigurationReadObject.setPassword("admin");
            statConfigurationReadObject.setTenantID(1);

            statConfigurationReadObject.setMB_statEnable(true);
            statConfigurationReadObject.setMessage_statEnable(true);
            statConfigurationReadObject.setURL("127.0.0.1:944377");
            statConfigurationReadObject.setSystem_statEnable(false);
        }
        return statConfigurationReadObject;

    }


}
