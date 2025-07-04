package com.elearningsystem.subscriptionservice.client;

import com.elearningsystem.subscriptionservice.dto.CourseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "course-service", fallback = CourseClientFallback.class)
public interface CourseClient {
    @GetMapping("/api/courses/{id}")
    CourseDTO getCourseById(@PathVariable("id") Long id);
} 