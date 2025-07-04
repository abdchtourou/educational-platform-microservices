package com.elearningsystem.course_service;

import com.elearningsystem.course_service.model.Course;
import com.elearningsystem.course_service.repository.CourseRepository;
import com.elearningsystem.course_service.service.CourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class CourseServiceApplicationTests {

	@Autowired
	private CourseService courseService;
	
	@Autowired
	private CourseRepository courseRepository;

	@Test
	void contextLoads() {
		assertThat(courseService).isNotNull();
		assertThat(courseRepository).isNotNull();
	}
	
	@Test
	void testAddCourse() {
		Course course = Course.builder()
				.title("Test Course")
				.description("Test Description")
				.trainer("test.trainer")
				.price(50.0)
				.duration(20)
				.build();
		
		Course savedCourse = courseService.addCourse(course);
		
		assertThat(savedCourse).isNotNull();
		assertThat(savedCourse.getId()).isNotNull();
		assertThat(savedCourse.getTitle()).isEqualTo("Test Course");
		assertThat(savedCourse.getStatus()).isEqualTo(Course.CourseStatus.PENDING);
	}
	
	@Test
	void testApproveCourse() {
		Course course = Course.builder()
				.title("Test Course for Approval")
				.description("Test Description")
				.trainer("test.trainer")
				.price(75.0)
				.duration(30)
				.build();
		
		Course savedCourse = courseService.addCourse(course);
		Course approvedCourse = courseService.approveCourse(savedCourse.getId());
		
		assertThat(approvedCourse).isNotNull();
		assertThat(approvedCourse.getStatus()).isEqualTo(Course.CourseStatus.APPROVED);
	}

}
