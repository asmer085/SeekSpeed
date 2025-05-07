package com.example.events.service;

import com.example.events.dto.ReviewDTO;
import com.example.events.entity.Event;
import com.example.events.entity.Review;
import com.example.events.exception.ResourceNotFoundException;
import com.example.events.repository.EventRepository;
import com.example.events.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private ReviewService reviewService;

    private UUID eventId;
    private UUID userId;
    private ReviewDTO reviewDTO;
    private Event event;
    private Review review;

    @BeforeEach
    void setUp() {
        eventId = UUID.randomUUID();
        userId = UUID.randomUUID();

        event = new Event();
        event.setId(eventId);

        reviewDTO = new ReviewDTO();
        reviewDTO.setEventId(eventId);
        reviewDTO.setUserUUID(userId);
        reviewDTO.setStars(5);

        review = new Review();
        review.setId(UUID.randomUUID());
        review.setEvent(event);
        review.setUserUUID(userId);
        review.setStars(5);
    }

    @Test
    void createReview_ValidInput_ShouldReturnReview() {
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        Review result = reviewService.createReview(reviewDTO);

        assertNotNull(result);
        assertEquals(5, result.getStars());
        verify(eventRepository).findById(eventId);
        verify(reviewRepository).save(any(Review.class));
    }

    @Test
    void createReview_EventNotFound_ShouldThrowException() {
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reviewService.createReview(reviewDTO));
    }

    @Test
    void getAllReviews_ShouldReturnList() {
        when(reviewRepository.findAll()).thenReturn(List.of(review));

        Iterable<Review> result = reviewService.getAllReviews();

        assertNotNull(result);
        assertTrue(result.iterator().hasNext());
    }

    @Test
    void getReviewsByEventId_ValidEvent_ShouldReturnReviews() {
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(reviewRepository.findByEvent(event)).thenReturn(List.of(review));

        List<Review> result = reviewService.getReviewsByEventId(eventId);

        assertEquals(1, result.size());
    }

    @Test
    void getReviewsByEventId_EventNotFound_ShouldThrowException() {
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reviewService.getReviewsByEventId(eventId));
    }
}
