package com.example.events.service;

import com.example.events.dto.ReviewDTO;
import com.example.events.entity.Event;
import com.example.events.entity.Review;
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

    public Review createReview(ReviewDTO reviewDTO) {
        Review review = new Review();
        review.setStars(reviewDTO.getStars());
        review.setUserUUID(reviewDTO.getUserUUID());

        Event event = eventRepository.findById(reviewDTO.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));
        review.setEvent(event);

        return reviewRepository.save(review);
    }

    public Iterable<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public List<Review> getReviewsByEventId(UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        return reviewRepository.findByEvent(event);
    }
}
