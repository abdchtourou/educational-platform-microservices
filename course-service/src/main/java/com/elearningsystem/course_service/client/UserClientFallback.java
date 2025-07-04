package com.elearningsystem.course_service.client;

import com.elearningsystem.course_service.dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserClientFallback implements UserClient {
    @Override
    public UserDTO getUserById(Long id) {
        return new UserDTO(id, "unavailable", "unavailable@example.com", "Unknown", "User", "USER", null);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        return new UserDTO(null, "unavailable", email, "Unknown", "User", "USER", null);
    }
} 