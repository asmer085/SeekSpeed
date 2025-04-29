package com.example.events.controller;

import com.example.events.dto.EventDTO;
import com.example.events.entity.Event;
import com.example.events.exception.GlobalExceptionHandler;
import com.example.events.exception.InvalidPatchException;
import com.example.events.exception.ResourceNotFoundException;
import com.example.events.service.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.fge.jsonpatch.JsonPatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Event testEvent;
    private EventDTO testEventDTO;
    private UUID testEventId;
    private UUID testOrganizerId;
    private String testCategory;

    @BeforeEach
    void setUp() {
        // Include the GlobalExceptionHandler here
        mockMvc = MockMvcBuilders.standaloneSetup(eventController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

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
    void createEvent_ValidInput_ShouldReturnCreatedEvent() throws Exception {
        when(eventService.createEvent(any(EventDTO.class))).thenReturn(testEvent);

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testEventDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Event"))
                .andExpect(jsonPath("$.id").value(testEventId.toString()));
    }

    @Test
    void getAllEvents_ShouldReturnAllEvents() throws Exception {
        List<Event> events = Collections.singletonList(testEvent);
        when(eventService.getAllEvents()).thenReturn(events);

        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Event"))
                .andExpect(jsonPath("$[0].id").value(testEventId.toString()));
    }

    @Test
    void getEventById_ExistingId_ShouldReturnEvent() throws Exception {
        when(eventService.getEventById(testEventId)).thenReturn(testEvent);

        mockMvc.perform(get("/api/events/{eventId}", testEventId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Event"))
                .andExpect(jsonPath("$.id").value(testEventId.toString()));
    }

    @Test
    void getEventById_NonExistingId_ShouldReturnNotFound() throws Exception {
        when(eventService.getEventById(any(UUID.class)))
                .thenThrow(new ResourceNotFoundException("Event not found"));

        mockMvc.perform(get("/api/events/{eventId}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getEventsByOrganizerId_ExistingOrganizer_ShouldReturnEvents() throws Exception {
        List<Event> events = Collections.singletonList(testEvent);
        when(eventService.getEventsByOrganizerId(testOrganizerId)).thenReturn(events);

        mockMvc.perform(get("/api/events/organizer/{organizerId}", testOrganizerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].organizerID").value(testOrganizerId.toString()));
    }

    @Test
    void getEventsByOrganizerId_NonExistingOrganizer_ShouldReturnNotFound() throws Exception {
        when(eventService.getEventsByOrganizerId(any(UUID.class)))
                .thenThrow(new ResourceNotFoundException("No events found"));

        mockMvc.perform(get("/api/events/organizer/{organizerId}", UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("No events found")));
    }

    @Test
    void getEventsByCategory_ExistingCategory_ShouldReturnEvents() throws Exception {
        List<Event> events = Collections.singletonList(testEvent);
        when(eventService.getEventsByCategory(testCategory)).thenReturn(events);

        mockMvc.perform(get("/api/events/category/{category}", testCategory))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value(testCategory));
    }

    @Test
    void getEventsByCategory_NonExistingCategory_ShouldReturnNotFound() throws Exception {
        when(eventService.getEventsByCategory("NonExisting"))
                .thenThrow(new ResourceNotFoundException("No events found"));

        mockMvc.perform(get("/api/events/category/{category}", "NonExisting"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("No events found")));
    }

    @Test
    void patchEvent_ValidPatch_ShouldReturnUpdatedEvent() throws Exception {
        String patchJson = "[{\"op\":\"replace\",\"path\":\"/name\",\"value\":\"Patched Event\"}]";
        Event patchedEvent = new Event();
        patchedEvent.setId(testEventId);
        patchedEvent.setName("Patched Event");

        when(eventService.updateEventPatch(eq(testEventId), any(JsonPatch.class))).thenReturn(patchedEvent);

        mockMvc.perform(patch("/api/events/{eventId}", testEventId)
                        .contentType("application/json-patch+json")
                        .content(patchJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Patched Event"));
    }

    @Test
    void patchEvent_InvalidPatch_ShouldReturnBadRequest() throws Exception {
        String invalidPatchJson = "[{\"op\":\"replace\",\"path\":\"/invalidField\",\"value\":\"value\"}]";

        when(eventService.updateEventPatch(any(UUID.class), any(JsonPatch.class)))
                .thenThrow(new InvalidPatchException("Invalid patch"));

        mockMvc.perform(patch("/api/events/{eventId}", UUID.randomUUID())
                        .contentType("application/json-patch+json")
                        .content(invalidPatchJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Invalid patch")));
    }
}