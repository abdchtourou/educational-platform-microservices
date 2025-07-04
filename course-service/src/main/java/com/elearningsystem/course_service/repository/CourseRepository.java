package com.elearningsystem.course_service.repository;

import com.elearningsystem.course_service.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    List<Course> findByTrainer(String trainer);
    
    List<Course> findByStatus(Course.CourseStatus status);
} 