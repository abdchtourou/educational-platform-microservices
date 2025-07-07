package com.elearningsystem.course_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "courses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(length = 1000)
    private String description;
    
    @Column(nullable = false)
    private String trainer;
    
    @Column(nullable = false)
    private Double price;
    
    @Column(nullable = false)
    private Integer duration; 
    
    @Enumerated(EnumType.STRING)
    private CourseStatus status;
    
    public enum CourseStatus {
        PENDING, APPROVED, REJECTED
    }
} 