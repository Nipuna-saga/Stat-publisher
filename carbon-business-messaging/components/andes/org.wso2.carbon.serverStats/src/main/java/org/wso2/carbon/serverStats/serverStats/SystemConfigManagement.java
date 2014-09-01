package org.wso2.carbon.serverStats.serverStats;

/**
 * Created by sanjaya on 7/26/14.
 */
import org.apache.log4j.Logger;

import javax.management.*;
import java.lang.management.ManagementFactory;

public class SystemConfigManagement {
    private static Logger logger = Logger.getLogger(SystemConfigManagement.class);
    private static final int DEFAULT_NO_THREADS=10;
    private static final String DEFAULT_SCHEMA="default";

    public static void main(String[] args) throws MalformedObjectNameException, InterruptedException, InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
        //Get the MBean server
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        //register the MBean
        SystemConfig mBean = new SystemConfig(DEFAULT_NO_THREADS, DEFAULT_SCHEMA);
        ObjectName name = new ObjectName("com.journaldev.jmx:type=SystemConfig");
        mbs.registerMBean(mBean, name);
        do{
            Thread.sleep(3000);
            logger.info("Thread Count="+mBean.getThreadCount()+":::Schema Name="+mBean.getSchemaName());
        }while(mBean.getThreadCount() !=0);

    }

}