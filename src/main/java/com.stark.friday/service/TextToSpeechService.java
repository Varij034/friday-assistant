package com.stark.friday.service;

import org.springframework.stereotype.Service;

@Service
public class TextToSpeechService {

    public void speak(String text) {
        try {
            // Rate = 2 increases speaking speed slightly (default is 0)
            String script = String.format(
                    "Add-Type -AssemblyName System.Speech; " +
                            "$synth = New-Object System.Speech.Synthesis.SpeechSynthesizer; " +
                            "$synth.Rate = 2; " +
                            "$synth.Speak('%s');",
                    text.replace("'", "''")
            );

            ProcessBuilder pb = new ProcessBuilder("powershell.exe", "-Command", script);
            pb.start();
        } catch (Exception e) {
            System.err.println("Error in TextToSpeech: " + e.getMessage());
        }
    }
}