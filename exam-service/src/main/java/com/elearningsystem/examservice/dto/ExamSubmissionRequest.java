package com.elearningsystem.examservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamSubmissionRequest {
    @JsonProperty("courseId")
    private Long courseId;
    
    @JsonProperty("userId")
    private Long userId;
    
    @JsonProperty("answers")
    private String answers;
    
    // Custom setter to handle string to Long conversion
    public void setCourseId(Object courseId) {
        if (courseId instanceof String) {
            this.courseId = Long.valueOf((String) courseId);
        } else if (courseId instanceof Number) {
            this.courseId = ((Number) courseId).longValue();
        } else {
            this.courseId = (Long) courseId;
        }
    }
    
    // Custom setter to handle string to Long conversion
    public void setUserId(Object userId) {
        if (userId instanceof String) {
            this.userId = Long.valueOf((String) userId);
        } else if (userId instanceof Number) {
            this.userId = ((Number) userId).longValue();
        } else {
            this.userId = (Long) userId;
        }
    }
} 