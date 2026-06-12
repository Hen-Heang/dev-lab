package com.henheang.heangapicenter.controller;

import com.henheang.heangapicenter.common.api.ApiResponse;
import com.henheang.heangapicenter.dto.DevToArticleDto;
import com.henheang.heangapicenter.service.DevToService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/tech-news")
@RequiredArgsConstructor
public class TechNewsController {

    private final DevToService devToService;

    @GetMapping("/devto")
    public Mono<ApiResponse<List<DevToArticleDto>>> getDevToArticles(
            @RequestParam(defaultValue = "java") String tag,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        return devToService.getArticlesByTag(tag, perPage)
                .collectList()
                .map(ApiResponse::success);
    }

    @GetMapping("/devto/{id}")
    public Mono<ApiResponse<DevToArticleDto>> getDevToArticleById(@PathVariable Long id) {
        return devToService.getArticleById(id)
                .map(article -> ApiResponse.success(article))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found")));
    }
}