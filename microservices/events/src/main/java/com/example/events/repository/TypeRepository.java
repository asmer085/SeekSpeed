package com.example.events.repository;

import com.example.events.entity.Event;
import com.example.events.entity.Type;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.UUID;

public interface TypeRepository extends CrudRepository<Type, UUID> {
    // Find all types for a specific event
    List<Type> findByEvent(Event event);
}