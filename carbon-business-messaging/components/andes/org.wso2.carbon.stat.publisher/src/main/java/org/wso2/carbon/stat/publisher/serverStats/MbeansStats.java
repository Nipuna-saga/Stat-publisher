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

import org.wso2.andes.management.common.JMXConnnectionFactory;
import org.wso2.carbon.stat.publisher.conf.JMXConfiguration;
import org.wso2.carbon.stat.publisher.internal.ds.ServiceValueHolder;
import org.wso2.carbon.stat.publisher.serverStats.data.MbeansStatsData;
import org.wso2.carbon.user.core.UserRealm;

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


    private JMXConnector jmxConnector;
    private MBeanServerConnection connection;
    private long timeout = 100000;
    private MbeansStatsData mbeansStatsData;


    public MbeansStats(JMXConfiguration jmxConfiguration) throws Exception {

        mbeansStatsData = new MbeansStatsData();

        //get MB username and password
        UserRealm realm = ServiceValueHolder.getInstance().getRealmService().getBootstrapRealm();
        String userName = realm.getRealmConfiguration().getAdminUserName();
        String password = realm.getRealmConfiguration().getAdminPassword();

        //get JMX port
        int jmxPort = Integer.parseInt(jmxConfiguration.getRmiRegistryPort()) + Integer.parseInt(jmxConfiguration.getOffSet());

        //create JMX connection
        jmxConnector = JMXConnnectionFactory.getJMXConnection(timeout, jmxConfiguration.getHostName(), jmxPort, userName, password);
        connection = jmxConnector.getMBeanServerConnection();
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
