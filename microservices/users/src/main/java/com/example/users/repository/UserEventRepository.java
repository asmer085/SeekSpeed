package com.example.users.repository;

import com.example.users.entity.UserEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserEventRepository extends JpaRepository<UserEvent, UUID> {
}
