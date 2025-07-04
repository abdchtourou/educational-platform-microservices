package com.elearningsystem.examservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDTO {
    private Long id;
    private Long userId;
    private Long courseId;
    private Boolean isPaid;
    private Boolean isCompleted;
    private LocalDateTime enrolledAt;
} 