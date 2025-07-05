package com.elearningsystem.examservice.controller;

import com.elearningsystem.examservice.dto.ExamSubmissionRequest;
import com.elearningsystem.examservice.model.Exam;
import com.elearningsystem.examservice.model.Result;
import com.elearningsystem.examservice.service.ExamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ExamController {

    private final ExamService examService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    @GetMapping("/exams")
    public ResponseEntity<List<Exam>> getAllExams() {
        log.info("Getting all exams");
        List<Exam> exams = examService.getAllExams();
        return ResponseEntity.ok(exams);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    @PostMapping("/exams")
    public ResponseEntity<Exam> createExam(@RequestBody Exam exam) {
        log.info("Creating exam for course: {}", exam.getCourseId());
        Exam createdExam = examService.createExam(exam);
        return new ResponseEntity<>(createdExam, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    @GetMapping("/exams/course/{courseId}")
    public ResponseEntity<Exam> getExamByCourse(@PathVariable Long courseId) {
        log.info("Getting exam for course: {}", courseId);
        return examService.getExamByCourseId(courseId)
                .map(exam -> ResponseEntity.ok(exam))
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/exams/submit")
    public ResponseEntity<?> submitExam(@RequestBody ExamSubmissionRequest request) {
        
        log.info("Submitting exam for course: {} by user: {}", request.getCourseId(), request.getUserId());
        
        // Validate request
        if (request.getCourseId() == null || request.getUserId() == null || request.getAnswers() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing required fields: courseId, userId, or answers"));
        }
        
        try {
            Result result = examService.submitExam(request.getCourseId(), request.getUserId(), request.getAnswers());
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            log.error("Error submitting exam: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    @GetMapping("/results/user/{userId}")
    public ResponseEntity<List<Result>> getResultsByUser(@PathVariable Long userId) {
        log.info("Getting results for user: {}", userId);
        List<Result> results = examService.getResultsByUserId(userId);
        return ResponseEntity.ok(results);
    }

    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    @GetMapping("/results/user/{userId}/course/{courseId}")
    public ResponseEntity<Result> getResultByUserAndCourse(
            @PathVariable Long userId,
            @PathVariable Long courseId) {
        
        log.info("Getting result for user: {} and course: {}", userId, courseId);
        return examService.getResultByUserIdAndCourseId(userId, courseId)
                .map(result -> ResponseEntity.ok(result))
                .orElse(ResponseEntity.notFound().build());
    }
} 