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
public class CourseResponseDto {
    
    private Long id;
    private String title;
    private String description;
    private String trainer;
    private Double price;
    private Integer duration;
    private Course.CourseStatus status;
    
    public static CourseResponseDto fromEntity(Course course) {
        return CourseResponseDto.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .trainer(course.getTrainer())
                .price(course.getPrice())
                .duration(course.getDuration())
                .status(course.getStatus())
                .build();
    }
} 