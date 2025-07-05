package com.elearningsystem.subscriptionservice.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class SecurityContextPropagationConfig {

    @PostConstruct
    public void setup() {
        // Use inheritable ThreadLocal so child threads share the parent SecurityContext
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }
} 