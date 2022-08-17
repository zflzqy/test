package cn.zflzqy.test;

import com.sun.management.OperatingSystemMXBean;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.management.ManagementFactory;

public class MonitorInfoTest {
    public static void main(String[] args)throws Exception {
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        System.out.println();

    } 
} 
class MonitorInfoBean {
    /**
     * 可使用内存.
     */
    private long totalMemory;
    /**
     * 剩余内存.
     */
    private long freeMemory;
    /**
     * 最大可使用内存.
     */
    private long maxMemory;
    /**
     * 操作系统.
     */
    private String osName;
    /**
     * 总的物理内存.
     */
    private long totalPhysicalMemorySize;
    /**
     * 剩余的物理内存.
     */
    private long freePhysicalMemorySize;
    /**
     * 已使用的物理内存.
     */
    private long usedMemory;
    /**
     * 线程总数.
     */
    private int totalThread;
    /**
     * cpu使用率.
     */
    private double cpuRatio;
    /*生成get和set....*/
}