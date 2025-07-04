package com.elearningsystem.subscriptionservice.config;

import com.elearningsystem.subscriptionservice.model.Subscription;
import com.elearningsystem.subscriptionservice.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final SubscriptionRepository subscriptionRepository;
    
    @Override
    public void run(String... args) throws Exception {
        if (subscriptionRepository.count() == 0) {
            log.info("Initializing sample subscription data...");
            
            // Sample subscriptions
            Subscription subscription1 = Subscription.builder()
                    .userId(1L)
                    .courseId(1L)
                    .isPaid(true)
                    .isCompleted(false)
                    .enrolledAt(LocalDateTime.now().minusDays(30))
                    .build();
            
            Subscription subscription2 = Subscription.builder()
                    .userId(1L)
                    .courseId(2L)
                    .isPaid(true)
                    .isCompleted(true)
                    .enrolledAt(LocalDateTime.now().minusDays(60))
                    .build();
            
            Subscription subscription3 = Subscription.builder()
                    .userId(2L)
                    .courseId(1L)
                    .isPaid(false)
                    .isCompleted(false)
                    .enrolledAt(LocalDateTime.now().minusDays(15))
                    .build();
            
            Subscription subscription4 = Subscription.builder()
                    .userId(3L)
                    .courseId(3L)
                    .isPaid(true)
                    .isCompleted(false)
                    .enrolledAt(LocalDateTime.now().minusDays(20))
                    .build();
            
            Subscription subscription5 = Subscription.builder()
                    .userId(2L)
                    .courseId(4L)
                    .isPaid(false)
                    .isCompleted(false)
                    .enrolledAt(LocalDateTime.now().minusDays(5))
                    .build();
            
            subscriptionRepository.save(subscription1);
            subscriptionRepository.save(subscription2);
            subscriptionRepository.save(subscription3);
            subscriptionRepository.save(subscription4);
            subscriptionRepository.save(subscription5);
            
            log.info("Sample subscription data initialized successfully!");
        }
    }
} 