package com.elearningsystem.course_service.controller;

import com.elearningsystem.course_service.config.JwtUtil;
import com.elearningsystem.course_service.dto.CourseRequestDto;
import com.elearningsystem.course_service.dto.CourseResponseDto;
import com.elearningsystem.course_service.dto.StatusUpdateResponseDto;
import com.elearningsystem.course_service.model.Course;
import com.elearningsystem.course_service.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class CourseController {
    
    private final CourseService courseService;
    private final JwtUtil jwtUtil;
    
    
    @PostMapping
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
    public ResponseEntity<?> addCourse(@Valid @RequestBody CourseRequestDto courseRequest,
                                      @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        String instructor;
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Authorization header is required"));
            }
            
            String token = authHeader.substring(7); 
            
            if (!jwtUtil.isTokenValid(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid or expired token"));
            }
            
            instructor = jwtUtil.extractUsername(token);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid token: " + e.getMessage()));
        }
        
    Course course = Course.builder()
                .title(courseRequest.getTitle())
                .description(courseRequest.getDescription())
                .trainer(instructor) 
                .price(courseRequest.getPrice())
                .duration(courseRequest.getDuration())
                .build();
        
        Course savedCourse = courseService.addCourse(course);
        CourseResponseDto response = CourseResponseDto.fromEntity(savedCourse);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StatusUpdateResponseDto> approveCourse(@PathVariable Long id) {
        
        try {
            Course originalCourse = courseService.getCourseById(id)
                    .orElseThrow(() -> new RuntimeException("Course not found"));
            Course.CourseStatus previousStatus = originalCourse.getStatus();
            
            Course approvedCourse = courseService.approveCourse(id);
            StatusUpdateResponseDto response = StatusUpdateResponseDto.fromCourse(approvedCourse, previousStatus);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error approving course with id: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }
    
    
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StatusUpdateResponseDto> rejectCourse(@PathVariable Long id) {
        log.info("PUT /api/courses/{}/reject - Rejecting course", id);
        
        try {
            Course originalCourse = courseService.getCourseById(id)
                    .orElseThrow(() -> new RuntimeException("Course not found"));
            Course.CourseStatus previousStatus = originalCourse.getStatus();
            
            Course rejectedCourse = courseService.rejectCourse(id);
            StatusUpdateResponseDto response = StatusUpdateResponseDto.fromCourse(rejectedCourse, previousStatus);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error rejecting course with id: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<CourseResponseDto>> getAllApprovedCourses() {
        
        List<Course> approvedCourses = courseService.getCoursesByStatus(Course.CourseStatus.APPROVED);
        List<CourseResponseDto> response = approvedCourses.stream()
                .map(CourseResponseDto::fromEntity)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
    
      
    @GetMapping("/trainer/{trainerId}")
    public ResponseEntity<List<CourseResponseDto>> getCoursesByTrainer(@PathVariable String trainerId) {
        
        List<Course> trainerCourses = courseService.getCoursesForTrainer(trainerId);
        List<CourseResponseDto> response = trainerCourses.stream()
                .map(CourseResponseDto::fromEntity)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
    
    
    @GetMapping("/trainer")
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
    public ResponseEntity<?> getCoursesForCurrentTrainer(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Authorization header with Bearer token is required"));
        }

        String token = authHeader.substring(7); 
        if (!jwtUtil.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid or expired token"));
        }

        String trainerUsername = jwtUtil.extractUsername(token);

        List<Course> trainerCourses = courseService.getCoursesForTrainer(trainerUsername);
        List<CourseResponseDto> response = trainerCourses.stream()
                .map(CourseResponseDto::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
    

    @GetMapping("/all")
    public ResponseEntity<List<CourseResponseDto>> getAllCourses() {
        log.info("GET /api/courses/all - Fetching all courses (admin view)");
        
        List<Course> allCourses = courseService.getAllCourses();
        List<CourseResponseDto> response = allCourses.stream()
                .map(CourseResponseDto::fromEntity)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDto> getCourseById(@PathVariable Long id) {
        log.info("GET /api/courses/{} - Fetching course by id", id);
        
        return courseService.getCourseById(id)
                .map(course -> ResponseEntity.ok(CourseResponseDto.fromEntity(course)))
                .orElse(ResponseEntity.notFound().build());
    }
    
  
    @GetMapping("/status/{status}")
    public ResponseEntity<List<CourseResponseDto>> getCoursesByStatus(@PathVariable Course.CourseStatus status) {
        
        List<Course> courses = courseService.getCoursesByStatus(status);
        List<CourseResponseDto> response = courses.stream()
                .map(CourseResponseDto::fromEntity)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
} 