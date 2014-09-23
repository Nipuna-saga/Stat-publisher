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

package org.wso2.carbon.stat.publisher.serverStats;

import org.apache.log4j.Logger;
import org.wso2.andes.management.common.JMXConnnectionFactory;
import org.wso2.carbon.stat.publisher.conf.JMXConfiguration;
import org.wso2.carbon.stat.publisher.exception.StatPublisherRuntimeException;
import org.wso2.carbon.stat.publisher.internal.ds.ServiceValueHolder;
import org.wso2.carbon.stat.publisher.serverStats.data.MbeansStatsData;
import org.wso2.carbon.user.core.UserRealm;
import org.wso2.carbon.user.core.UserStoreException;

import javax.management.*;
import javax.management.openmbean.CompositeData;
import javax.management.remote.JMXConnector;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.Set;

//import com.sun.management.OperatingSystemMXBean;
//import sun.management.ManagementFactory;

public class MbeansStats {

    private static JMXConnector jmxConnector;
    private static MBeanServerConnection connection = null;
    private static long timeout = 100000;
    private MbeansStatsData mbeansStatsData;
    private static MbeansStats mbeansStats = null;
    private static final Logger logger = Logger.getLogger(MbeansStats.class);
    public MbeansStats(JMXConfiguration jmxConfiguration)  {

        mbeansStatsData = new MbeansStatsData();

        //get MB username and password
        UserRealm realm;
        String userName;
        String password;
        try {
            realm = ServiceValueHolder.getInstance().getRealmService().getBootstrapRealm();
            userName = realm.getRealmConfiguration().getAdminUserName();
            password = realm.getRealmConfiguration().getAdminPassword();
        } catch (UserStoreException e) {
            throw new StatPublisherRuntimeException("Fail to get admin username or password of MB" , e );
        }
        //create jmxConnection
        createJMXConnection(jmxConfiguration, userName, password);

    }

    private static void createJMXConnection(JMXConfiguration jmxConfiguration, String userName, String password){

        //get JMX port
        final int jmxPort = Integer.parseInt(jmxConfiguration.getRmiRegistryPort()) +
                Integer.parseInt(jmxConfiguration.getOffSet());
        try {
            jmxConnector = JMXConnnectionFactory.getJMXConnection(timeout, jmxConfiguration.getHostName(),
                    jmxPort, userName, password);
            connection = jmxConnector.getMBeanServerConnection();
        } catch (Exception e) {

                try {
                    Thread.sleep(2000);
                    logger.info("==============retrying==================");
                    createJMXConnection(jmxConfiguration , userName , password);

                } catch (InterruptedException e1) {
                    throw new StatPublisherRuntimeException(e1);
                }
        }
    }

    public String HeapMemoryUsage() throws MalformedObjectNameException, IOException, AttributeNotFoundException,
            MBeanException, ReflectionException, InstanceNotFoundException {
            Set<ObjectInstance> set = connection.queryMBeans(new ObjectName("java.lang:type=Memory"), null);
            ObjectInstance oi = set.iterator().next();
            Object attrValue = connection.getAttribute(oi.getObjectName(), "HeapMemoryUsage");

        return ((CompositeData) attrValue).get("used").toString();
    }

    public String NonHeapMemoryUsage() throws MalformedObjectNameException, IOException, AttributeNotFoundException,
            MBeanException, ReflectionException, InstanceNotFoundException {

        Set<ObjectInstance> set = connection.queryMBeans(new ObjectName("java.lang:type=Memory"), null);
        ObjectInstance oi = set.iterator().next();
        Object attrValue_nonHeapMem = connection.getAttribute(oi.getObjectName(), "NonHeapMemoryUsage");

        return ((CompositeData) attrValue_nonHeapMem).get("used").toString();
    }

    public String CPUUsage(){
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();

        return Double.toString(osBean.getSystemLoadAverage());
    }


    public MbeansStatsData getMbeansStatsData() throws MalformedObjectNameException, InstanceNotFoundException, IOException, ReflectionException, AttributeNotFoundException, MBeanException {

        mbeansStatsData.setHeapMemoryUsage(HeapMemoryUsage());
        mbeansStatsData.setNonHeapMemoryUsage(NonHeapMemoryUsage());
        mbeansStatsData.setCPULoadAverage(CPUUsage());

        return mbeansStatsData;
    }
}
