package com.elearningsystem.course_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.elearningsystem.course_service.repository")
@EnableTransactionManagement
public class DatabaseConfig {
    // Additional database configuration can be added here if needed
} 