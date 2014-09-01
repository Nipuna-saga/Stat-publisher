package org.wso2.carbon.stat.publisher.ui;

import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.wso2.carbon.stat.publisher.internal.data.xsd.StatConfiguration;

import java.rmi.RemoteException;


public class StatPublisherClient {

    private StatPublisherServiceStub stub;
    private static StatPublisherClient statPublisherClient;


    public StatPublisherClient(ConfigurationContext configCtx, String backendServerURL, String cookie) throws Exception {
        String serviceURL = backendServerURL + "StatPublisherService";
        stub = new StatPublisherServiceStub(configCtx, serviceURL);
        ServiceClient client = stub._getServiceClient();
        Options options = client.getOptions();
        options.setManageSession(true);
        options.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, cookie);
        statPublisherClient = this;
    }

    public static StatPublisherClient getStatPublisherClient() {

        return statPublisherClient;
    }

    public StatConfiguration getStatConfiguration() {

        StatConfiguration response;
        try {
            response = stub.getStatConfiguration();
        } catch (RemoteException e) {
            response = new StatConfiguration();
        }

        return response;
    }


    public String setStatConfiguration(StatConfiguration statConfigurationObject) {
        String response;

        try {
            stub.setStatConfiguration(statConfigurationObject);
            response = "Configuration Saved Successfully";
        } catch (RemoteException e) {
            response = "" + e;
        }

        return response;
    }


    public boolean URLValidator(String URL) {

        boolean response;

        try {
            response = stub.uRLValidator(URL);
        } catch (Exception e) {
            response = false;
        }
        return response;
    }

}
