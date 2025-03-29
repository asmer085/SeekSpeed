package com.example.users.controllers;

import com.example.users.dtos.NewsletterDTO;
import com.example.users.entity.Newsletter;
import com.example.users.services.NewsletterService;
import com.example.users.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/newsletter")
public class NewsletterController {

    @Autowired
    private NewsletterService newsletterService;

    @GetMapping("/all")
    public @ResponseBody Iterable<Newsletter> getAllNewsletters() {
        return newsletterService.getAllNewsletters();
    }

    @GetMapping("/{newsletterId}")
    public ResponseEntity<Newsletter> getNewsletterById(@PathVariable UUID newsletterId) {
        try {
            Newsletter news = newsletterService.getNewsletterById(newsletterId);
            return ResponseEntity.ok(news);
        } catch (NewsletterService.NewsletterNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<NewsletterDTO> addNewsletter(@Valid @RequestBody NewsletterDTO newsletterDTO) {
        NewsletterDTO createdNewsletter = newsletterService.addNewsletter(newsletterDTO);
        return ResponseEntity.ok(createdNewsletter);
    }

    @PutMapping("/{newsletterId}")
    public ResponseEntity<Newsletter> updateNewsletter(
            @PathVariable UUID newsletterId,
            @Valid @RequestBody Newsletter updatedNewsletter) {
        return newsletterService.updateNewsletter(newsletterId, updatedNewsletter);
    }

    @DeleteMapping("/{newsletterId}")
    public @ResponseBody ResponseEntity<Object> deleteNewsletter(@PathVariable UUID newsletterId) {
        return newsletterService.deleteNewsletter(newsletterId);
    }

    @ExceptionHandler(NewsletterService.NewsletterNotFoundException.class)
    public ResponseEntity<Object> handleNewsletterNotFound(NewsletterService.NewsletterNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }
}
