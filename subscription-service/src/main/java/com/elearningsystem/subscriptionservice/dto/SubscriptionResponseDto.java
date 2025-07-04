package com.elearningsystem.subscriptionservice.dto;

import com.elearningsystem.subscriptionservice.model.Subscription;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionResponseDto {
    
    private Long id;
    private Long userId;
    private Long courseId;
    private Boolean isPaid;
    private Boolean isCompleted;
    private LocalDateTime enrolledAt;
    
    public static SubscriptionResponseDto fromEntity(Subscription subscription) {
        return SubscriptionResponseDto.builder()
                .id(subscription.getId())
                .userId(subscription.getUserId())
                .courseId(subscription.getCourseId())
                .isPaid(subscription.getIsPaid())
                .isCompleted(subscription.getIsCompleted())
                .enrolledAt(subscription.getEnrolledAt())
                .build();
    }
} 