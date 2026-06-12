package com.learn.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Prompt Templates — externalize prompts from code.
 *
 * Instead of hardcoding prompts in Java, store them in .st (StringTemplate)
 * files under src/main/resources/prompts/.
 * Variables in templates use {variableName} syntax.
 *
 * Benefits:
 * - Prompts are easy to edit without recompiling
 * - Can be version-controlled separately
 * - Non-developers can update prompts
 */
@RestController
@RequestMapping("/api/prompt")
@RequiredArgsConstructor
public class PromptController {

    private final ChatClient chatClient;

    /**
     * Uses an external prompt template file.
     * GET /api/prompt/recipe?ingredient=chicken&cuisine=Thai
     */
    @GetMapping("/recipe")
    public String getRecipe(@RequestParam String ingredient,
                            @RequestParam String cuisine) {
        PromptTemplate template = new PromptTemplate(new ClassPathResource("prompts/recipe.st"));
        String prompt = template.render(Map.of(
                "ingredient", ingredient,
                "cuisine", cuisine
        ));

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }

    /**
     * Code review prompt template.
     * GET /api/prompt/review?language=Java&code=System.out.println("hello")
     */
    @GetMapping("/review")
    public String reviewCode(@RequestParam String language,
                              @RequestParam String code) {
        PromptTemplate template = new PromptTemplate(new ClassPathResource("prompts/code-review.st"));
        String prompt = template.render(Map.of(
                "language", language,
                "code", code
        ));

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}
