package com.example.users.controllers;

import com.example.users.dtos.NewsletterDTO;
import com.example.users.entity.Newsletter;
import com.example.users.entity.Users;
import com.example.users.services.NewsletterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class NewsletterControllerTests {

    @Mock
    private NewsletterService newsletterService;

    @InjectMocks
    private NewsletterController newsletterController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Newsletter testNewsletter;
    private NewsletterDTO testNewsletterDTO;
    private UUID testNewsletterId;
    private UUID testUserId;

    @BeforeEach
    void setUp() {
        testNewsletterId = UUID.randomUUID();
        testUserId = UUID.randomUUID();

        Users testUser = new Users();
        testUser.setId(testUserId);

        testNewsletter = new Newsletter();
        testNewsletter.setId(testNewsletterId);
        testNewsletter.setTitle("Monthly Update");
        testNewsletter.setDescription("Latest news and updates");
        testNewsletter.setUser(testUser);

        testNewsletterDTO = new NewsletterDTO();
        testNewsletterDTO.setTitle("Monthly Update");
        testNewsletterDTO.setDescription("Latest news and updates");
        testNewsletterDTO.setUserId(testUserId);

        mockMvc = MockMvcBuilders.standaloneSetup(newsletterController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllNewsletters_ShouldReturnNewsletters() throws Exception {
        // Arrange
        Iterable<Newsletter> newsletters = Arrays.asList(testNewsletter);
        given(newsletterService.getAllNewsletters()).willReturn(newsletters);

        // Act & Assert
        mockMvc.perform(get("/newsletter/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Monthly Update"))
                .andExpect(jsonPath("$[0].description").value("Latest news and updates"));
    }

    @Test
    void getNewsletterById_ExistingId_ShouldReturnNewsletter() throws Exception {
        // Arrange
        given(newsletterService.getNewsletterById(testNewsletterId)).willReturn(testNewsletter);

        // Act & Assert
        mockMvc.perform(get("/newsletter/{newsletterId}", testNewsletterId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Monthly Update"))
                .andExpect(jsonPath("$.description").value("Latest news and updates"));
    }

    @Test
    void getNewsletterById_NonExistingId_ShouldThrowException() throws Exception {
        // Arrange
        given(newsletterService.getNewsletterById(testNewsletterId))
                .willThrow(new NewsletterService.NewsletterNotFoundException("Newsletter not found"));

        // Act & Assert
        mockMvc.perform(get("/newsletter/{newsletterId}", testNewsletterId))
                .andExpect(status().isNotFound());
    }

    @Test
    void addNewsletter_ValidNewsletter_ShouldReturnCreatedNewsletter() throws Exception {
        // Arrange
        given(newsletterService.addNewsletter(any(NewsletterDTO.class))).willReturn(testNewsletterDTO);

        // Act & Assert
        mockMvc.perform(post("/newsletter/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testNewsletterDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Monthly Update"))
                .andExpect(jsonPath("$.description").value("Latest news and updates"));
    }

    @Test
    void updateNewsletter_ExistingNewsletter_ShouldReturnUpdatedNewsletter() throws Exception {
        // Arrange
        Newsletter updatedNewsletter = new Newsletter();
        updatedNewsletter.setTitle("Updated Monthly Update");
        updatedNewsletter.setDescription("Updated news and updates");

        given(newsletterService.updateNewsletter(eq(testNewsletterId), any(Newsletter.class)))
                .willReturn(ResponseEntity.ok(updatedNewsletter));

        // Act & Assert
        mockMvc.perform(put("/newsletter/{newsletterId}", testNewsletterId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedNewsletter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Monthly Update"))
                .andExpect(jsonPath("$.description").value("Updated news and updates"));
    }

    @Test
    void updateNewsletter_NonExistingNewsletter_ShouldReturnNotFound() throws Exception {
        // Arrange
        given(newsletterService.updateNewsletter(eq(testNewsletterId), any(Newsletter.class)))
                .willReturn(ResponseEntity.notFound().build());

        // Act & Assert
        mockMvc.perform(put("/newsletter/{newsletterId}", testNewsletterId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testNewsletter)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteNewsletter_ExistingNewsletter_ShouldReturnOk() throws Exception {
        // Arrange
        given(newsletterService.deleteNewsletter(testNewsletterId))
                .willReturn(ResponseEntity.ok().build());

        // Act & Assert
        mockMvc.perform(delete("/newsletter/{newsletterId}", testNewsletterId))
                .andExpect(status().isOk());
    }

    @Test
    void deleteNewsletter_NonExistingNewsletter_ShouldReturnNotFound() throws Exception {
        // Arrange
        given(newsletterService.deleteNewsletter(testNewsletterId))
                .willReturn(ResponseEntity.notFound().build());

        // Act & Assert
        mockMvc.perform(delete("/newsletter/{newsletterId}", testNewsletterId))
                .andExpect(status().isNotFound());
    }
}