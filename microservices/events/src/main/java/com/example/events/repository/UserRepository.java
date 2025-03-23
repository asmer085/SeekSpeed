package com.example.events.repository;

import com.example.events.entity.User;
import org.springframework.data.repository.CrudRepository;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {
}