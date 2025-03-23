package com.example.events.controller;

import com.example.events.repository.TypeRepository;
import com.example.events.entity.Event;
import com.example.events.entity.Type;
import com.example.events.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping(path="/types")
public class TypeController {

    @Autowired
    private TypeRepository typeRepository;

    @Autowired
    private EventRepository eventRepository;

    // Add a new type
    @PostMapping(path="/add")
    public @ResponseBody String addNewType(
            @RequestParam double price,
            @RequestParam String distance,
            @RequestParam String results,
            @RequestParam UUID eventId) {

        Type type = new Type();
        type.setPrice(price);
        type.setDistance(distance);
        type.setResults(results);

        // Fetch the event from the database
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));
        type.setEvent(event);

        typeRepository.save(type);
        return "Type Saved";
    }

    // Get all types
    @GetMapping(path="/all")
    public @ResponseBody Iterable<Type> getAllTypes() {
        return typeRepository.findAll();
    }

    // Get types by event ID
    @GetMapping(path="/event/{eventId}")
    public @ResponseBody Iterable<Type> getTypesByEventId(@PathVariable UUID eventId) {
        Event event = new Event();
        event.setId(eventId);
        return typeRepository.findByEvent(event);
    }
}