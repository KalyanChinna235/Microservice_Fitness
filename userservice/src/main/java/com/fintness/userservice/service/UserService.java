package com.fintness.userservice.service;

import com.fintness.userservice.dto.UserRequest;
import com.fintness.userservice.dto.UserResponse;
import com.fintness.userservice.entity.User;
import com.fintness.userservice.exception.ResourceNotFoundException;
import com.fintness.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return mapToResponse(user);
    }

    public UserResponse createUser(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new ResourceNotFoundException("Email already exists");

        }
        User savedUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .keyclockId(userRequest.getKeyclockId())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .build();
        return mapToResponse(userRepository.save(savedUser));
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .keyclockId(user.getKeyclockId())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public boolean existsByKeyclockId(String keyclockId) {
        log.info("Checking if user exists with userId: {}", keyclockId);
        return userRepository.existsByKeyclockId(keyclockId);

    }

    public UserResponse getByKeycloakId(String keycloakId) {

        User user = userRepository.findByKeyclockId(keycloakId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with keycloakId: " + keycloakId)
                );

        return mapToResponse(user);
    }
}
