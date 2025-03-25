package com.example.users.repository;

import com.example.users.entity.UserEvent;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserEventRepository extends CrudRepository<UserEvent, UUID> {
}
