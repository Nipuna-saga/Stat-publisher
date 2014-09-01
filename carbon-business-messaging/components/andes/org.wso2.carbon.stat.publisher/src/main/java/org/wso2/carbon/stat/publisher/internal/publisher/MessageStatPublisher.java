package org.wso2.carbon.stat.publisher.internal.publisher;

import org.wso2.carbon.stat.publisher.internal.DTO.StatConfigurationDTO;

/**
 * Created by nipuna on 9/1/14.
 */
public class MessageStatPublisher implements Runnable {

    int tenantID;
    StatConfigurationDTO statConfigurationDTOObject;

    public MessageStatPublisher(int TID) {

        tenantID = TID;
        statConfigurationDTOObject = new StatConfigurationDTO();
    }


    @Override
    public void run() {

        if (statConfigurationDTOObject.ReadRegistry(tenantID).isEnableStatPublisher()) {
            if (statConfigurationDTOObject.ReadRegistry(tenantID).isMessage_statEnable()) {
                Publisher();
            }
        }


    }

    public String Publisher() {


        System.out.println("Message stat Publishing activated" + tenantID);

        return "";
    }

}
