package com.example.events.service;

import com.example.events.dto.EventDTO;
import com.example.events.entity.Event;
import com.example.events.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {
    @Autowired
    private final EventRepository eventRepository;

    public Event createEvent(EventDTO eventDTO) {
        Event event = new Event();
        event.setStreet(eventDTO.getStreet());
        event.setCity(eventDTO.getCity());
        event.setCountry(eventDTO.getCountry());
        event.setCategory(eventDTO.getCategory());
        event.setOrganizerID(eventDTO.getOrganizerID());
        event.setName(eventDTO.getName());
        event.setDescription(eventDTO.getDescription());
        event.setDateTime(eventDTO.getDateTime());

        return eventRepository.save(event);
    }

    public Iterable<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(UUID eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    public List<Event> getEventsByOrganizerId(UUID organizerId) {
        return eventRepository.findByOrganizerID(organizerId);
    }

    public List<Event> getEventsByCategory(String category) {
        return eventRepository.findByCategory(category);
    }
}
