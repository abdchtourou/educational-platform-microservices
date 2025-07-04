package com.elearningsystem.userservice.service;

import com.elearningsystem.userservice.config.JwtUtil;
import com.elearningsystem.userservice.dto.AuthResponse;
import com.elearningsystem.userservice.dto.LoginRequest;
import com.elearningsystem.userservice.dto.SignupRequest;
import com.elearningsystem.userservice.model.Role;
import com.elearningsystem.userservice.model.User;
import com.elearningsystem.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest loginRequest) {
        log.info("Attempting login for username: {}", loginRequest.getUsername());
        
        // Authenticate the user
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            )
        );
        
        // Get user details
        User user = userRepository.findByUsername(loginRequest.getUsername())
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + loginRequest.getUsername()));
        
        if (!user.isEnabled()) {
            throw new IllegalArgumentException("User account is disabled");
        }
        
        // Generate JWT token
        Map<String, Object> claims = Map.of("role", user.getRole().name());
        String token = jwtUtil.generateToken(claims, user);
        
        log.info("Login successful for user: {}", loginRequest.getUsername());
        
        return AuthResponse.builder()
            .token(token)
            .tokenType("Bearer")
            .userId(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .role(user.getRole())
            .enabled(user.isEnabled())
            .build();
    }

    public AuthResponse signup(SignupRequest signupRequest) {
        log.info("Attempting signup for username: {}", signupRequest.getUsername());
        
        // Check if username already exists
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + signupRequest.getUsername());
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + signupRequest.getEmail());
        }
        
        // Create new user
        User user = User.builder()
            .username(signupRequest.getUsername())
            .email(signupRequest.getEmail())
            .password(passwordEncoder.encode(signupRequest.getPassword()))
            .firstName(signupRequest.getFirstName())
            .lastName(signupRequest.getLastName())
            .role(signupRequest.getRole() != null ? signupRequest.getRole() : Role.STUDENT)
            .enabled(true)
            .build();
        
        User savedUser = userRepository.save(user);
        log.info("User created successfully with ID: {}", savedUser.getId());
        
        // Generate JWT token
        Map<String, Object> claims = Map.of("role", savedUser.getRole().name());
        String token = jwtUtil.generateToken(claims, savedUser);
        
        return AuthResponse.builder()
            .token(token)
            .tokenType("Bearer")
            .userId(savedUser.getId())
            .username(savedUser.getUsername())
            .email(savedUser.getEmail())
            .firstName(savedUser.getFirstName())
            .lastName(savedUser.getLastName())
            .role(savedUser.getRole())
            .enabled(savedUser.isEnabled())
            .build();
    }
} 