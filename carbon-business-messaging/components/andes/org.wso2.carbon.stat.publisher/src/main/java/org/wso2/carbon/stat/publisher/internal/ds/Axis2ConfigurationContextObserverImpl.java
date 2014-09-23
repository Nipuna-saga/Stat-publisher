package org.wso2.carbon.stat.publisher.internal.ds;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.log4j.Logger;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.stat.publisher.exception.StatPublisherConfigurationException;
import org.wso2.carbon.stat.publisher.exception.StatPublisherRuntimeException;
import org.wso2.carbon.stat.publisher.internal.publisher.StatPublisherManager;
import org.wso2.carbon.utils.Axis2ConfigurationContextObserver;

public class Axis2ConfigurationContextObserverImpl implements Axis2ConfigurationContextObserver {

    private static final Logger logger = Logger.getLogger(Axis2ConfigurationContextObserverImpl.class);
    @Override
    public void creatingConfigurationContext(int i) {

    }

    @Override
    public void createdConfigurationContext(ConfigurationContext configurationContext) {
        PrivilegedCarbonContext carbonContext = PrivilegedCarbonContext.getThreadLocalCarbonContext();
        int tenantID=carbonContext.getTenantId();
        try {
            StatPublisherManager statPublisherManager = new StatPublisherManager();
            statPublisherManager.createStatPublisherObserver(tenantID);
        }catch (StatPublisherConfigurationException e){
            logger.error("Exception in initializing StatPublisherManager ",e);
            throw new StatPublisherRuntimeException(e);
        }

    }

    @Override
    public void terminatingConfigurationContext(ConfigurationContext configurationContext) {
    }

    @Override
    public void terminatedConfigurationContext(ConfigurationContext configurationContext) {
        PrivilegedCarbonContext carbonContext = PrivilegedCarbonContext.getThreadLocalCarbonContext();
        int tenantID = carbonContext.getTenantId();
        try {
            StatPublisherManager statPublisherManager = new StatPublisherManager();
            statPublisherManager.removeStatPublisherObserver(tenantID);

        } catch (StatPublisherConfigurationException e) {
            logger.error("Exception in removing StatPublisherManager ", e);
            throw new StatPublisherRuntimeException(e);
        }
    }
}