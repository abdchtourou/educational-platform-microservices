package com.elearningsystem.subscriptionservice.controller;

import com.elearningsystem.subscriptionservice.dto.SubscriptionRequestDto;
import com.elearningsystem.subscriptionservice.dto.SubscriptionResponseDto;
import com.elearningsystem.subscriptionservice.model.Subscription;
import com.elearningsystem.subscriptionservice.service.SubscriptionService;
import com.elearningsystem.subscriptionservice.config.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestHeader;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class SubscriptionController {
    
    private final SubscriptionService subscriptionService;
    private final JwtUtil jwtUtil;
    
    /**
     * Create a new subscription
     * POST /api/subscriptions
     */
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping
    public ResponseEntity<?> createSubscription(@Valid @RequestBody SubscriptionRequestDto request,
                                                @RequestHeader(value = "Authorization", required = false) String authHeader) {
        log.info("POST /api/subscriptions - Creating subscription for user {} to course {}", request.getUserId(), request.getCourseId());

        // Validate Authorization header
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Authorization header with Bearer token is required"));
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid or expired token"));
        }

        String usernameFromToken = jwtUtil.extractUsername(token);
        // Optional: Ensure the userId matches the token's subject via user-service if needed
 
        try {
            Subscription subscription = subscriptionService.subscribeUserToCourse(
                    request.getUserId(),
                    request.getCourseId()
            );
            
            SubscriptionResponseDto response = SubscriptionResponseDto.fromEntity(subscription);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            log.error("Error creating subscription", e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get all subscriptions for a user
     * GET /api/subscriptions/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SubscriptionResponseDto>> getUserSubscriptions(@PathVariable Long userId) {
        log.info("GET /api/subscriptions/user/{} - Fetching user subscriptions", userId);
        
        List<Subscription> subscriptions = subscriptionService.getUserSubscriptions(userId);
        List<SubscriptionResponseDto> response = subscriptions.stream()
                .map(SubscriptionResponseDto::fromEntity)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Mark subscription as paid
     * PUT /api/subscriptions/{id}/pay
     */
    @PutMapping("/{id}/pay")
    public ResponseEntity<SubscriptionResponseDto> markSubscriptionAsPaid(@PathVariable Long id) {
        log.info("PUT /api/subscriptions/{}/pay - Marking subscription as paid", id);
        
        try {
            Subscription subscription = subscriptionService.markSubscriptionAsPaid(id);
            SubscriptionResponseDto response = SubscriptionResponseDto.fromEntity(subscription);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error marking subscription as paid", e);
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Mark subscription as completed
     * PUT /api/subscriptions/{id}/complete
     */
    @PutMapping("/{id}/complete")
    public ResponseEntity<SubscriptionResponseDto> markSubscriptionAsCompleted(@PathVariable Long id) {
        log.info("PUT /api/subscriptions/{}/complete - Marking subscription as completed", id);
        
        try {
            Subscription subscription = subscriptionService.markSubscriptionAsCompleted(id);
            SubscriptionResponseDto response = SubscriptionResponseDto.fromEntity(subscription);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error marking subscription as completed", e);
            return ResponseEntity.notFound().build();
        }
    }
} 