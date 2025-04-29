package com.example.events.controller;

import com.example.events.service.UserEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserEventControllerTest {

    @Mock
    private UserEventService userEventService;

    @InjectMocks
    private UserEventController userEventController;

    private MockMvc mockMvc;
    private UUID userId;
    private UUID eventId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userEventController)
                .build();

        userId = UUID.randomUUID();
        eventId = UUID.randomUUID();
    }

    @Test
    void getEventIdsForUser_ShouldReturnList() throws Exception {
        when(userEventService.getEventIdsByUserId(userId)).thenReturn(List.of(eventId));

        mockMvc.perform(get("/api/user-events/user/{userId}/events", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(eventId.toString()));
    }
}
