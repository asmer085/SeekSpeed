package com.example.users.controllers;

import com.example.users.entity.Newsletter;
import com.example.users.services.NewsletterService;
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
    public @ResponseBody Newsletter getNewsletterById(@PathVariable UUID newsletterId) {
        return newsletterService.getNewsletterById(newsletterId);
    }

    @PostMapping("/add")
    public @ResponseBody Newsletter addNewsletter(@RequestBody Newsletter newsletter) {
        return newsletterService.addNewsletter(newsletter);
    }

    @PutMapping("/{newsletterId}")
    public @ResponseBody ResponseEntity<Newsletter> updateNewsletter(@PathVariable UUID newsletterId, @RequestBody Newsletter updatedNewsletter) {
        return newsletterService.updateNewsletter(newsletterId, updatedNewsletter);
    }

    @DeleteMapping("/{newsletterId}")
    public @ResponseBody ResponseEntity<Object> deleteNewsletter(@PathVariable UUID newsletterId) {
        return newsletterService.deleteNewsletter(newsletterId);
    }
}
