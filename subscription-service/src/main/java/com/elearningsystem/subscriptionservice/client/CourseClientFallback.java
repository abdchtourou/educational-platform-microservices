package com.elearningsystem.subscriptionservice.client;

import com.elearningsystem.subscriptionservice.dto.CourseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CourseClientFallback implements CourseClient {
    @Override
    public CourseDTO getCourseById(Long id) {
        log.error("CourseClientFallback: Fallback triggered for course ID: {}", id);
        return new CourseDTO(id, "Unavailable", 0.0, null);
    }
} 