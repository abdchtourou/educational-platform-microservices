package com.elearningsystem.course_service.service;

import com.elearningsystem.course_service.dto.UserDTO;
import com.elearningsystem.course_service.model.Course;
import com.elearningsystem.course_service.repository.CourseRepository;
import com.elearningsystem.course_service.service.ResilientUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CourseService {
    
    private final CourseRepository courseRepository;
    private final ResilientUserService resilientUserService;
    
    /**
     * Add a new course with default status: PENDING
     */
    public Course addCourse(Course course) {
        log.info("Adding new course: {}", course.getTitle());
        course.setStatus(Course.CourseStatus.PENDING);
        return courseRepository.save(course);
    }
    
    /**
     * Change course status (Approve/Reject)
     */
    public Course changeCourseStatus(Long courseId, Course.CourseStatus newStatus) {
        log.info("Changing status of course with id: {} to {}", courseId, newStatus);
        return courseRepository.findById(courseId)
                .map(course -> {
                    course.setStatus(newStatus);
                    return courseRepository.save(course);
                })
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
    }
    
    /**
     * Approve a course
     */
    public Course approveCourse(Long courseId) {
        return changeCourseStatus(courseId, Course.CourseStatus.APPROVED);
    }
    
    /**
     * Reject a course
     */
    public Course rejectCourse(Long courseId) {
        return changeCourseStatus(courseId, Course.CourseStatus.REJECTED);
    }
    
    /**
     * Get all courses
     */
    public List<Course> getAllCourses() {
        log.info("Fetching all courses");
        return courseRepository.findAll();
    }
    
    /**
     * Get courses for a specific trainer
     */
    public List<Course> getCoursesForTrainer(String trainer) {
        log.info("Fetching courses for trainer: {}", trainer);
        return courseRepository.findByTrainer(trainer);
    }
    
    // Additional helper methods
    public Optional<Course> getCourseById(Long id) {
        log.info("Fetching course with id: {}", id);
        return courseRepository.findById(id);
    }
    
    public List<Course> getCoursesByStatus(Course.CourseStatus status) {
        log.info("Fetching courses with status: {}", status);
        return courseRepository.findByStatus(status);
    }
    
    /**
     * Get user details by ID
     */
    public UserDTO getUserById(Long userId) {
        log.info("Fetching user details for userId: {}", userId);
        return resilientUserService.getUserById(userId);
    }
    
    /**
     * Get user details by email
     */
    public UserDTO getUserByEmail(String email) {
        log.info("Fetching user details for email: {}", email);
        return resilientUserService.getUserByEmail(email);
    }
    
    /**
     * Validate trainer exists before creating course
     */
    public Course addCourseWithTrainerValidation(Course course, Long trainerId) {
        log.info("Adding new course with trainer validation: {}", course.getTitle());
        
        // Validate trainer exists
        UserDTO trainer = resilientUserService.getUserById(trainerId);
        if (trainer == null || trainer.getId() == null) {
            throw new RuntimeException("Trainer not found with id: " + trainerId);
        }
        
        // Set trainer name in course
        course.setTrainer(trainer.getUsername());
        course.setStatus(Course.CourseStatus.PENDING);
        
        return courseRepository.save(course);
    }
} 