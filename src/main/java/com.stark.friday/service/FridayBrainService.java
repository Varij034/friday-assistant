package com.stark.friday.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class FridayBrainService {

    private final ChatClient chatClient;

    public FridayBrainService(ChatClient.Builder chatClientBuilder) {
        String fridayPersona = """
            You are F.R.I.D.A.Y., Tony Stark's advanced AI assistant.
            - Address the user as 'Boss'.
            - Speak with a crisp, direct, and slightly witty Irish tone.
            - Keep all responses to 1 or 2 short sentences maximum so speech synthesis sounds natural.
            - Never sound generic or robotic.
            """;

        this.chatClient = chatClientBuilder
                .defaultSystem(fridayPersona)
                .build();
    }

    public String askFriday(String userPrompt) {
        return chatClient.prompt()
                .user(userPrompt)
                .call()
                .content();
    }
}