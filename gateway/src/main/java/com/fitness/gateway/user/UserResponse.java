package com.fitness.gateway.user;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String keyclockId;
    private String email;
    //private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
