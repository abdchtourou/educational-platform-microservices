package com.elearningsystem.subscriptionservice.service;

import com.elearningsystem.subscriptionservice.dto.CourseDTO;
import com.elearningsystem.subscriptionservice.model.Subscription;
import com.elearningsystem.subscriptionservice.repository.SubscriptionRepository;
import com.elearningsystem.subscriptionservice.service.ResilientCourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SubscriptionService {
    
    private final SubscriptionRepository subscriptionRepository;
    private final ResilientCourseService resilientCourseService;
    
 
    public Subscription subscribeUserToCourse(Long userId, Long courseId) {
        log.info("Subscribing user {} to course {}", userId, courseId);
        
        if (subscriptionRepository.existsByUserIdAndCourseId(userId, courseId)) {
            throw new RuntimeException("User is already subscribed to this course");
        }
        
        Subscription subscription = Subscription.builder()
                .userId(userId)
                .courseId(courseId)
                .isPaid(false)
                .isCompleted(false)
                .enrolledAt(LocalDateTime.now())
                .build();
        
        return subscriptionRepository.save(subscription);
    }
    

    public List<Subscription> getUserSubscriptions(Long userId) {
        log.info("Fetching subscriptions for user: {}", userId);
        return subscriptionRepository.findByUserId(userId);
    }
    
  
    public Subscription markSubscriptionAsPaid(Long subscriptionId) {
        log.info("Marking subscription {} as paid", subscriptionId);
        
        return subscriptionRepository.findById(subscriptionId)
                .map(subscription -> {
                    subscription.setIsPaid(true);
                    return subscriptionRepository.save(subscription);
                })
                .orElseThrow(() -> new RuntimeException("Subscription not found with id: " + subscriptionId));
    }
    
   
    public Subscription markSubscriptionAsCompleted(Long subscriptionId) {
        log.info("Marking subscription {} as completed", subscriptionId);
        
        return subscriptionRepository.findById(subscriptionId)
                .map(subscription -> {
                    subscription.setIsCompleted(true);
                    return subscriptionRepository.save(subscription);
                })
                .orElseThrow(() -> new RuntimeException("Subscription not found with id: " + subscriptionId));
    }
    

    public Subscription getSubscriptionById(Long id) {
        log.info("Fetching subscription with id: {}", id);
        return subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found with id: " + id));
    }
    
   
    public List<Subscription> getAllSubscriptions() {
        log.info("Fetching all subscriptions");
        return subscriptionRepository.findAll();
    }
    

    public List<Subscription> getSubscriptionsByPaymentStatus(Boolean isPaid) {
        log.info("Fetching subscriptions with payment status: {}", isPaid);
        return subscriptionRepository.findByIsPaid(isPaid);
    }
    

    public List<Subscription> getSubscriptionsByCompletionStatus(Boolean isCompleted) {
        log.info("Fetching subscriptions with completion status: {}", isCompleted);
        return subscriptionRepository.findByIsCompleted(isCompleted);
    }
    

    public boolean isUserSubscribedToCourse(Long userId, Long courseId) {
        return subscriptionRepository.existsByUserIdAndCourseId(userId, courseId);
    }
    

    public CourseDTO fetchCourse(Long courseId) {
        log.info("Fetching course details for courseId: {}", courseId);
        try {
            CompletableFuture<CourseDTO> future = resilientCourseService.getCourseById(courseId);
            return future.get(); 
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error fetching course {}: {}", courseId, e.getMessage());
            throw new RuntimeException("Failed to fetch course details", e);
        }
    }

   
} 