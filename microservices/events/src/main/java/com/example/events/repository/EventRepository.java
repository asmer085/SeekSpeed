package com.example.events.repository;

import com.example.events.entity.Event;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface EventRepository extends CrudRepository<Event, UUID> {
    List<Event> findByOrganizerID(UUID organizerId);
    List<Event> findByCategory(String category);
}
