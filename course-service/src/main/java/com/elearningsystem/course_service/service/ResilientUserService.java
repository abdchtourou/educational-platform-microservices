package com.elearningsystem.course_service.service;

import com.elearningsystem.course_service.client.UserClient;
import com.elearningsystem.course_service.dto.UserDTO;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResilientUserService {

    private final UserClient userClient;

    @CircuitBreaker(name = "user-service", fallbackMethod = "fallbackGetUserById")
    @Retry(name = "user-service")
    @TimeLimiter(name = "user-service")
    @Bulkhead(name = "user-service")
    public UserDTO getUserById(Long id) {
        log.info("Fetching user {} from user-service with resilience patterns", id);
        return userClient.getUserById(id);
    }

    @CircuitBreaker(name = "user-service", fallbackMethod = "fallbackGetUserByEmail")
    @Retry(name = "user-service")
    @TimeLimiter(name = "user-service")
    @Bulkhead(name = "user-service")
    public UserDTO getUserByEmail(String email) {
        log.info("Fetching user by email {} from user-service with resilience patterns", email);
        return userClient.getUserByEmail(email);
    }

    // Fallback methods
    public UserDTO fallbackGetUserById(Long id, Exception ex) {
        log.error("Fallback: Unable to fetch user {} due to {}", id, ex.getMessage());
        return new UserDTO(id, "unavailable", "unavailable@example.com", "Unknown", "User", "USER", null);
    }

    public UserDTO fallbackGetUserByEmail(String email, Exception ex) {
        log.error("Fallback: Unable to fetch user by email {} due to {}", email, ex.getMessage());
        return new UserDTO(null, "unavailable", email, "Unknown", "User", "USER", null);
    }
} 