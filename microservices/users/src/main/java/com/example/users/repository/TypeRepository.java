package com.example.users.repository;

import com.example.users.entity.Type;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface TypeRepository extends CrudRepository<Type, UUID> {
}
