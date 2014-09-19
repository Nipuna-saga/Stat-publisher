package org.wso2.carbon.stat.publisher.internal.ds;

import org.apache.axis2.context.ConfigurationContext;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.utils.Axis2ConfigurationContextObserver;

/**
 * Created by nipuna on 9/19/14.
 */
public class Axis2ConfigurationContextObserverImpl implements Axis2ConfigurationContextObserver {
    @Override
    public void creatingConfigurationContext(int i) {
        System.out.println(i + "++++++++++++++++++++++++++++++++++++++++++++");

    }

    @Override
    public void createdConfigurationContext(ConfigurationContext configurationContext) {

    }

    @Override
    public void terminatingConfigurationContext(ConfigurationContext configurationContext) {
        System.out.println(PrivilegedCarbonContext.getCurrentContext(
                configurationContext).getTenantId()+"++++++++++++++++++++++++++++++++++++++++++++");
    }

    @Override
    public void terminatedConfigurationContext(ConfigurationContext configurationContext) {
        System.out.println(PrivilegedCarbonContext.getCurrentContext(
                configurationContext).getTenantId()+"++++++++++++++++++++++++++++++++++++++++++++");
    }
}
