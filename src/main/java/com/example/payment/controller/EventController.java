package com.example.payment.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.payment.entity.Event;
import com.example.payment.repository.EventRepo;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventRepo eventRepository;

    @PostMapping("/create")
    public ResponseEntity<String> createEvent(@RequestBody Event event) {
        eventRepository.save(event);
        return ResponseEntity.ok("Event created successfully");
    }
}
