package com.example.events.repository;

import com.example.events.entity.Event;
import com.example.events.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TypeRepository extends JpaRepository<Type, UUID> {
    List<Type> findByEvent(Event event);

    @Query("SELECT t FROM Type t WHERE t.event.id = :eventId AND t.distance > :minDistance")
    List<Type> findByEventIdAndDistanceGreaterThan(@Param("eventId") UUID eventId, @Param("minDistance") double minDistance);
}