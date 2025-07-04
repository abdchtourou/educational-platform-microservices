package com.elearningsystem.examservice.controller;

import com.elearningsystem.examservice.model.Exam;
import com.elearningsystem.examservice.model.Result;
import com.elearningsystem.examservice.service.ExamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ExamController {

    private final ExamService examService;

    @GetMapping("/exams")
    public ResponseEntity<List<Exam>> getAllExams() {
        log.info("Getting all exams");
        List<Exam> exams = examService.getAllExams();
        return ResponseEntity.ok(exams);
    }

    @PostMapping("/exams")
    public ResponseEntity<Exam> createExam(@RequestBody Exam exam) {
        log.info("Creating exam for course: {}", exam.getCourseId());
        Exam createdExam = examService.createExam(exam);
        return new ResponseEntity<>(createdExam, HttpStatus.CREATED);
    }

    @GetMapping("/exams/course/{courseId}")
    public ResponseEntity<Exam> getExamByCourse(@PathVariable Long courseId) {
        log.info("Getting exam for course: {}", courseId);
        return examService.getExamByCourseId(courseId)
                .map(exam -> ResponseEntity.ok(exam))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/exams/{courseId}/submit")
    public ResponseEntity<Result> submitExam(
            @PathVariable Long courseId,
            @RequestBody Map<String, Object> submissionData) {
        
        Long userId = Long.valueOf(submissionData.get("userId").toString());
        String answers = submissionData.get("answers").toString();
        
        log.info("Submitting exam for course: {} by user: {}", courseId, userId);
        
        try {
            Result result = examService.submitExam(courseId, userId, answers);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            log.error("Error submitting exam: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/results/user/{userId}")
    public ResponseEntity<List<Result>> getResultsByUser(@PathVariable Long userId) {
        log.info("Getting results for user: {}", userId);
        List<Result> results = examService.getResultsByUserId(userId);
        return ResponseEntity.ok(results);
    }

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