package com.example.users.repository;

import com.example.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<Users, UUID> {
    Users findByUsername(String username);
    boolean existsByUsername (String username);
}
