package com.elearningsystem.examservice.client;

import com.elearningsystem.examservice.dto.SubscriptionDTO;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionClientFallback implements SubscriptionClient {
    @Override
    public SubscriptionDTO[] getUserSubscriptions(Long userId) {
        return new SubscriptionDTO[0]; 
    }

    @Override
    public SubscriptionDTO getSubscriptionById(Long id) {
        SubscriptionDTO dto = new SubscriptionDTO();
        dto.setId(id);
        dto.setUserId(null);
        dto.setCourseId(null);
        dto.setIsPaid(false);
        dto.setIsCompleted(false);
        dto.setEnrolledAt(null);
        return dto;
    }

    @Override
    public java.util.Map<String, Boolean> isUserSubscribedToCourse(Long userId, Long courseId) {
        return java.util.Map.of("isSubscribed", false); 
    }
} 