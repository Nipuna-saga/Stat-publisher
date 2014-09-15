package org.wso2.carbon.stat.publisher.serverStats.data;

public class MbeansStatsData {

    private String heapMemoryUsage;
    private String nonHeapMemoryUsage;
    private String CPULoadAverage;

    public String getHeapMemoryUsage() {
        return heapMemoryUsage;
    }

    public void setHeapMemoryUsage(String heapMemoryUsage) {
        this.heapMemoryUsage = heapMemoryUsage;
    }

    public String getNonHeapMemoryUsage() {
        return nonHeapMemoryUsage;
    }

    public void setNonHeapMemoryUsage(String nonHeapMemoryUsage) {
        this.nonHeapMemoryUsage = nonHeapMemoryUsage;
    }

    public String getCPULoadAverage() {
        return CPULoadAverage;
    }

    public void setCPULoadAverage(String CPULoadAverage) {
        this.CPULoadAverage = CPULoadAverage;
    }
}
