package org.wso2.carbon.stat.publisher;

import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.stat.publisher.internal.DTO.StatConfigurationDTO;
import org.wso2.carbon.stat.publisher.internal.util.URLOperations;
import org.wso2.carbon.stat.publisher.internal.data.StatConfiguration;

public class StatPublisherService {


    private StatConfigurationDTO StatConfigurationDTOObject;

    //StatConfiguration details get method
    public StatConfiguration getStatConfiguration() {
        int tenantID = CarbonContext.getThreadLocalCarbonContext().getTenantId();

        StatConfigurationDTOObject = new StatConfigurationDTO();
        return StatConfigurationDTOObject.ReadRegistry(tenantID);
    }


    //StatConfiguration details set method
    public boolean setStatConfiguration(StatConfiguration StatConfigurationData) {
        StatConfigurationDTOObject = new StatConfigurationDTO();
        StatConfigurationDTOObject.WriteRegistry(StatConfigurationData);


        return true;
    }

    //util validation method
    public boolean URLValidator(String URLS) {

        URLOperations URLValidatorObject = new URLOperations();
        return URLValidatorObject.URLValidator(URLS);


    }
}
