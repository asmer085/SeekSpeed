package com.example.users.controllers;
import com.example.users.entity.Newsletter;
import com.example.users.repository.NewsletterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/newsletter")
public class NewsletterController {

    @Autowired
    private NewsletterRepository newsletterRepository;


    @GetMapping("/all")
    public @ResponseBody Iterable<Newsletter> getAllNewsletters() {
        return newsletterRepository.findAll();
    }

    @GetMapping("/{newsletterId}")
    public @ResponseBody Newsletter getNewsletterById(@PathVariable UUID newsletterId) {
        return newsletterRepository.findById(newsletterId).orElseThrow(() -> new RuntimeException("Newsletter not found"));
    }

    @PostMapping("/add")
    public @ResponseBody Newsletter addNewsletter(@RequestBody Newsletter newsletter) {
        return newsletterRepository.save(newsletter);
    }

    @DeleteMapping("/{newsletterId}")
    public @ResponseBody ResponseEntity<Object> deleteNewsletter(@PathVariable UUID newsletterId) {
        return newsletterRepository.findById(newsletterId)
                .map(newsletter -> {
                    newsletterRepository.delete(newsletter);
                    return ResponseEntity.ok().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{newsletterId}")
    public @ResponseBody ResponseEntity <Newsletter> updateNewsletter(@PathVariable UUID newsletterId, @RequestBody Newsletter updatedNewsletter) {
        return newsletterRepository.findById(newsletterId)
                .map(news -> {
                    if (updatedNewsletter.getDescription() != null) news.setDescription(updatedNewsletter.getDescription());
                    if (updatedNewsletter.getTitle() != null) news.setTitle(updatedNewsletter.getTitle());
                    if (updatedNewsletter.getUser() != null) news.setUser(updatedNewsletter.getUser());
                    return ResponseEntity.ok(newsletterRepository.save(news));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
