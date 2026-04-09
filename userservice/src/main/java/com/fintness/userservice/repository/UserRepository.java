package com.fintness.userservice.repository;

import com.fintness.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    Boolean existsByKeyclockId(String id);

    boolean existsByEmail(String email);

    Optional<User> findByKeyclockId(String keycloakId);
}
