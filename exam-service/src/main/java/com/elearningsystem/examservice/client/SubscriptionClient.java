package com.elearningsystem.examservice.client;

import com.elearningsystem.examservice.dto.SubscriptionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "subscription-service", fallback = SubscriptionClientFallback.class)
public interface SubscriptionClient {
    @GetMapping("/api/subscriptions/user/{userId}")
    SubscriptionDTO[] getUserSubscriptions(@PathVariable("userId") Long userId);
    
    @GetMapping("/api/subscriptions/{id}")
    SubscriptionDTO getSubscriptionById(@PathVariable("id") Long id);
    
    @GetMapping("/api/subscriptions/user/{userId}/course/{courseId}")
    Boolean isUserSubscribedToCourse(@PathVariable("userId") Long userId, @PathVariable("courseId") Long courseId);
} 