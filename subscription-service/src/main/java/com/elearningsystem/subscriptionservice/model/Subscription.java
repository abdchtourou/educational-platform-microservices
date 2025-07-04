package com.elearningsystem.subscriptionservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long userId;
    
    @Column(nullable = false)
    private Long courseId;
    
    @Column(nullable = false)
    private Boolean isPaid;
    
    @Column(nullable = false)
    private Boolean isCompleted;
    
    @Column(nullable = false)
    private LocalDateTime enrolledAt;
} 