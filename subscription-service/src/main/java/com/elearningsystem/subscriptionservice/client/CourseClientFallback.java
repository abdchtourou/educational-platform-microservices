package com.elearningsystem.subscriptionservice.client;

import com.elearningsystem.subscriptionservice.dto.CourseDTO;
import org.springframework.stereotype.Component;

@Component
public class CourseClientFallback implements CourseClient {
    @Override
    public CourseDTO getCourseById(Long id) {
        return new CourseDTO(id, "Unavailable", 0.0, null);
    }
} 