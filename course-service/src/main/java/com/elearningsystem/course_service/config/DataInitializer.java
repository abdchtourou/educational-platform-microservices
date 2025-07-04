package com.elearningsystem.course_service.config;

import com.elearningsystem.course_service.model.Course;
import com.elearningsystem.course_service.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(CourseRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                log.info("Initializing sample course data...");
                
                Course course1 = Course.builder()
                        .title("Introduction to Spring Boot")
                        .description("Learn the fundamentals of Spring Boot framework")
                        .trainer("john.doe")
                        .price(99.99)
                        .duration(40)
                        .status(Course.CourseStatus.APPROVED)
                        .build();
                
                Course course2 = Course.builder()
                        .title("Advanced Java Programming")
                        .description("Master advanced Java concepts and best practices")
                        .trainer("jane.smith")
                        .price(149.99)
                        .duration(60)
                        .status(Course.CourseStatus.APPROVED)
                        .build();
                
                Course course3 = Course.builder()
                        .title("Microservices Architecture")
                        .description("Design and implement microservices using Spring Cloud")
                        .trainer("bob.johnson")
                        .price(199.99)
                        .duration(80)
                        .status(Course.CourseStatus.PENDING)
                        .build();
                
                Course course4 = Course.builder()
                        .title("React Fundamentals")
                        .description("Learn React.js from scratch")
                        .trainer("alice.brown")
                        .price(89.99)
                        .duration(35)
                        .status(Course.CourseStatus.REJECTED)
                        .build();
                
                repository.save(course1);
                repository.save(course2);
                repository.save(course3);
                repository.save(course4);
                
                log.info("Sample course data initialized successfully!");
            }
        };
    }
} 