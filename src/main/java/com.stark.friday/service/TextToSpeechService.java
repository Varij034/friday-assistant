package com.stark.friday.service;

import org.springframework.stereotype.Service;

@Service
public class TextToSpeechService {

    public void speak(String text) {
        if (text == null || text.isBlank()) return;

        String cleanText = text.replaceAll("[^a-zA-Z0-9\\s,.'!?]", "").trim();

        // Updated PowerShell script with $speech.Rate = 2 (~1.5x speed)
        String script =
                "Add-Type -AssemblyName System.Speech; " +
                        "$speech = New-Object System.Speech.Synthesis.SpeechSynthesizer; " +
                        "$femaleVoice = $speech.GetInstalledVoices() | Where-Object { " +
                        "  $_.VoiceInfo.Gender -eq 'Female' -or $_.VoiceInfo.Name -like '*Zira*' -or $_.VoiceInfo.Name -like '*Hazel*' -or $_.VoiceInfo.Name -like '*Eva*' " +
                        "} | Select-Object -First 1; " +
                        "if ($femaleVoice) { $speech.SelectVoice($femaleVoice.VoiceInfo.Name) }; " +
                        "$speech.Rate = 2; " + // Increased speed to 1.5x
                        "$speech.Speak($env:TTS_TEXT);";

        ProcessBuilder pb = new ProcessBuilder(
                "powershell.exe",
                "-NoProfile",
                "-ExecutionPolicy", "Bypass",
                "-Command",
                script
        );

        pb.environment().put("TTS_TEXT", cleanText);

        try {
            Process process = pb.start();
            process.waitFor();
        } catch (Exception e) {
            System.err.println("TTS Error: " + e.getMessage());
        }
    }
}