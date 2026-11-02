package com.stark.friday.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class FridayBrainService {

    private final ChatClient chatClient;

    public FridayBrainService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String chat(String userMessage) {
        String systemInstruction = """
            You are F.R.I.D.A.Y., a friendly, highly intelligent AI desktop assistant.
            You are speaking directly to your user, whom you always address as "Boss".
            
            RULES:
            - ALWAYS address the user as "Boss".
            - Keep responses short, direct, and conversational (1 to 2 sentences).
            - Respond naturally to casual greetings, questions, or statements.
            - Do NOT bring up system diagnostics, CPU, RAM, or hardware specs unless explicitly asked.
            - Do NOT mention Iron Man, Tony Stark, Arc Reactors, or Marvel lore.
            """;

        try {
            return chatClient.prompt()
                    .system(systemInstruction)
                    .user(userMessage)
                    .call()
                    .content();
        } catch (Exception e) {
            System.err.println("Ollama Connection Error: " + e.getMessage());
            return "Boss, I am unable to connect to Ollama. Please ensure Ollama is running.";
        }
    }

    public String chatWithDiagnostics(String userMessage, String systemMetrics) {
        String systemInstruction = """
            You are F.R.I.D.A.Y., answering a direct system diagnostic request for your user, "Boss".
            
            RULES:
            - Address the user as "Boss".
            - Summarize the provided system metrics directly and concisely in 1-2 sentences.
            - Do NOT make up hardware specs or model names.
            
            Current Metrics: %s
            """.formatted(systemMetrics);

        try {
            return chatClient.prompt()
                    .system(systemInstruction)
                    .user(userMessage)
                    .call()
                    .content();
        } catch (Exception e) {
            System.err.println("Ollama Connection Error: " + e.getMessage());
            return "Boss, " + systemMetrics;
        }
    }
}