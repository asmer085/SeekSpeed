package com.example.users.services;

import com.example.users.dtos.NewsletterDTO;
import com.example.users.entity.Newsletter;
import com.example.users.entity.Users;
import com.example.users.mappers.NewsletterMapper;
import com.example.users.repository.NewsletterRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class NewsletterService {

    @Autowired
    private NewsletterRepository newsletterRepository;

    @Autowired
    private NewsletterMapper newsletterMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Validator validator;

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

    @Transactional
    public ResponseEntity<Newsletter> updateNewsletter(UUID newsletterId, NewsletterDTO updatedNewsletter) {
        return newsletterRepository.findById(newsletterId)
                .map(news -> {
                    if (updatedNewsletter.getDescription() != null) news.setDescription(updatedNewsletter.getDescription());
                    if (updatedNewsletter.getTitle() != null) news.setTitle(updatedNewsletter.getTitle());
                    if (updatedNewsletter.getUserId() != null) {
                        Users user = userService.getUserById(updatedNewsletter.getUserId());
                        news.setUser(user);
                    }
                    return ResponseEntity.ok(newsletterRepository.save(news));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    public Newsletter applyPatchToNewsletter(JsonPatch patch, UUID newsletterId) {
        try {
            Newsletter newsletter = newsletterRepository.findById(newsletterId)
                    .orElseThrow(() -> new NewsletterNotFoundException("Newsletter with id " + newsletterId + " not found"));
            JsonNode newsletterNode = objectMapper.valueToTree(newsletter);
            JsonNode patchedNode = patch.apply(newsletterNode);
            Newsletter patchedNewsletter = objectMapper.treeToValue(patchedNode, Newsletter.class);

            Set<ConstraintViolation<Newsletter>> violations = validator.validate(patchedNewsletter);
            if (!violations.isEmpty()) {
                StringBuilder errorMessage = new StringBuilder("Validation failed: ");
                for (ConstraintViolation<Newsletter> violation : violations) {
                    errorMessage.append(violation.getMessage()).append(" ");
                }
                throw new RuntimeException(errorMessage.toString());
            }
            return newsletterRepository.save(patchedNewsletter);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
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
