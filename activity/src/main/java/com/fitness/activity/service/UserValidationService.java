package com.fitness.activity.service;

import com.fitness.activity.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
@Service
@RequiredArgsConstructor
@Slf4j
public class UserValidationService {

    private final WebClient userServiceWebClient;

    public Long getUserIdByKeycloakId(String keycloakId) {

        try {
            return userServiceWebClient.get()
                    .uri("/api/users/by-keycloak/{keycloakId}", keycloakId)
                    .headers(headers -> headers.setBearerAuth(getToken())) // ✅ FIX
                    .retrieve()
                    .bodyToMono(UserResponse.class)
                    .map(UserResponse::getId)
                    .block();

        } catch (WebClientResponseException e) {

            log.error("User service error: {}", e.getResponseBodyAsString());

            throw new RuntimeException("User service error");
        }
    }

    // Extract token from SecurityContext
    private String getToken() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        return jwt.getTokenValue();
    }
}