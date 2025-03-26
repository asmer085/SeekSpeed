package com.example.events.controller;


import com.example.events.repository.EventRepository;
import com.example.events.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller // This means that this class is a Controller
@RequestMapping(path="/events") // URLs start with /events (after the application path)
public class EventController {

    @Autowired // Automatically injects the EventRepository bean
    private EventRepository eventRepository;

    // Add a new event
    @PostMapping(path="/add") // Map ONLY POST Requests
    public @ResponseBody String addNewEvent(
            @RequestParam String street,
            @RequestParam String city,
            @RequestParam String country,
            @RequestParam String category,
            @RequestParam UUID organizerID,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam LocalDateTime dateTime) {

        // Create a new Event object
        Event event = new Event();
        event.setStreet(street);
        event.setCity(city);
        event.setCountry(country);
        event.setCategory(category);
        event.setOrganizerID(organizerID);
        event.setName(name);
        event.setDescription(description);
        event.setDateTime(dateTime);


        // Save the event to the database
        eventRepository.save(event);
        return "Event Saved";
    }

    // Get all events
    @GetMapping(path="/all")
    public @ResponseBody Iterable<Event> getAllEvents() {
        // This returns a JSON or XML with all the events
        return eventRepository.findAll();
    }

    @GetMapping(path="/{eventId}")
    public @ResponseBody Event getEventById(@PathVariable UUID eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    @GetMapping(path="/organizer/{organizerId}")
    public @ResponseBody List<Event> getEventsByOrganizerId(@PathVariable UUID organizerId) {
        return eventRepository.findByOrganizerID(organizerId);
    }

    @GetMapping(path="/category/{category}")
    public @ResponseBody List<Event> getEventsByCategory(@PathVariable String category) {
        return eventRepository.findByCategory(category);
    }
}