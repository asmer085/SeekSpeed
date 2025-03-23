package com.example.events.repository;

import com.example.events.entity.Event;
import com.example.events.entity.Review;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends CrudRepository<Review, UUID> {
    // Find all reviews for a specific event
    List<Review> findByEvent(Event event);
}