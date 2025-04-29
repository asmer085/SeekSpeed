package com.example.events.service;

import com.example.events.dto.EventDTO;
import com.example.events.entity.Event;
import com.example.events.exception.InvalidPatchException;
import com.example.events.exception.ResourceNotFoundException;
import com.example.events.repository.EventRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private EventService eventService;

    private Event testEvent;
    private EventDTO testEventDTO;
    private UUID testEventId;
    private UUID testOrganizerId;
    private String testCategory;

    @BeforeEach
    void setUp() {
        testEventId = UUID.randomUUID();
        testOrganizerId = UUID.randomUUID();
        testCategory = "Music";

        testEvent = new Event();
        testEvent.setId(testEventId);
        testEvent.setName("Test Event");
        testEvent.setDescription("Test Description");
        testEvent.setDateTime(LocalDateTime.now().plusDays(1));
        testEvent.setStreet("Test Street");
        testEvent.setCity("Test City");
        testEvent.setCountry("Test Country");
        testEvent.setCategory(testCategory);
        testEvent.setOrganizerID(testOrganizerId);

        testEventDTO = new EventDTO();
        testEventDTO.setName("Test Event");
        testEventDTO.setDescription("Test Description");
        testEventDTO.setDateTime(LocalDateTime.now().plusDays(1));
        testEventDTO.setStreet("Test Street");
        testEventDTO.setCity("Test City");
        testEventDTO.setCountry("Test Country");
        testEventDTO.setCategory(testCategory);
        testEventDTO.setOrganizerID(testOrganizerId);
    }

    @Test
    void createEvent_ValidInput_ShouldReturnCreatedEvent() {
        when(eventRepository.save(any(Event.class))).thenReturn(testEvent);

        Event result = eventService.createEvent(testEventDTO);

        assertNotNull(result);
        assertEquals(testEvent.getName(), result.getName());
        assertEquals(testEvent.getDescription(), result.getDescription());
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void getAllEvents_ShouldReturnAllEvents() {
        List<Event> events = Arrays.asList(testEvent);
        when(eventRepository.findAll()).thenReturn(events);

        Iterable<Event> result = eventService.getAllEvents();

        assertNotNull(result);
        assertEquals(1, ((List<Event>) result).size());
        verify(eventRepository, times(1)).findAll();
    }

    @Test
    void getEventById_ExistingId_ShouldReturnEvent() {
        when(eventRepository.findById(testEventId)).thenReturn(Optional.of(testEvent));

        Event result = eventService.getEventById(testEventId);

        assertNotNull(result);
        assertEquals(testEventId, result.getId());
        verify(eventRepository, times(1)).findById(testEventId);
    }

    @Test
    void getEventById_NonExistingId_ShouldThrowException() {
        when(eventRepository.findById(testEventId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            eventService.getEventById(testEventId);
        });
        verify(eventRepository, times(1)).findById(testEventId);
    }

    @Test
    void getEventsByOrganizerId_ExistingOrganizer_ShouldReturnEvents() {
        List<Event> events = Arrays.asList(testEvent);
        when(eventRepository.findByOrganizerID(testOrganizerId)).thenReturn(events);

        List<Event> result = eventService.getEventsByOrganizerId(testOrganizerId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testOrganizerId, result.get(0).getOrganizerID());
        verify(eventRepository, times(1)).findByOrganizerID(testOrganizerId);
    }

    @Test
    void getEventsByOrganizerId_NonExistingOrganizer_ShouldThrowException() {
        when(eventRepository.findByOrganizerID(testOrganizerId)).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () -> {
            eventService.getEventsByOrganizerId(testOrganizerId);
        });
        verify(eventRepository, times(1)).findByOrganizerID(testOrganizerId);
    }

    @Test
    void getEventsByCategory_ExistingCategory_ShouldReturnEvents() {
        List<Event> events = Arrays.asList(testEvent);
        when(eventRepository.findByCategory(testCategory)).thenReturn(events);

        List<Event> result = eventService.getEventsByCategory(testCategory);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testCategory, result.get(0).getCategory());
        verify(eventRepository, times(1)).findByCategory(testCategory);
    }

    @Test
    void getEventsByCategory_NonExistingCategory_ShouldThrowException() {
        when(eventRepository.findByCategory(testCategory)).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () -> {
            eventService.getEventsByCategory(testCategory);
        });
        verify(eventRepository, times(1)).findByCategory(testCategory);
    }

    @Test
    @Transactional
    void updateEventPatch_NonExistingEvent_ShouldThrowException() throws IOException {
        String patchJson = "[{\"op\":\"replace\",\"path\":\"/name\",\"value\":\"Patched Event\"}]";
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchJson));

        when(eventRepository.findById(testEventId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            eventService.updateEventPatch(testEventId, patch);
        });
    }
}