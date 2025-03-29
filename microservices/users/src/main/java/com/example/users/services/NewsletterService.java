package com.example.users.services;

import com.example.users.dtos.NewsletterDTO;
import com.example.users.entity.Newsletter;
import com.example.users.entity.Users;
import com.example.users.mappers.NewsletterMapper;
import com.example.users.repository.NewsletterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NewsletterService {

    @Autowired
    private NewsletterRepository newsletterRepository;

    @Autowired
    private NewsletterMapper newsletterMapper;

    @Autowired
    private UserService userService;

    public Iterable<Newsletter> getAllNewsletters() {
        return newsletterRepository.findAll();
    }

    public Newsletter getNewsletterById(UUID newsletterId) {
        return newsletterRepository.findById(newsletterId)
                .orElseThrow(() -> new NewsletterNotFoundException("Newsletter with id " + newsletterId + " not found"));
    }

    public NewsletterDTO addNewsletter(NewsletterDTO newsletterDTO) {
        Newsletter news = newsletterMapper.newsletterDTOToNewsletter(newsletterDTO, userService);
        Newsletter savedNews = newsletterRepository.save(news);
        return newsletterMapper.newsletterToNewsletterDTO(savedNews);
    }


    public ResponseEntity<Newsletter> updateNewsletter(UUID newsletterId, Newsletter updatedNewsletter) {
        return newsletterRepository.findById(newsletterId)
                .map(news -> {
                    if (updatedNewsletter.getDescription() != null) news.setDescription(updatedNewsletter.getDescription());
                    if (updatedNewsletter.getTitle() != null) news.setTitle(updatedNewsletter.getTitle());
                    if (updatedNewsletter.getUser() != null) news.setUser(updatedNewsletter.getUser());
                    return ResponseEntity.ok(newsletterRepository.save(news));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Object> deleteNewsletter(UUID newsletterId) {
        return newsletterRepository.findById(newsletterId)
                .map(newsletter -> {
                    newsletterRepository.delete(newsletter);
                    return ResponseEntity.ok().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public static class NewsletterNotFoundException extends RuntimeException {
        public NewsletterNotFoundException(String message) {
            super(message);
        }
    }
}
