package com.stark.friday.service;

import org.springframework.stereotype.Service;
import java.io.File;

@Service
public class SystemControlService {

    public void executeCommand(String command) {
        String cmd = command.toLowerCase();

        if (cmd.contains("code") || cmd.contains("visual studio")) {
            openVSCode();
        } else if (cmd.contains("notepad")) {
            launchProcess("notepad.exe");
        } else if (cmd.contains("calculator") || cmd.contains("calc")) {
            launchProcess("calc.exe");
        } else if (cmd.contains("shutdown") || cmd.contains("turn off my pc")) {
            launchProcess("shutdown /s /t 10");
        }
    }

    private void openVSCode() {
        // 1. Try launching directly via system PATH
        if (launchProcess("cmd.exe /c start code")) return;

        // 2. Fallback to default LocalAppData installation path on Windows
        String userHome = System.getProperty("user.home");
        File defaultVsCode = new File(userHome + "\\AppData\\Local\\Programs\\Microsoft VS Code\\Code.exe");

        if (defaultVsCode.exists()) {
            launchProcess(defaultVsCode.getAbsolutePath());
        } else {
            System.out.println("F.R.I.D.A.Y.: VS Code executable path not found.");
        }
    }

    private boolean launchProcess(String command) {
        try {
            Runtime.getRuntime().exec(command);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}