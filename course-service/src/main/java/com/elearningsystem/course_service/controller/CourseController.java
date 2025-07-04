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
    
    /**
     * POST /api/courses: add a new course (by trainer)
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
    public ResponseEntity<?> addCourse(@Valid @RequestBody CourseRequestDto courseRequest,
                                      @RequestHeader(value = "Authorization", required = false) String authHeader) {
        log.info("POST /api/courses - Adding new course: {}", courseRequest.getTitle());
        
        // Extract instructor from JWT token
        String instructor;
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("Missing or invalid Authorization header");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Authorization header is required"));
            }
            
            String token = authHeader.substring(7); // Remove "Bearer " prefix
            log.debug("Extracted token: {}", token.substring(0, Math.min(token.length(), 20)) + "...");
            
            if (!jwtUtil.isTokenValid(token)) {
                log.warn("Token validation failed");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid or expired token"));
            }
            
            instructor = jwtUtil.extractUsername(token);
            log.info("Course creation requested by instructor: {}", instructor);
            
        } catch (Exception e) {
            log.error("Error processing JWT token: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid token: " + e.getMessage()));
        }
        
        Course course = Course.builder()
                .title(courseRequest.getTitle())
                .description(courseRequest.getDescription())
                .trainer(instructor) // Use instructor from JWT token
                .price(courseRequest.getPrice())
                .duration(courseRequest.getDuration())
                .build();
        
        Course savedCourse = courseService.addCourse(course);
        CourseResponseDto response = CourseResponseDto.fromEntity(savedCourse);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * PUT /api/courses/{id}/approve: approve a course (by admin)
     */
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StatusUpdateResponseDto> approveCourse(@PathVariable Long id) {
        log.info("PUT /api/courses/{}/approve - Approving course", id);
        
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
    
    /**
     * PUT /api/courses/{id}/reject: reject a course
     */
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
    
    /**
     * GET /api/courses: get all approved courses (for users)
     */
    @GetMapping
    public ResponseEntity<List<CourseResponseDto>> getAllApprovedCourses() {
        log.info("GET /api/courses - Fetching all approved courses");
        
        List<Course> approvedCourses = courseService.getCoursesByStatus(Course.CourseStatus.APPROVED);
        List<CourseResponseDto> response = approvedCourses.stream()
                .map(CourseResponseDto::fromEntity)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * GET /api/courses/trainer/{trainerId}: get all courses created by a trainer
     */
    @GetMapping("/trainer/{trainerId}")
    public ResponseEntity<List<CourseResponseDto>> getCoursesByTrainer(@PathVariable String trainerId) {
        log.info("GET /api/courses/trainer/{} - Fetching courses for trainer", trainerId);
        
        List<Course> trainerCourses = courseService.getCoursesForTrainer(trainerId);
        List<CourseResponseDto> response = trainerCourses.stream()
                .map(CourseResponseDto::fromEntity)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
    
    // Additional utility endpoints
    
    /**
     * GET /api/courses/all: get all courses (admin view)
     */
    @GetMapping("/all")
    public ResponseEntity<List<CourseResponseDto>> getAllCourses() {
        log.info("GET /api/courses/all - Fetching all courses (admin view)");
        
        List<Course> allCourses = courseService.getAllCourses();
        List<CourseResponseDto> response = allCourses.stream()
                .map(CourseResponseDto::fromEntity)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * GET /api/courses/{id}: get course by id
     */
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDto> getCourseById(@PathVariable Long id) {
        log.info("GET /api/courses/{} - Fetching course by id", id);
        
        return courseService.getCourseById(id)
                .map(course -> ResponseEntity.ok(CourseResponseDto.fromEntity(course)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * GET /api/courses/status/{status}: get courses by status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<CourseResponseDto>> getCoursesByStatus(@PathVariable Course.CourseStatus status) {
        log.info("GET /api/courses/status/{} - Fetching courses by status", status);
        
        List<Course> courses = courseService.getCoursesByStatus(status);
        List<CourseResponseDto> response = courses.stream()
                .map(CourseResponseDto::fromEntity)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
} 