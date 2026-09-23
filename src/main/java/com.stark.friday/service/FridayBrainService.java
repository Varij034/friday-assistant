package com.stark.friday.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class FridayBrainService {

    private final ChatClient chatClient;

    public FridayBrainService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String chat(String userMessage, String systemMetrics) {
        String systemInstruction = """
            You are F.R.I.D.A.Y., a professional, highly efficient AI desktop assistant.
            You are talking directly to your user, Varij, whom you always address as "Boss".
            
            STRICT RULES:
            - ALWAYS address Varij as "Boss".
            - NEVER invent or make up hardware specs, model numbers, CPU names, or GPU models (e.g., do NOT mention Intel i7, Nvidia, WD Blue, etc.).
            - ONLY report the exact numerical metrics provided in the system diagnostics below.
            - DO NOT mention Tony Stark, Iron Man, Arc Reactors, suits, or fictional Marvel lore.
            - Keep responses concise, direct, and limited to 2 short sentences.
            
            Current System Diagnostics: %s
            """.formatted(systemMetrics);

        return chatClient.prompt()
                .system(systemInstruction)
                .user(userMessage)
                .call()
                .content();
    }
}