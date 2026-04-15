package com.fitness.gateway;

import com.fitness.gateway.user.UserValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class KeyclockUserSyncFilter implements WebFilter {

    private final UserValidationService userValidationService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        // Skip public API
        if (path.startsWith("/api/users/register")) {
            return chain.filter(exchange);
        }

        String token = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (token == null) {
            return chain.filter(exchange);
        }

        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> (Jwt) ctx.getAuthentication().getPrincipal())
                .flatMap(jwt -> {

                    String keycloakId = jwt.getSubject();
                    String email = jwt.getClaim("email");
                    String firstName = jwt.getClaim("given_name");
                    String lastName = jwt.getClaim("family_name");

                    log.info("Processing user: {}", email);

                    return userValidationService.getOrCreateUser(
                                    keycloakId,
                                    firstName,
                                    lastName,
                                    email,
                                    token
                            )
                            // Just continue request (NO header modification)
                            .then(chain.filter(exchange));
                });
    }
}