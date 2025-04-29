package com.example.events.controller;

import com.example.events.dto.ReviewDTO;
import com.example.events.entity.Event;
import com.example.events.entity.Review;
import com.example.events.exception.GlobalExceptionHandler;
import com.example.events.exception.ResourceNotFoundException;
import com.example.events.service.ReviewService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Review testReview;
    private ReviewDTO testReviewDTO;
    private UUID testEventId;
    private UUID testUserId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();

        testEventId = UUID.randomUUID();
        testUserId = UUID.randomUUID();

        testReviewDTO = new ReviewDTO();
        testReviewDTO.setStars(5);
        testReviewDTO.setEventId(testEventId);
        testReviewDTO.setUserUUID(testUserId);

        testReview = new Review();
        testReview.setId(UUID.randomUUID());
        testReview.setStars(5);

        Event event = new Event();
        event.setId(testReviewDTO.getEventId());
        testReview.setEvent(event);
        testReview.setUserUUID(testReviewDTO.getUserUUID());
    }

    @Test
    void createReview_ShouldReturnCreatedReview() throws Exception {
        when(reviewService.createReview(any(ReviewDTO.class))).thenReturn(testReview);

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testReviewDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stars").value(5))
                .andExpect(jsonPath("$.event.id").value(testEventId.toString()))
                .andExpect(jsonPath("$.userUUID").value(testUserId.toString()));
    }

    @Test
    void getAllReviews_ShouldReturnListOfReviews() throws Exception {
        when(reviewService.getAllReviews()).thenReturn(Collections.singletonList(testReview));

        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].stars").value(5));
    }

    @Test
    void getReviewsByEventId_ShouldReturnReviewsForEvent() throws Exception {
        when(reviewService.getReviewsByEventId(testEventId)).thenReturn(List.of(testReview));

        mockMvc.perform(get("/api/reviews/event/{eventId}", testEventId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].stars").value(5))
                .andExpect(jsonPath("$[0].event.id").value(testEventId.toString()));
    }

    @Test
    void getReviewsByEventId_NotFound_ShouldReturn404() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        when(reviewService.getReviewsByEventId(nonExistentId))
                .thenThrow(new ResourceNotFoundException("No reviews found"));

        mockMvc.perform(get("/api/reviews/event/{eventId}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("No reviews found")));
    }
}
