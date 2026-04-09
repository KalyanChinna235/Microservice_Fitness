package com.fitness.gateway.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserValidationService {

    private final WebClient userServiceWebClient;

    public Mono<Long> getOrCreateUser(
            String keycloakId,
            String firstName,
            String lastName,
            String email,
            String token
    ) {

        // 1️⃣ Try to get existing user
        return userServiceWebClient.get()
                .uri("/api/users/by-keycloak/{keycloakId}", keycloakId)
                .headers(h -> h.set(HttpHeaders.AUTHORIZATION, token))
                .retrieve()
                .bodyToMono(UserResponse.class)
                .map(UserResponse::getId)

                // 2️⃣ If not found → create user
                .onErrorResume(ex -> {

                    log.info("User not found, creating new user");

                    UserRequest request = new UserRequest();
                    request.setFirstName(firstName);
                    request.setLastName(lastName);
                    request.setEmail(email);
                    request.setKeyclockId(keycloakId);
                    request.setPassword("KEYCLOAK_USER");

                    return userServiceWebClient.post()
                            .uri("/api/users/register")
                            .headers(h -> h.set(HttpHeaders.AUTHORIZATION, token))
                            .bodyValue(request)
                            .retrieve()
                            .bodyToMono(UserResponse.class)
                            .map(UserResponse::getId);
                });
    }
}