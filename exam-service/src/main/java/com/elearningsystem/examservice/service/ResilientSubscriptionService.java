package com.elearningsystem.examservice.service;

import com.elearningsystem.examservice.client.SubscriptionClient;
import com.elearningsystem.examservice.dto.SubscriptionDTO;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResilientSubscriptionService {
    
    private final SubscriptionClient subscriptionClient;
    
    @CircuitBreaker(name = "subscription-service", fallbackMethod = "fallbackIsUserSubscribedAsync")
    @Retry(name = "subscription-service")
    @TimeLimiter(name = "subscription-service")
    @Bulkhead(name = "subscription-service")
    public CompletableFuture<Map<String, Boolean>> isUserSubscribedToCourseAsync(Long userId, Long courseId) {
        log.info("Checking subscription for user {} and course {} with resilience patterns", userId, courseId);
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                Map<String, Boolean> result = subscriptionClient.isUserSubscribedToCourse(userId, courseId);
                log.info("Successfully checked subscription for user {} and course {}: {}", userId, courseId, result);
                return result;
            } catch (Exception e) {
                log.error("Error checking subscription for user {} and course {}: {}", userId, courseId, e.getMessage());
                throw new RuntimeException("Subscription service call failed", e);
            }
        });
    }
    
    @CircuitBreaker(name = "subscription-service", fallbackMethod = "fallbackGetUserSubscriptionsAsync")
    @Retry(name = "subscription-service")
    @TimeLimiter(name = "subscription-service")
    @Bulkhead(name = "subscription-service")
    public CompletableFuture<SubscriptionDTO[]> getUserSubscriptionsAsync(Long userId) {
        log.info("Fetching subscriptions for user {} with resilience patterns", userId);
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                SubscriptionDTO[] result = subscriptionClient.getUserSubscriptions(userId);
                log.info("Successfully fetched {} subscriptions for user {}", result.length, userId);
                return result;
            } catch (Exception e) {
                log.error("Error fetching subscriptions for user {}: {}", userId, e.getMessage());
                throw new RuntimeException("Subscription service call failed", e);
            }
        });
    }
    
    // Synchronous methods with resilience patterns
    @CircuitBreaker(name = "subscription-service", fallbackMethod = "fallbackIsUserSubscribed")
    @Retry(name = "subscription-service")
    @Bulkhead(name = "subscription-service")
    public Map<String, Boolean> isUserSubscribedToCourse(Long userId, Long courseId) {
        log.info("Checking subscription for user {} and course {} (sync)", userId, courseId);
        
        try {
            Map<String, Boolean> result = subscriptionClient.isUserSubscribedToCourse(userId, courseId);
            log.info("Successfully checked subscription for user {} and course {}: {}", userId, courseId, result);
            return result;
        } catch (Exception e) {
            log.error("Error in sync subscription check for user {} and course {}: {}", userId, courseId, e.getMessage());
            throw new RuntimeException("Subscription service call failed", e);
        }
    }
    
    @CircuitBreaker(name = "subscription-service", fallbackMethod = "fallbackGetUserSubscriptions")
    @Retry(name = "subscription-service")
    @Bulkhead(name = "subscription-service")
    public SubscriptionDTO[] getUserSubscriptions(Long userId) {
        log.info("Fetching subscriptions for user {} (sync)", userId);
        
        try {
            SubscriptionDTO[] result = subscriptionClient.getUserSubscriptions(userId);
            log.info("Successfully fetched {} subscriptions for user {}", result.length, userId);
            return result;
        } catch (Exception e) {
            log.error("Error fetching subscriptions for user {}: {}", userId, e.getMessage());
            throw new RuntimeException("Subscription service call failed", e);
        }
    }
    
    // Fallback methods
    public CompletableFuture<Map<String, Boolean>> fallbackIsUserSubscribedAsync(Long userId, Long courseId, Exception ex) {
        log.error("Fallback: Subscription service is unavailable for user {} and course {} due to: {}", userId, courseId, ex.getMessage());
        throw new RuntimeException("Subscription service is currently unavailable. Please try again later.", ex);
    }
    
    public Map<String, Boolean> fallbackIsUserSubscribed(Long userId, Long courseId, Exception ex) {
        log.error("Fallback: Subscription service is unavailable for user {} and course {} due to: {}", userId, courseId, ex.getMessage());
        throw new RuntimeException("Subscription service is currently unavailable. Please try again later.", ex);
    }
    
    public CompletableFuture<SubscriptionDTO[]> fallbackGetUserSubscriptionsAsync(Long userId, Exception ex) {
        log.error("Fallback: Subscription service is unavailable for user {} due to: {}", userId, ex.getMessage());
        throw new RuntimeException("Subscription service is currently unavailable. Please try again later.", ex);
    }
    
    public SubscriptionDTO[] fallbackGetUserSubscriptions(Long userId, Exception ex) {
        log.error("Fallback: Subscription service is unavailable for user {} due to: {}", userId, ex.getMessage());
        throw new RuntimeException("Subscription service is currently unavailable. Please try again later.", ex);
    }
} 