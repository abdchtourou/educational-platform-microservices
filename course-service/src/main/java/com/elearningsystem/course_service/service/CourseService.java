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
    
  
    public Course addCourse(Course course) {
        course.setStatus(Course.CourseStatus.PENDING);
        return courseRepository.save(course);
    }
    
 
    public Course changeCourseStatus(Long courseId, Course.CourseStatus newStatus) {
        return courseRepository.findById(courseId)
                .map(course -> {
                    course.setStatus(newStatus);
                    return courseRepository.save(course);
                })
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
    }
    
  
    public Course approveCourse(Long courseId) {
        return changeCourseStatus(courseId, Course.CourseStatus.APPROVED);
    }
    
  
    public Course rejectCourse(Long courseId) {
        return changeCourseStatus(courseId, Course.CourseStatus.REJECTED);
    }
    
  
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
    
  
    public List<Course> getCoursesForTrainer(String trainer) {
        return courseRepository.findByTrainer(trainer);
    }
    
  
    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }
    
    public List<Course> getCoursesByStatus(Course.CourseStatus status) {
        return courseRepository.findByStatus(status);
    }
    
  
    public UserDTO getUserById(Long userId) {
        return resilientUserService.getUserById(userId);
    }
    
  
    public UserDTO getUserByEmail(String email) {
        return resilientUserService.getUserByEmail(email);
    }
    
  
    public Course addCourseWithTrainerValidation(Course course, Long trainerId) {
        
        UserDTO trainer = resilientUserService.getUserById(trainerId);
        if (trainer == null || trainer.getId() == null) {
            throw new RuntimeException("Trainer not found with id: " + trainerId);
        }
        
        course.setTrainer(trainer.getUsername());
        course.setStatus(Course.CourseStatus.PENDING);
        
        return courseRepository.save(course);
    }
} 