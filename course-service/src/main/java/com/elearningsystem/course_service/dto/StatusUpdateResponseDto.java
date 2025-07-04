package com.elearningsystem.course_service.dto;

import com.elearningsystem.course_service.model.Course;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusUpdateResponseDto {
    
    private Long courseId;
    private String title;
    private Course.CourseStatus previousStatus;
    private Course.CourseStatus newStatus;
    private String message;
    
    public static StatusUpdateResponseDto fromCourse(Course course, Course.CourseStatus previousStatus) {
        return StatusUpdateResponseDto.builder()
                .courseId(course.getId())
                .title(course.getTitle())
                .previousStatus(previousStatus)
                .newStatus(course.getStatus())
                .message("Course status updated successfully")
                .build();
    }
} 