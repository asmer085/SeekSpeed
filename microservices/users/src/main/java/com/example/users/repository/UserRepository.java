package com.example.users.repository;

import com.example.users.entity.Users;
import org.springframework.data.repository.CrudRepository;
import java.util.UUID;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
public interface UserRepository extends CrudRepository<Users, UUID> {
}
