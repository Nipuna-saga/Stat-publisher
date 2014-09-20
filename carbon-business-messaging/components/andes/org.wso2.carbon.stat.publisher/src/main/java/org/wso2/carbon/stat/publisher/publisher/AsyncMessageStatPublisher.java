package org.wso2.carbon.stat.publisher.publisher;


import org.wso2.carbon.stat.publisher.conf.MessageStat;
import org.wso2.carbon.stat.publisher.conf.StatPublisherConfiguration;
import org.wso2.carbon.stat.publisher.internal.ds.ServiceValueHolder;
import org.wso2.carbon.user.api.TenantManager;
import org.wso2.carbon.user.api.UserStoreException;

import java.util.concurrent.BlockingQueue;

public class AsyncMessageStatPublisher implements Runnable {

    private MessageStat messageStat;
    private int tenantID;
    private StatPublisherConfiguration statPublisherConfiguration;
    private TenantManager tenantManager;
    private BlockingQueue<MessageStat> messageQueue = MessageStatPublisher.getInstance().getMessageQueue();
    int msg;

    public AsyncMessageStatPublisher(int msg) {

        this.msg = msg;
    }


    @Override
    public void run() {
        while (messageQueue.size() > 0) {
            try {
                messageStat = MessageStatPublisher.getInstance().getMessageQueue().take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            tenantManager = ServiceValueHolder.getInstance().getRealmService().getTenantManager();
            try {
                tenantID = tenantManager.getTenantId(messageStat.getDomain());
            } catch (UserStoreException e) {
                e.printStackTrace();
            }

            statPublisherConfiguration = ServiceValueHolder.
                    getInstance().getStatPublisherManagerService().getStatPublisherConfiguration(tenantID);

            System.out.print(msg + "+++++++++++++++++++++++ Message Stat Publisher Activated" + Thread.currentThread().getName());


        }
    }

}
