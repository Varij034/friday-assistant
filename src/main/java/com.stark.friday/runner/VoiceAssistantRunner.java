package com.stark.friday.runner;

import org.vosk.LogLevel;
import com.stark.friday.service.FridayBrainService;
import com.stark.friday.service.SystemControlService;
import com.stark.friday.service.TextToSpeechService;
import org.vosk.LibVosk;
import org.vosk.Model;
import org.vosk.Recognizer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sound.sampled.*;

@Component
public class VoiceAssistantRunner implements CommandLineRunner {

    private final FridayBrainService brain;
    private final TextToSpeechService tts;
    private final SystemControlService systemControl;

    public VoiceAssistantRunner(FridayBrainService brain, TextToSpeechService tts, SystemControlService systemControl) {
        this.brain = brain;
        this.tts = tts;
        this.systemControl = systemControl;
    }

    @Override
    public void run(String... args) throws Exception {
        LibVosk.setLogLevel(LogLevel.WARNINGS);

        try (Model model = new Model("models/vosk-model-small-en-us")) {
            AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);

            line.open(format);
            line.start();

            Recognizer recognizer = new Recognizer(model, 16000);
            byte[] buffer = new byte[4096];

            System.out.println("\n>>> F.R.I.D.A.Y. ONLINE & LISTENING <<<\n");

            // Automatic Startup Greeting
            tts.speak("Systems nominal. Hey Boss, what's up?");

            while (true) {
                int bytesRead = line.read(buffer, 0, buffer.length);
                if (recognizer.acceptWaveForm(buffer, bytesRead)) {
                    String json = recognizer.getResult();
                    String input = parseText(json);

                    if (!input.isBlank()) {
                        System.out.println("Boss: " + input);

                        // Intercept local shutdown command
                        if (input.contains("turn off my pc") || input.contains("shutdown")) {
                            tts.speak("Initiating system shutdown. Goodnight, Boss.");
                            systemControl.executeCommand(input);
                            break;
                        }

                        // Fetch live PC stats
                        String currentStats = systemControl.getSystemDiagnostics();

                        // Intercept diagnostic queries directly
                        if (input.contains("system status") || input.contains("system check") || input.contains("diagnostics")) {
                            String stats = systemControl.getSystemDiagnostics();
                            String directResponse = "Boss, " + stats;
                            System.out.println("F.R.I.D.A.Y.: " + directResponse);
                            tts.speak(directResponse);
                            continue;
                        }

                        // Intercept status queries directly or pass to Ollama
                        String response = brain.chat(input, currentStats);

                        System.out.println("F.R.I.D.A.Y.: " + response);
                        tts.speak(response);
                    }
                }
            }
        }
    }

    private String parseText(String json) {
        int start = json.indexOf("\"text\" : \"") + 10;
        int end = json.lastIndexOf("\"");
        if (start > 9 && end > start) {
            return json.substring(start, end).trim();
        }
        return "";
    }
}