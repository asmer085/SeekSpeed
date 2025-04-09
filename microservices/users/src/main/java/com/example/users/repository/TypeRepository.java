package com.example.users.repository;

import com.example.users.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TypeRepository extends JpaRepository<Type, UUID> {
}
