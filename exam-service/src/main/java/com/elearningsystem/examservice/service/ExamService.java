package com.elearningsystem.examservice.service;

import com.elearningsystem.examservice.client.SubscriptionClient;
import com.elearningsystem.examservice.dto.SubscriptionDTO;
import com.elearningsystem.examservice.model.Exam;
import com.elearningsystem.examservice.model.Result;
import com.elearningsystem.examservice.repository.ExamRepository;
import com.elearningsystem.examservice.repository.ResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExamService {

    private final ExamRepository examRepository;
    private final ResultRepository resultRepository;
    private final SubscriptionClient subscriptionClient;

    public List<Exam> getAllExams() {
        log.info("Getting all exams");
        return examRepository.findAll();
    }

    public Exam createExam(Exam exam) {
        log.info("Creating exam for course: {}", exam.getCourseId());
        return examRepository.save(exam);
    }

    public Optional<Exam> getExamByCourseId(Long courseId) {
        log.info("Getting exam for course: {}", courseId);
        return examRepository.findByCourseId(courseId);
    }

    public Result submitExam(Long courseId, Long userId, String answers) {
        log.info("Submitting exam for course: {} by user: {}", courseId, userId);
        
        // Check if user is subscribed to the course
        Boolean isSubscribed = subscriptionClient.isUserSubscribedToCourse(userId, courseId);
        if (!isSubscribed) {
            throw new RuntimeException("User is not subscribed to this course");
        }
        
        // Get the exam
        Exam exam = examRepository.findByCourseId(courseId)
                .orElseThrow(() -> new RuntimeException("Exam not found for course: " + courseId));

        // Calculate score (simplified logic - in real scenario, you'd parse JSON and calculate)
        Double score = calculateScore(exam.getQuestions(), answers);
        Boolean isPassed = score >= 60.0; // Pass threshold is 60%

        // Create and save result
        Result result = Result.builder()
                .userId(userId)
                .courseId(courseId)
                .score(score)
                .isPassed(isPassed)
                .build();

        return resultRepository.save(result);
    }

    public List<Result> getResultsByUserId(Long userId) {
        log.info("Getting results for user: {}", userId);
        return resultRepository.findByUserId(userId);
    }

    public Optional<Result> getResultByUserIdAndCourseId(Long userId, Long courseId) {
        log.info("Getting result for user: {} and course: {}", userId, courseId);
        return resultRepository.findByUserIdAndCourseId(userId, courseId);
    }

    /**
     * Get user subscriptions
     */
    public SubscriptionDTO[] getUserSubscriptions(Long userId) {
        log.info("Fetching subscriptions for user: {}", userId);
        return subscriptionClient.getUserSubscriptions(userId);
    }
    
    /**
     * Check if user is subscribed to course
     */
    public Boolean isUserSubscribedToCourse(Long userId, Long courseId) {
        log.info("Checking if user {} is subscribed to course {}", userId, courseId);
        return subscriptionClient.isUserSubscribedToCourse(userId, courseId);
    }

    private Double calculateScore(String questions, String answers) {
        // Simplified scoring logic - in real scenario, you'd parse JSON
        // and compare correct answers with submitted answers
        // For now, return a random score between 40-100
        return 70.0 + (Math.random() * 30);
    }
} 