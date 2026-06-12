package com.learn.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * Spring AI 1.0 — ChatClient API.
 *
 * ChatClient is the fluent, high-level API for interacting with AI models.
 * It replaces the lower-level ChatModel/ChatLanguageModel from earlier versions.
 *
 * Three patterns demonstrated:
 * 1. Simple chat     — one message, one response
 * 2. System prompt   — set AI role/persona before conversation
 * 3. Streaming       — receive tokens as they arrive (Flux<String>)
 */
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatClient chatClient;

    /**
     * Simple chat — send a message and get a full response.
     * GET /api/chat?message=What is Spring Boot?
     */
    @GetMapping
    public String chat(@RequestParam String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    /**
     * Chat with a system prompt to define AI persona/role.
     * GET /api/chat/expert?topic=Spring+Boot
     */
    @GetMapping("/expert")
    public String chatWithExpert(@RequestParam String topic) {
        return chatClient.prompt()
                .messages(
                        new SystemMessage("You are a senior Java Spring Boot expert. Give concise, practical answers."),
                        new UserMessage("Explain " + topic + " in 3 bullet points.")
                )
                .call()
                .content();
    }

    /**
     * Streaming chat — returns tokens as they are generated.
     * The client receives the response incrementally (Server-Sent Events).
     * GET /api/chat/stream?message=Tell me about reactive programming
     */
    @GetMapping(value = "/stream", produces = "text/event-stream")
    public Flux<String> streamChat(@RequestParam String message) {
        return chatClient.prompt()
                .user(message)
                .stream()
                .content();
    }
}
