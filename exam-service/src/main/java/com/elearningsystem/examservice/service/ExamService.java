package com.elearningsystem.examservice.service;

import com.elearningsystem.examservice.dto.SubscriptionDTO;
import com.elearningsystem.examservice.model.Exam;
import com.elearningsystem.examservice.model.Result;
import com.elearningsystem.examservice.repository.ExamRepository;
import com.elearningsystem.examservice.repository.ResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExamService {

    private final ExamRepository examRepository;
    private final ResultRepository resultRepository;
    private final ResilientSubscriptionService resilientSubscriptionService;

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
        
        // Check if user is subscribed to the course using resilient service
        try {
            Map<String, Boolean> subscriptionResponse = resilientSubscriptionService.isUserSubscribedToCourse(userId, courseId);
            Boolean isSubscribed = subscriptionResponse != null ? subscriptionResponse.get("isSubscribed") : false;
            System.out.println("Checking subscription for user: " + userId + " and course: " + courseId);
            System.out.println("Subscription response: " + subscriptionResponse);
            if (isSubscribed == null || !isSubscribed) {
                log.error("User {} is not subscribed to course {}", userId, courseId);
                throw new RuntimeException("User is not subscribed to this course. Please subscribe first to take the exam.");
            } else {
                log.info("User {} is subscribed to course {}, proceeding with exam submission", userId, courseId);
            }
        } catch (Exception e) {
            log.error("Error checking subscription for user {} and course {}: {}", userId, courseId, e.getMessage());
            throw new RuntimeException("Unable to verify subscription status. Please try again later or contact support.");
        }
        
        // Get the exam
        log.info("Looking for exam with courseId: {}", courseId);
        Optional<Exam> examOptional = examRepository.findByCourseId(courseId);
        if (examOptional.isEmpty()) {
            log.error("No exam found for course: {}", courseId);
            throw new RuntimeException("Exam not found for course: " + courseId + ". Please make sure an exam exists for this course.");
        }
        Exam exam = examOptional.get();
        log.info("Found exam: {} for course: {}", exam.getTitle(), courseId);

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
     * Get user subscriptions with resilience patterns
     */
    public SubscriptionDTO[] getUserSubscriptions(Long userId) {
        log.info("Fetching subscriptions for user: {}", userId);
        return resilientSubscriptionService.getUserSubscriptions(userId);
    }
    
    /**
     * Check if user is subscribed to course with resilience patterns
     */
    public Boolean isUserSubscribedToCourse(Long userId, Long courseId) {
        log.info("Checking if user {} is subscribed to course {}", userId, courseId);
        Map<String, Boolean> subscriptionResponse = resilientSubscriptionService.isUserSubscribedToCourse(userId, courseId);
        return subscriptionResponse.get("isSubscribed");
    }

    private Double calculateScore(String questions, String answers) {
    
        return 70.0 + (Math.random() * 30);
    }
} 