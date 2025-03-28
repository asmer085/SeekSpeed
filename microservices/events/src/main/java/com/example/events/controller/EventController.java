package com.example.events.controller;

import com.example.events.dto.EventDTO;
import com.example.events.entity.Event;
import com.example.events.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping
    public Event createEvent(@Valid @RequestBody EventDTO eventDTO) {
        return eventService.createEvent(eventDTO);
    }

    @GetMapping
    public Iterable<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{eventId}")
    public Event getEventById(@PathVariable UUID eventId) {
        return eventService.getEventById(eventId);
    }

    @GetMapping("/organizer/{organizerId}")
    public List<Event> getEventsByOrganizerId(@PathVariable UUID organizerId) {
        return eventService.getEventsByOrganizerId(organizerId);
    }

    @GetMapping("/category/{category}")
    public List<Event> getEventsByCategory(@PathVariable String category) {
        return eventService.getEventsByCategory(category);
    }
}