package org.wso2.carbon.serverStats.mbeans;

import org.wso2.andes.management.common.JMXConnnectionFactory;

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

	private String heapMemoryUsage = null;
    private String nonHeapMemoryUsage = null;
    private String CPULoadAverage;
    private JMXConnector jmxc;
    private MBeanServerConnection connection;

	
public  MbeansStats(String host,int port, String username, String password) throws Exception {


   jmxc =  JMXConnnectionFactory.getJMXConnection(100000, host, port, username,password);
	
	//JMXConnector jmxc = JMXConnectorFactory.connect(url, environment);
	//MBeanServerConnection conn =   
	
	connection =jmxc.getMBeanServerConnection();

    setHeapMemoryUsageAndNonHeapMemUsage();
   setCPUUsage();




}





    public void setHeapMemoryUsageAndNonHeapMemUsage() throws MalformedObjectNameException, IOException, AttributeNotFoundException, MBeanException, ReflectionException, InstanceNotFoundException {


        Set<ObjectInstance> set = connection.queryMBeans(new ObjectName("java.lang:type=Memory"), null);
        ObjectInstance oi = set.iterator().next();
        // replace "HeapMemoryUsage" with "NonHeapMemoryUsage" to get non-heap mem
        Object attrValue = connection.getAttribute(oi.getObjectName(), "HeapMemoryUsage");
    /*    if( !( attrValue instanceof CompositeData ) ) {
            System.out.println( "attribute value is instanceof [" + attrValue.getClass().getName() +
                    ", exitting -- must be CompositeData." );
            return;
        }*/
        System.out.println("testing mbeans ..............................................");


        // replace "used" with "max" to get max
        heapMemoryUsage = ((CompositeData)attrValue).get("used").toString();

        Object attrValue_nonHeapMem = connection.getAttribute(oi.getObjectName(), "NonHeapMemoryUsage");
        nonHeapMemoryUsage = ((CompositeData)attrValue_nonHeapMem).get("used").toString();


    }

    public void setCPUUsage() throws MalformedObjectNameException, IOException, AttributeNotFoundException, MBeanException, ReflectionException, InstanceNotFoundException {


     /*   Set<ObjectInstance> set = connection.queryMBeans(new ObjectName("java.lang:type=OperatingSystem"), null);
        ObjectInstance oi = set.iterator().next();


        Object attrValue = connection.getAttribute(oi.getObjectName(), "SystemLoadAverage");

        System.out.println("CPU LOAD::::::::::::"+attrValue.toString());
        */


        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
// What % CPU load this current JVM is taking, from 0.0-1.0
        System.out.println("system average::::::::::::::::::::::" +osBean.getSystemLoadAverage());

        CPULoadAverage = Double.toString(osBean.getSystemLoadAverage());

// What % load the overall system is at, from 0.0-1.0
       // System.out.println(osBean.getSystemCpuLoad());



/*
        ObjectName name = new ObjectName("oracle.jrockit.management:type=Runtime");
        Double jvmCpuLoad =(Double)connection.getAttribute(name, "VMGeneratedCPULoad");

        System.out.println("Cpu load::::::::::::::::::::::::::: " + jvmCpuLoad);
*/





    }

    public String getHeapMemoryUsage(){

        return heapMemoryUsage;

    }

    public String getNonHeapMemoryUsage(){

        return nonHeapMemoryUsage;
    }

    public String getCPULoadAverage(){
        return CPULoadAverage;
    }
}
