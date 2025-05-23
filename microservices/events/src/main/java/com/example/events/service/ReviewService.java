package com.example.events.service;

import com.example.events.dto.ReviewDTO;
import com.example.events.entity.Event;
import com.example.events.entity.Review;
import com.example.events.entity.User;
import com.example.events.exception.ResourceNotFoundException;
import com.example.events.repository.EventRepository;
import com.example.events.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {
    @Autowired
    private final ReviewRepository reviewRepository;
    @Autowired
    private final EventRepository eventRepository;
    private final RemoteUserSyncService remoteUserSyncService;

    public Review createReview(ReviewDTO reviewDTO) {
        // Validacija i sinhronizacija korisnika (pisac recenzije)
        User user = remoteUserSyncService.fetchAndSaveUserIfMissing(reviewDTO.getUserUUID());

        Event event = eventRepository.findById(reviewDTO.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + reviewDTO.getEventId()));

        Review review = new Review();
        review.setStars(reviewDTO.getStars());
        review.setUserUUID(user.getId());
        review.setEvent(event);

        return reviewRepository.save(review);
    }

    public Iterable<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public List<Review> getReviewsByEventId(UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));
        return reviewRepository.findByEvent(event);
    }
}
