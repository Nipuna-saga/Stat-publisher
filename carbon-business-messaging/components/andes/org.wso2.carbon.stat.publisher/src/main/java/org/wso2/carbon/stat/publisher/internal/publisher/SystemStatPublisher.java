package org.wso2.carbon.stat.publisher.internal.publisher;

import org.wso2.carbon.stat.publisher.internal.DTO.StatConfigurationDTO;

/**
 * Created by nipuna on 8/20/14.
 */
public class SystemStatPublisher implements Runnable {

    private int tenantID;
    private StatConfigurationDTO statConfigurationDTOObject;

    public SystemStatPublisher(int ID) {

        tenantID = ID;
        statConfigurationDTOObject = new StatConfigurationDTO();
    }

    public String Publisher() {

        if (statConfigurationDTOObject.ReadRegistry(tenantID).isSystem_statEnable()) {
            System.out.println("System stat Publishing activated" + tenantID);
        }
        if (statConfigurationDTOObject.ReadRegistry(tenantID).isMB_statEnable()) {
            System.out.println("MB stat Publishing activated" + tenantID);
        }
        return "";
    }


    @Override
    public void run() {

        //  synchronized(Publisher()) {
        Publisher();
        //  }

    }
}
