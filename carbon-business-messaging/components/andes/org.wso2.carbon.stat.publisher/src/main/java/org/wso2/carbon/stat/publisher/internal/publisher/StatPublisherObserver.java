/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

package org.wso2.carbon.stat.publisher.internal.publisher;

import org.apache.log4j.Logger;
import org.wso2.carbon.stat.publisher.conf.JMXConfiguration;
import org.wso2.carbon.stat.publisher.conf.StatPublisherConfiguration;
import org.wso2.carbon.stat.publisher.conf.StreamConfiguration;
import org.wso2.carbon.stat.publisher.exception.StatPublisherConfigurationException;
import org.wso2.carbon.stat.publisher.internal.ds.StatPublisherValueHolder;
import org.wso2.carbon.stat.publisher.internal.util.RegistryPersistenceManager;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.user.core.tenant.TenantManager;

import javax.management.*;
import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * StatPublisherObserver create observer Instance for every tenant
 * Stat publishing activation and deactivation methods handle by this class
 */

public class StatPublisherObserver {

    private static final Logger logger = Logger.getLogger(StatPublisherObserver.class);
    public StatPublisherDataAgent statPublisherDataAgent;
    TenantManager tenantManager = StatPublisherValueHolder.getRealmService().getTenantManager();
    private StatPublisherConfiguration statPublisherConfiguration;
    private Timer timer;
    private TimerTask statPublisherTimerTask;
    private String tenantDomain = null;
    private int tenantID;
    private StreamConfiguration streamConfiguration;

    public StatPublisherObserver(JMXConfiguration jmxConfiguration, StreamConfiguration streamConfiguration,
                                 int tenantID) throws StatPublisherConfigurationException {
        this.tenantID = tenantID;
        this.streamConfiguration=streamConfiguration;
        this.statPublisherConfiguration = RegistryPersistenceManager.loadConfigurationData(tenantID);
        if (statPublisherConfiguration.isSystemStatEnable() || statPublisherConfiguration.isMbStatEnable()
                ) {
           statPublisherDataAgent =
                    new StatPublisherDataAgent(jmxConfiguration, streamConfiguration, statPublisherConfiguration);
        }

    }

    public String getTenantDomain() {
        return tenantDomain;
    }

    /**
     * Start periodical statistic Publishing using timer task
     * activate publishing after read registry configurations
     */
    public void startObserver() throws UserStoreException {

        if (statPublisherConfiguration.isMessageStatEnable()) {
            tenantDomain = tenantManager.getDomain(tenantID);
        }
        //Checking  System or MB stat enable or not
        if (statPublisherConfiguration.isSystemStatEnable() || statPublisherConfiguration.isMbStatEnable()) {

            statPublisherTimerTask = new TimerTask() {
                @Override
                public void run() {
                    //check system stat enable configuration
                    if (statPublisherConfiguration.isSystemStatEnable()) {
                        //System stat publishing activated
                        try {


                              statPublisherDataAgent.sendSystemStats();

                        } catch (MalformedObjectNameException e) {
                            logger.error("Fail to publish system stats");
                        } catch (ReflectionException e) {
                            logger.error("Fail to publish system stats");
                        } catch (IOException e) {
                            logger.error("Fail to publish system stats");
                        } catch (InstanceNotFoundException e) {
                            logger.error("Fail to publish system stats");
                        } catch (AttributeNotFoundException e) {
                            logger.error("Fail to publish system stats");
                        } catch (MBeanException e) {
                            logger.error("Fail to publish system stats");
                        }

                        logger.info("System stat Publishing activated ");
                    }
                    //check MB stat enable configuration
                    if (statPublisherConfiguration.isMbStatEnable()) {
                      try {
                            statPublisherDataAgent.sendMBStats();
                        } catch (MalformedObjectNameException e) {
                          logger.error("Fail to publish MB stats");
                        } catch (ReflectionException e) {
                          logger.error("Fail to publish MB stats");
                        } catch (IOException e) {
                          logger.error("Fail to publish MB stats");
                        } catch (InstanceNotFoundException e) {
                          logger.error("Fail to publish MB stats");
                        } catch (AttributeNotFoundException e) {
                          logger.error("Fail to publish MB stats");
                        } catch (MBeanException e) {
                          logger.error("Fail to publish MB stats");
                        }


                        logger.info("MB stat Publishing activated ");
                    }
                }


            };
            timer = new Timer();
            // scheduling the task at fixed rate
            Thread timerTaskThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    timer.scheduleAtFixedRate(statPublisherTimerTask, new Date(),streamConfiguration.getTimeInterval() );
                }
            });
            timerTaskThread.start();
        }
    }

    /**
     * Stop periodical statistic Publishing uby stopping timer task
     */
    public void stopObserver() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public StatPublisherConfiguration getStatPublisherConfiguration() {
        return statPublisherConfiguration;
    }
}
