package com.stark.friday.service;

import org.springframework.stereotype.Service;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import java.io.File;

@Service
public class SystemControlService {

    public String getSystemDiagnostics() {
        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        double cpuLoad = osBean.getCpuLoad() * 100;
        long totalMemory = osBean.getTotalMemorySize() / (1024 * 1024 * 1024); // GB
        long freeMemory = osBean.getFreeMemorySize() / (1024 * 1024 * 1024);   // GB
        long usedMemory = totalMemory - freeMemory;

        File cDrive = new File("C:");
        long freeDisk = cDrive.getFreeSpace() / (1024 * 1024 * 1024); // GB
        long totalDisk = cDrive.getTotalSpace() / (1024 * 1024 * 1024); // GB

        return String.format(
                "Real PC Metrics -> CPU Usage: %.1f%%, RAM Used: %dGB/%dGB, C: Drive Free Space: %dGB/%dGB",
                cpuLoad < 0 ? 0 : cpuLoad, usedMemory, totalMemory, freeDisk, totalDisk
        );
    }

    public void executeCommand(String command) {
        String cmd = command.toLowerCase();
        if (cmd.contains("code") || cmd.contains("visual studio")) {
            try { Runtime.getRuntime().exec("cmd.exe /c start code"); } catch (Exception ignored) {}
        } else if (cmd.contains("notepad")) {
            try { Runtime.getRuntime().exec("notepad.exe"); } catch (Exception ignored) {}
        }
    }
}