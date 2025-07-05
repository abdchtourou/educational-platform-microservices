package com.elearningsystem.subscriptionservice.service;

import com.elearningsystem.subscriptionservice.client.CourseClient;
import com.elearningsystem.subscriptionservice.dto.CourseDTO;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResilientCourseService {

    private final CourseClient courseClient;

    @CircuitBreaker(name = "course-service", fallbackMethod = "fallbackGetCourseById")
    @Retry(name = "course-service")
    @TimeLimiter(name = "course-service")
    @Bulkhead(name = "course-service")
    public CompletableFuture<CourseDTO> getCourseById(Long id) {
        // Capture the current SecurityContext
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        
        log.info("Attempting to fetch course {} from course-service", id);
        log.debug("Current authentication: {}", authentication != null ? authentication.getName() : "null");
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Set the SecurityContext in the new thread
                SecurityContextHolder.setContext(securityContext);
                
                CourseDTO course = courseClient.getCourseById(id);
                log.info("Successfully fetched course: {}", course);
                return course;
            } catch (Exception e) {
                log.error("Error fetching course {}: {}", id, e.getMessage());
                log.error("Full stack trace:", e);
                throw new RuntimeException("Failed to fetch course", e);
            } finally {
                // Clean up the SecurityContext
                SecurityContextHolder.clearContext();
            }
        });
    }

    // Fallback method for async calls
    public CompletableFuture<CourseDTO> fallbackGetCourseById(Long id, Exception ex) {
        log.error("Fallback triggered for course {}", id);
        log.error("Fallback reason: {}", ex.getMessage());
        log.error("Exception type: {}", ex.getClass().getName());
        log.error("Full stack trace:", ex);
        return CompletableFuture.completedFuture(new CourseDTO(id, "Unavailable", 0.0, null));
    }
} 