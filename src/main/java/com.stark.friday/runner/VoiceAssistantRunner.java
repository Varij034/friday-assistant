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

                        // 1. Intercept local shutdown command
                        if (input.contains("turn off my pc") || input.contains("shutdown")) {
                            tts.speak("Initiating system shutdown. Goodnight, Boss.");
                            systemControl.executeCommand(input);
                            break;
                        }

                        // 2. Intercept quick casual greetings locally for instant response
                        if (input.equals("hi friday") || input.equals("hello friday") || input.equals("hey friday")) {
                            String quickGreeting = "Always at your service, Boss. What are we working on?";
                            System.out.println("F.R.I.D.A.Y.: " + quickGreeting);
                            tts.speak(quickGreeting);
                            continue;
                        }

                        // 3. Intercept system diagnostics ONLY when explicitly requested
                        boolean isDiagnosticQuery = input.contains("system") || input.contains("status")
                                || input.contains("diagnostics") || input.contains("control panel")
                                || input.contains("specs") || input.contains("performance");

                        String response;
                        if (isDiagnosticQuery) {
                            String currentStats = systemControl.getSystemDiagnostics();
                            response = brain.chatWithDiagnostics(input, currentStats);
                        } else {
                            // 4. Process general conversation with safe fallback
                            response = brain.chat(input);
                            if (response == null || response.isBlank()) {
                                response = "I'm listening, Boss. How can I help?";
                            }
                        }

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