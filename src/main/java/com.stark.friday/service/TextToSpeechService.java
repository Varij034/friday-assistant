package com.stark.friday.service;

import org.springframework.stereotype.Service;

@Service
public class TextToSpeechService {

    public void speak(String text) {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            ProcessBuilder builder;

            if (os.contains("win")) {
                String script = String.format(
                        "Add-Type –AssemblyName System.Speech; " +
                                "$synth = New-Object System.Speech.Synthesis.SpeechSynthesizer; " +
                                "$synth.Speak('%s');", text.replace("'", "''"));
                builder = new ProcessBuilder("powershell", "-Command", script);
            } else if (os.contains("mac")) {
                builder = new ProcessBuilder("say", text);
            } else {
                builder = new ProcessBuilder("espeak", text);
            }

            Process process = builder.start();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}