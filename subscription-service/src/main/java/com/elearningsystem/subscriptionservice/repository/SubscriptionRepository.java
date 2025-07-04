package com.elearningsystem.subscriptionservice.repository;

import com.elearningsystem.subscriptionservice.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    
    List<Subscription> findByUserId(Long userId);
    
    List<Subscription> findByCourseId(Long courseId);
    
    Optional<Subscription> findByUserIdAndCourseId(Long userId, Long courseId);
    
    boolean existsByUserIdAndCourseId(Long userId, Long courseId);
    
    List<Subscription> findByIsPaid(Boolean isPaid);
    
    List<Subscription> findByIsCompleted(Boolean isCompleted);
} 