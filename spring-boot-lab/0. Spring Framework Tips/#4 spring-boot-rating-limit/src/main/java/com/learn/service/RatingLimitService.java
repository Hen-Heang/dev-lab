package com.learn.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * Builds a Bucket4j token bucket based on the caller's plan.
 *
 * Rate limits:
 *   GOLD  -> 5 requests / minute
 *   other -> 2 requests / minute
 *
 * Each request consumes one token; tokens refill on the interval below.
 */
@Service
public class RatingLimitService {

    public Bucket bucket(String userType) {
        return Bucket.builder()
                .addLimit(getBucketBandwidth(userType))
                .build();
    }

    public Bandwidth getBucketBandwidth(String userType) {
        if ("GOLD".equals(userType)) {
            return Bandwidth.classic(5, Refill.intervally(5, Duration.ofMinutes(1)));
        }
        return Bandwidth.classic(2, Refill.intervally(2, Duration.ofMinutes(1)));
    }
}
