package com.elearningsystem.subscriptionservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.elearningsystem.subscriptionservice.repository")
@EnableTransactionManagement
public class DatabaseConfig {
    // Database configuration can be extended here if needed
} 