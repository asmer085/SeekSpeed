package com.example.events.repository;

import com.example.events.entity.Event;
import com.example.events.entity.Types;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TypeRepository extends JpaRepository<Types, UUID> {
    List<Types> findByEvent(Event event);

    @Query("SELECT t FROM Types t WHERE t.event.id = :eventId AND t.distance > :minDistance")
    List<Types> findByEventIdAndDistanceGreaterThan(@Param("eventId") UUID eventId, @Param("minDistance") double minDistance);
}