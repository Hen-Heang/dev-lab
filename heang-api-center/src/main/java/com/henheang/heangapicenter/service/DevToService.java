package com.henheang.heangapicenter.service;

import com.henheang.heangapicenter.dto.DevToArticleDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class DevToService {

    private static final String DEV_TO_BASE_URL = "https://dev.to/api";

    private final WebClient.Builder webClientBuilder;

    public Flux<DevToArticleDto> getArticlesByTag(String tag, int perPage) {
        log.info("Fetching Dev.to articles - tag: {}, perPage: {}", tag, perPage);
        return webClientBuilder.build()
                .get()
                .uri(DEV_TO_BASE_URL + "/articles?tag={tag}&per_page={perPage}", tag, perPage)
                .retrieve()
                .bodyToFlux(DevToArticleDto.class)
                .doOnError(e -> log.error("Failed to fetch articles from Dev.to: {}", e.getMessage()));
    }

    public Mono<DevToArticleDto> getArticleById(Long id) {
        log.info("Fetching Dev.to article - id: {}", id);
        return webClientBuilder.build()
                .get()
                .uri(DEV_TO_BASE_URL + "/articles/{id}", id)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(),
                        response -> response.bodyToMono(String.class)
                                .doOnNext(body -> log.error("Dev.to error for id {}: {}", id, body))
                                .then(Mono.error(new WebClientResponseException(
                                        response.statusCode().value(), "Dev.to API error", null, null, null))))
                .bodyToMono(DevToArticleDto.class)
                .doOnError(e -> log.error("Failed to fetch article {} from Dev.to: {}", id, e.getMessage()))
                .onErrorResume(e -> Mono.empty());
    }
}