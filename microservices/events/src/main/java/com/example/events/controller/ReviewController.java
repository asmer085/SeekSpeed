package com.example.events.controller;

import com.example.events.dto.ReviewDTO;
import com.example.events.entity.Review;
import com.example.events.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public Review createReview(@RequestBody ReviewDTO reviewDTO) {
        return reviewService.createReview(reviewDTO);
    }

    @GetMapping
    public Iterable<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @GetMapping("/event/{eventId}")
    public List<Review> getReviewsByEventId(@PathVariable UUID eventId) {
        return reviewService.getReviewsByEventId(eventId);
    }
}
