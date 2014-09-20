package org.wso2.carbon.stat.publisher.internal.ds;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.log4j.Logger;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.stat.publisher.exception.StatPublisherConfigurationException;
import org.wso2.carbon.stat.publisher.publisher.StatPublisherManager;
import org.wso2.carbon.utils.Axis2ConfigurationContextObserver;

public class Axis2ConfigurationContextObserverImpl implements Axis2ConfigurationContextObserver {

    private static final Logger logger = Logger.getLogger(Axis2ConfigurationContextObserverImpl.class);
    @Override
    public void creatingConfigurationContext(int i) {
        System.out.println(i + "++++++++++++++++++++creatingConfigurationContext++++++++++++++++++++++++");

    }

    @Override
    public void createdConfigurationContext(ConfigurationContext configurationContext) {
        PrivilegedCarbonContext carbonContext = PrivilegedCarbonContext.getThreadLocalCarbonContext();
        int tenantID=carbonContext.getTenantId();
        try {
            StatPublisherManager statPublisherManager = new StatPublisherManager();
            statPublisherManager.onCreate(tenantID);
            System.out.println(tenantID+ "++++++++++++++++++++createdConfigurationContext++++++++++++++++++++++++");
        }catch (StatPublisherConfigurationException e){
            logger.error("Exception in initializing StatPublisherManager ",e);
        }

    }

    @Override
    public void terminatingConfigurationContext(ConfigurationContext configurationContext) {
        System.out.println("++++++++++++++++++++terminatingConfigurationContext++++++++++++++++++++++++");
    }

    @Override
    public void terminatedConfigurationContext(ConfigurationContext configurationContext) {
        PrivilegedCarbonContext carbonContext = PrivilegedCarbonContext.getThreadLocalCarbonContext();
        int tenantID = carbonContext.getTenantId();
        try {
            StatPublisherManager statPublisherManager = new StatPublisherManager();
            statPublisherManager.onRemove(tenantID);

        } catch (StatPublisherConfigurationException e) {
            logger.error("Exception in initializing StatPublisherManager ", e);
        }
    }
}