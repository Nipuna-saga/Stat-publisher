package org.wso2.carbon.stat.publisher.publisher;

import org.apache.log4j.Logger;
import org.wso2.carbon.stat.publisher.conf.JMXConfiguration;
import org.wso2.carbon.stat.publisher.conf.StatPublisherConfiguration;
import org.wso2.carbon.stat.publisher.conf.StreamConfiguration;
import org.wso2.carbon.stat.publisher.exception.StatPublisherConfigurationException;
import org.wso2.carbon.stat.publisher.registry.RegistryPersistenceManager;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * StatPublisherObserver create observer Instance for every tenant
 * Stat publishing activation and deactivation methods handle by this class
 */

public class StatPublisherObserver {

    private static final Logger LOGGER = Logger.getLogger(StatPublisherObserver.class);

    private StatPublisherConfiguration statPublisherConfiguration;
    private RegistryPersistenceManager registryPersistenceManager;
    private Timer timer;
    private TimerTask statPublisherTimerTask;
    private boolean enable;
    private StatPublisherDataAgent statPublisherDataAgent;
    private boolean messageStatEnableFlag;

//

    public StatPublisherObserver(JMXConfiguration jmxConfiguration, StreamConfiguration streamConfiguration,
                                 int tenantID) throws StatPublisherConfigurationException {

        registryPersistenceManager = new RegistryPersistenceManager();
        this.statPublisherConfiguration = registryPersistenceManager.loadConfigurationData(tenantID);
        //   statPublisherDataAgent=
        ///            new StatPublisherDataAgent(jmxConfiguration, streamConfiguration, statPublisherConfiguration);


    }

    /**
     * Start periodical statistic Publishing using timer task
     * activate publishing after read registry configurations
     */


    public void startMonitor() {


        if (statPublisherConfiguration.isMessageStatEnable()) {
            messageStatEnableFlag = true;

        }


        //Checking  System or MB stat enable or not
        if (statPublisherConfiguration.isSystemStatEnable() || statPublisherConfiguration.isMbStatEnable()) {


            statPublisherTimerTask = new TimerTask() {
                @Override
                public void run() {
                    //check system stat enable configuration
                    if (statPublisherConfiguration.isSystemStatEnable()) {
                        //System stat publishing activated
                        //dataAgentInstance.sendSystemStats(URL, credentials);
                        LOGGER.info("System stat Publishing activated ");
                    }
                    //check MB stat enable configuration
                    if (statPublisherConfiguration.isMbStatEnable()) {
                        //MB stat publishing activated
                        // dataAgentInstance.sendMBStatistics(URL, credentials);
                        LOGGER.info("MB stat Publishing activated ");
                    }




/*
                    try {
                        statPublisherDataAgent.sendSystemStats();
                    } catch (MalformedObjectNameException e) {
                        e.printStackTrace();
                    } catch (ReflectionException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InstanceNotFoundException e) {
                        e.printStackTrace();
                    } catch (AttributeNotFoundException e) {
                        e.printStackTrace();
                    } catch (MBeanException e) {
                        e.printStackTrace();
                    }

*/

                }


            };
            timer = new Timer();
            // scheduling the task at fixed rate
            long timeInterval = 5000;
            timer.scheduleAtFixedRate(statPublisherTimerTask, new Date(), timeInterval);
        }
    }


    /**
     * Stop periodical statistic Publishing uby stopping timer task
     */

    public void stopMonitor() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public boolean getEnable() {
        return messageStatEnableFlag;
    }
}
