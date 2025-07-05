package com.elearningsystem.subscriptionservice.controller;

import com.elearningsystem.subscriptionservice.dto.SubscriptionRequestDto;
import com.elearningsystem.subscriptionservice.dto.SubscriptionResponseDto;
import com.elearningsystem.subscriptionservice.model.Subscription;
import com.elearningsystem.subscriptionservice.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

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
    
    /**
     * Create a new subscription
     * POST /api/subscriptions
     */
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping
    public ResponseEntity<?> createSubscription(@Valid @RequestBody SubscriptionRequestDto request) {
        log.info("POST /api/subscriptions - Creating subscription for user {} to course {}", request.getUserId(), request.getCourseId());
 
        try {
            Subscription subscription = subscriptionService.subscribeUserToCourse(
                    request.getUserId(),
                    request.getCourseId()
            );
            
            SubscriptionResponseDto response = SubscriptionResponseDto.fromEntity(subscription);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            log.error("Error creating subscription", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Get all subscriptions (Admin/Instructor only)
     * GET /api/subscriptions
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    @GetMapping
    public ResponseEntity<List<SubscriptionResponseDto>> getAllSubscriptions() {
        log.info("GET /api/subscriptions - Fetching all subscriptions");
        
        List<Subscription> subscriptions = subscriptionService.getAllSubscriptions();
        List<SubscriptionResponseDto> response = subscriptions.stream()
                .map(SubscriptionResponseDto::fromEntity)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get subscription by ID
     * GET /api/subscriptions/{id}
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionResponseDto> getSubscriptionById(@PathVariable Long id) {
        log.info("GET /api/subscriptions/{} - Fetching subscription by ID", id);
        
        try {
            Subscription subscription = subscriptionService.getSubscriptionById(id);
            SubscriptionResponseDto response = SubscriptionResponseDto.fromEntity(subscription);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error fetching subscription by ID", e);
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Get all subscriptions for a user
     * GET /api/subscriptions/user/{userId}
     */
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN') or hasRole('INSTRUCTOR')")
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
    @PreAuthorize("hasRole('STUDENT')")
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
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
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
    
    /**
     * Get subscriptions by payment status
     * GET /api/subscriptions/paid/{isPaid}
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    @GetMapping("/paid/{isPaid}")
    public ResponseEntity<List<SubscriptionResponseDto>> getSubscriptionsByPaymentStatus(@PathVariable Boolean isPaid) {
        log.info("GET /api/subscriptions/paid/{} - Fetching subscriptions by payment status", isPaid);
        
        List<Subscription> subscriptions = subscriptionService.getSubscriptionsByPaymentStatus(isPaid);
        List<SubscriptionResponseDto> response = subscriptions.stream()
                .map(SubscriptionResponseDto::fromEntity)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Check if user is subscribed to course
     * GET /api/subscriptions/user/{userId}/course/{courseId}
     */
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    @GetMapping("/user/{userId}/course/{courseId}")
    public ResponseEntity<Map<String, Boolean>> isUserSubscribedToCourse(@PathVariable Long userId, @PathVariable Long courseId) {
        log.info("GET /api/subscriptions/user/{}/course/{} - Checking if user is subscribed to course", userId, courseId);
        
        boolean isSubscribed = subscriptionService.isUserSubscribedToCourse(userId, courseId);
        return ResponseEntity.ok(Map.of("isSubscribed", isSubscribed));
    }
} 