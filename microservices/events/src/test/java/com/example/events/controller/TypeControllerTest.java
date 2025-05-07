package com.example.events.controller;

import com.example.events.dto.TypeDTO;
import com.example.events.entity.Event;
import com.example.events.entity.Type;
import com.example.events.exception.GlobalExceptionHandler;
import com.example.events.exception.ResourceNotFoundException;
import com.example.events.service.TypeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TypeControllerTest {

    @Mock
    private TypeService typeService;

    @InjectMocks
    private TypeController typeController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Type testType;
    private TypeDTO testTypeDTO;
    private UUID testEventId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(typeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();

        testEventId = UUID.randomUUID();

        testTypeDTO = new TypeDTO(UUID.randomUUID(), 99.99, "10km", "00:45:00", testEventId);

        testType = new Type();
        testType.setId(testTypeDTO.getId());
        testType.setPrice(testTypeDTO.getPrice());
        testType.setDistance(testTypeDTO.getDistance());
        testType.setResults(testTypeDTO.getResults());

        Event event = new Event();
        event.setId(testEventId);
        testType.setEvent(event);
    }

    @Test
    void createType_ShouldReturnCreatedType() throws Exception {
        when(typeService.createType(any(TypeDTO.class))).thenReturn(testType);

        mockMvc.perform(post("/api/types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTypeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.distance").value("10km"))
                .andExpect(jsonPath("$.results").value("00:45:00"))
                .andExpect(jsonPath("$.price").value(99.99))
                .andExpect(jsonPath("$.event.id").value(testEventId.toString()));
    }

    @Test
    void getAllTypes_ShouldReturnTypes() throws Exception {
        when(typeService.getAllTypes()).thenReturn(Collections.singletonList(testType));

        mockMvc.perform(get("/api/types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].event.id").value(testEventId.toString()));
    }

    @Test
    void getTypesByEventId_ShouldReturnTypes() throws Exception {
        when(typeService.getTypesByEventId(testEventId)).thenReturn(List.of(testType));

        mockMvc.perform(get("/api/types/event/{eventId}", testEventId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].event.id").value(testEventId.toString()));
    }

    @Test
    void getTypesByEventId_NotFound_ShouldReturn404() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        when(typeService.getTypesByEventId(nonExistentId))
                .thenThrow(new ResourceNotFoundException("No types found for event"));

        mockMvc.perform(get("/api/types/event/{eventId}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("No types found for event")));
    }

    @Test
    void updateTypes_ShouldReturnUpdatedList() throws Exception {
        when(typeService.updateTypes(anyList())).thenReturn(List.of(testType));

        mockMvc.perform(put("/api/types/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(testTypeDTO))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].event.id").value(testEventId.toString()));
    }

    @Test
    void getTypesByDistance_ShouldReturnFilteredTypes() throws Exception {
        when(typeService.getTypesByEventIdAndMinDistance(eq(testEventId), eq(10.0))).thenReturn(List.of(testType));

        mockMvc.perform(get("/api/types/byDistance")
                        .param("eventId", testEventId.toString())
                        .param("minDistance", "10.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].event.id").value(testEventId.toString()));
    }

    @Test
    void getTypesByDistance_NotFound_ShouldReturn404() throws Exception {
        when(typeService.getTypesByEventIdAndMinDistance(eq(testEventId), eq(10.0)))
                .thenThrow(new ResourceNotFoundException("No types found with given distance"));

        mockMvc.perform(get("/api/types/byDistance")
                        .param("eventId", testEventId.toString())
                        .param("minDistance", "10.0"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("No types found with given distance")));
    }
}
