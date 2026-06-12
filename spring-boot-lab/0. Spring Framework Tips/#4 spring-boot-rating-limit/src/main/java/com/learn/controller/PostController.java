package com.learn.controller;

import com.learn.service.PostService;
import com.learn.service.RatingLimitService;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/posts")
public class PostController {

    private final PostService postService;
    private final RatingLimitService ratingLimitService;

    @Value("${user.type}")
    private String userType;


    private Bucket bucket;

    public PostController(PostService postService, RatingLimitService ratingLimitService) {
        this.postService = postService;
        this.ratingLimitService = ratingLimitService;
    }

    // userType is field-injected via @Value, so it isn't available in the
    // constructor yet. @PostConstruct runs after injection -> build the bucket here.
    @PostConstruct
    void initBucket() {
        this.bucket = ratingLimitService.bucket(userType);
        log.info("Rate-limit bucket created for userType={}", userType);
    }

    @GetMapping
    public ResponseEntity<Object> posts() {
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            log.info("Allowed - {} tokens remaining", probe.getRemainingTokens());
            return ResponseEntity.ok()
                    .header("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()))
                    .body(postService.posts());
        }

        long waitSeconds = probe.getNanosToWaitForRefill() / 1_000_000_000;
        log.warn("Rate limit exceeded - retry in {}s", waitSeconds);
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .header("X-Rate-Limit-Retry-After-Seconds", String.valueOf(waitSeconds))
                .build();
    }
}
