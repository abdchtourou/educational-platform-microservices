package com.elearningsystem.examservice.client;

import com.elearningsystem.examservice.dto.SubscriptionDTO;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionClientFallback implements SubscriptionClient {
    @Override
    public SubscriptionDTO[] getUserSubscriptions(Long userId) {
        return new SubscriptionDTO[0]; // Return empty array when service is down
    }

    @Override
    public SubscriptionDTO getSubscriptionById(Long id) {
        return new SubscriptionDTO(id, null, null, false, false, null);
    }

    @Override
    public Boolean isUserSubscribedToCourse(Long userId, Long courseId) {
        return false; // Default to not subscribed when service is down
    }
} 