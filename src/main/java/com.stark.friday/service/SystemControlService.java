package com.stark.friday.service;

import org.springframework.stereotype.Service;
import java.awt.Desktop;
import java.io.File;

@Service
public class SystemControlService {

    public void shutdownPC() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                Runtime.getRuntime().exec("shutdown /s /t 5");
            } else {
                Runtime.getRuntime().exec("shutdown -h now");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openFolder(String path) {
        try {
            File folder = new File(path);
            if (Desktop.isDesktopSupported() && folder.exists()) {
                Desktop.getDesktop().open(folder);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}