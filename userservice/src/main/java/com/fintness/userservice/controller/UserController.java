package com.fintness.userservice.controller;

import com.fintness.userservice.dto.UserRequest;
import com.fintness.userservice.dto.UserResponse;
import com.fintness.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(
            @Valid
            @RequestBody UserRequest userRequest) {

        UserResponse savedUser = userService.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @GetMapping("/{id}/validate")
    public ResponseEntity<Boolean> validateUser(@PathVariable Long id) {
        Boolean user = userService.existsByUserId(id);
        return ResponseEntity.ok().body(user);
    }
}

