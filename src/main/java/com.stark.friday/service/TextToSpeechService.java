package com.stark.friday.service;

import org.springframework.stereotype.Service;

@Service
public class TextToSpeechService {

    public void speak(String text) {
        if (text == null || text.isBlank()) return;

        // Escape double quotes for PowerShell safety
        String cleanText = text.replace("\"", "'");

        // PowerShell script selecting Microsoft Zira (Female Voice)
        String script = String.format(
                "Add-Type -AssemblyName System.Speech; " +
                        "$speech = New-Object System.Speech.Synthesis.SpeechSynthesizer; " +
                        "try { $speech.SelectVoice('Microsoft Zira'); } catch {} " +
                        "$speech.Rate = 2; " +
                        "$speech.Speak(\"%s\");", cleanText
        );

        ProcessBuilder builder = new ProcessBuilder("powershell.exe", "-Command", script);

        try {
            Process process = builder.start();
            process.waitFor();
        } catch (Exception e) {
            System.err.println("TTS Error: " + e.getMessage());
        }
    }
}