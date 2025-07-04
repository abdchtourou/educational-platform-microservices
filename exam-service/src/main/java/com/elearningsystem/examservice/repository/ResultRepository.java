package com.elearningsystem.examservice.repository;

import com.elearningsystem.examservice.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    
    List<Result> findByUserId(Long userId);
    
    Optional<Result> findByUserIdAndCourseId(Long userId, Long courseId);
    
} 
