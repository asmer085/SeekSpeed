package com.example.events.service;

import com.example.events.dto.EventDTO;
import com.example.events.entity.Event;
import com.example.events.entity.User;
import com.example.events.exception.InvalidPatchException;
import com.example.events.exception.ResourceNotFoundException;
import com.example.events.repository.EventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {
    @Autowired
    private final EventRepository eventRepository;
    private final RemoteUserSyncService remoteUserSyncService;

    public Event createEvent(EventDTO eventDTO) {
        // Validacija i sinhronizacija korisnika (organizatora)
        UUID organizerId = eventDTO.getOrganizerID();
        User organizer = remoteUserSyncService.fetchAndSaveUserIfMissing(organizerId);

        Event event = new Event();
        event.setStreet(eventDTO.getStreet());
        event.setCity(eventDTO.getCity());
        event.setCountry(eventDTO.getCountry());
        event.setCategory(eventDTO.getCategory());
        event.setOrganizerID(organizer.getId());
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
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));
    }

    public List<Event> getEventsByOrganizerId(UUID organizerId) {
        List<Event> events = eventRepository.findByOrganizerID(organizerId);
        if (events.isEmpty()) {
            throw new ResourceNotFoundException("No events found for organizer: " + organizerId);
        }
        return events;
    }

    public List<Event> getEventsByCategory(String category) {
        List<Event> events = eventRepository.findByCategory(category);
        if (events.isEmpty()) {
            throw new ResourceNotFoundException("No events found in category: " + category);
        }
        return events;
    }

    @Transactional
    public Event updateEventPatch(UUID eventId, JsonPatch patch) {
        Event event = getEventById(eventId);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            JsonNode patched = patch.apply(objectMapper.convertValue(event, JsonNode.class));
            Event updatedEvent = objectMapper.treeToValue(patched, Event.class);
            updatedEvent.setId(eventId);
            return eventRepository.save(updatedEvent);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new InvalidPatchException("Invalid patch", e);
        }
    }

}
