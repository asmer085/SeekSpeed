package com.example.events.controller;

import com.example.events.repository.ReviewRepository;
import com.example.events.entity.Event;
import com.example.events.entity.Review;
import com.example.events.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping(path="/reviews")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired // Add this annotation to inject the EventRepository bean
    private EventRepository eventRepository;

    // Add a new review
    @PostMapping(path="/add")
    public @ResponseBody String addNewReview(
            @RequestParam int stars,
            @RequestParam UUID eventId,
            @RequestParam UUID userUUID) {

        Review review = new Review();
        review.setStars(stars);
        review.setUserUUID(userUUID);

        // Fetch the event from the database
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        review.setEvent(event);

        reviewRepository.save(review);
        return "Review Saved";
    }

    // Get all reviews
    @GetMapping(path="/all")
    public @ResponseBody Iterable<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    // Get reviews by event ID
    @GetMapping(path="/event/{eventId}")
    public @ResponseBody Iterable<Review> getReviewsByEventId(@PathVariable UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        return reviewRepository.findByEvent(event);
    }
}