package com.example.events.repository;

import com.example.events.entity.Event;
import com.example.events.entity.User;
import com.example.events.entity.UserEvent;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserEventRepository extends CrudRepository<UserEvent, UUID> {
    // Find a UserEvent by user ID and event ID
    Optional<UserEvent> findByUser_IdAndEvent_Id(UUID userId, UUID eventId);

    // Find all UserEvents for a specific user
    List<UserEvent> findByUser(User user);

    // Find all UserEvents for a specific event
    List<UserEvent> findByEvent(Event event);
}