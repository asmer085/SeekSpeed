package com.example.users.services;

import com.example.users.dtos.NewsletterDTO;
import com.example.users.entity.Newsletter;
import com.example.users.entity.Users;
import com.example.users.mappers.NewsletterMapper;
import com.example.users.repository.NewsletterRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewsletterServiceTests {

    @Mock
    private NewsletterRepository newsletterRepository;

    @Mock
    private NewsletterMapper newsletterMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private NewsletterService newsletterService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Validator validator;

    private Newsletter testNewsletter;
    private NewsletterDTO testNewsletterDTO;
    private NewsletterDTO updatedNewsletterDTO;
    private UUID testNewsletterId;
    private UUID testUserId;
    private UUID newUserId;

    @BeforeEach
    void setUp() {
        testNewsletterId = UUID.randomUUID();
        testUserId = UUID.randomUUID();
        newUserId = UUID.randomUUID();

        Users testUser = new Users();
        testUser.setId(testUserId);

        Users newUser = new Users();
        newUser.setId(newUserId);

        testNewsletter = new Newsletter();
        testNewsletter.setId(testNewsletterId);
        testNewsletter.setTitle("Monthly Update");
        testNewsletter.setDescription("Latest news");
        testNewsletter.setUser(testUser);

        testNewsletterDTO = new NewsletterDTO();
        testNewsletterDTO.setTitle("Monthly Update");
        testNewsletterDTO.setDescription("Latest news");
        testNewsletterDTO.setUserId(testUserId);

        updatedNewsletterDTO = new NewsletterDTO();
        updatedNewsletterDTO.setTitle("Weekly Digest");
        updatedNewsletterDTO.setDescription("Fresh updates");
        updatedNewsletterDTO.setUserId(newUserId);
    }

    @Test
    void getAllNewsletters_ShouldReturnAllNewsletters() {
        // Arrange
        when(newsletterRepository.findAll()).thenReturn(List.of(testNewsletter));

        // Act
        Iterable<Newsletter> result = newsletterService.getAllNewsletters();

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Newsletter>) result).size());
        verify(newsletterRepository, times(1)).findAll();
    }

    @Test
    void getNewsletterById_ExistingId_ShouldReturnNewsletter() {
        // Arrange
        when(newsletterRepository.findById(testNewsletterId)).thenReturn(Optional.of(testNewsletter));

        // Act
        Newsletter result = newsletterService.getNewsletterById(testNewsletterId);

        // Assert
        assertNotNull(result);
        assertEquals(testNewsletter, result);
        verify(newsletterRepository, times(1)).findById(testNewsletterId);
    }

    @Test
    void getNewsletterById_NonExistingId_ShouldThrowException() {
        // Arrange
        when(newsletterRepository.findById(testNewsletterId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NewsletterService.NewsletterNotFoundException.class, () -> {
            newsletterService.getNewsletterById(testNewsletterId);
        });
        verify(newsletterRepository, times(1)).findById(testNewsletterId);
    }

    @Test
    void addNewsletter_ValidNewsletter_ShouldReturnNewsletterDTO() {
        // Arrange
        when(newsletterMapper.newsletterDTOToNewsletter(any(NewsletterDTO.class), any(UserService.class)))
                .thenReturn(testNewsletter);
        when(newsletterRepository.save(any(Newsletter.class))).thenReturn(testNewsletter);
        when(newsletterMapper.newsletterToNewsletterDTO(any(Newsletter.class))).thenReturn(testNewsletterDTO);

        // Act
        NewsletterDTO result = newsletterService.addNewsletter(testNewsletterDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testNewsletterDTO, result);
        verify(newsletterRepository, times(1)).save(any(Newsletter.class));
    }

    @Test
    void updateNewsletter_ExistingNewsletter_ShouldReturnUpdatedNewsletter() {
        // Arrange
        Users newUser = new Users();
        newUser.setId(newUserId);

        Newsletter updatedNewsletter = new Newsletter();
        updatedNewsletter.setId(testNewsletterId);
        updatedNewsletter.setTitle("Weekly Digest");
        updatedNewsletter.setDescription("Fresh updates");
        updatedNewsletter.setUser(newUser);

        when(newsletterRepository.findById(testNewsletterId)).thenReturn(Optional.of(testNewsletter));
        when(userService.getUserById(newUserId)).thenReturn(newUser);
        when(newsletterRepository.save(any(Newsletter.class))).thenReturn(updatedNewsletter);

        // Act
        ResponseEntity<Newsletter> result = newsletterService.updateNewsletter(testNewsletterId, updatedNewsletterDTO);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        Newsletter updatedResult = result.getBody();
        assertNotNull(updatedResult);
        assertEquals("Weekly Digest", updatedResult.getTitle());
        assertEquals("Fresh updates", updatedResult.getDescription());
        assertEquals(newUserId, updatedResult.getUser().getId());
        verify(newsletterRepository, times(1)).findById(testNewsletterId);
        verify(userService, times(1)).getUserById(newUserId);
        verify(newsletterRepository, times(1)).save(any(Newsletter.class));
    }

    @Test
    void updateNewsletter_NonExistingNewsletter_ShouldReturnNotFound() {
        // Arrange
        when(newsletterRepository.findById(testNewsletterId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Newsletter> result = newsletterService.updateNewsletter(testNewsletterId, updatedNewsletterDTO);

        // Assert
        assertNotNull(result);
        assertEquals(404, result.getStatusCodeValue());
        verify(newsletterRepository, times(1)).findById(testNewsletterId);
        verify(newsletterRepository, never()).save(any(Newsletter.class));
    }

    @Test
    void deleteNewsletter_ExistingNewsletter_ShouldReturnOk() {
        // Arrange
        when(newsletterRepository.findById(testNewsletterId)).thenReturn(Optional.of(testNewsletter));

        // Act
        ResponseEntity<Object> result = newsletterService.deleteNewsletter(testNewsletterId);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        verify(newsletterRepository, times(1)).findById(testNewsletterId);
        verify(newsletterRepository, times(1)).delete(testNewsletter);
    }

    @Test
    void deleteNewsletter_NonExistingNewsletter_ShouldReturnNotFound() {
        // Arrange
        when(newsletterRepository.findById(testNewsletterId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Object> result = newsletterService.deleteNewsletter(testNewsletterId);

        // Assert
        assertNotNull(result);
        assertEquals(404, result.getStatusCodeValue());
        verify(newsletterRepository, times(1)).findById(testNewsletterId);
        verify(newsletterRepository, never()).delete(any(Newsletter.class));
    }

    @Test
    @Transactional
    void applyPatchToNewsletter_ValidPatch_ShouldReturnPatchedNewsletter() throws Exception {
        // Arrange
        String patchJson = "[{\"op\":\"replace\",\"path\":\"/title\",\"value\":\"Weekly Update\"}]";
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchJson));

        when(newsletterRepository.findById(testNewsletterId)).thenReturn(Optional.of(testNewsletter));

        // Mock the objectMapper behavior
        ObjectMapper realMapper = new ObjectMapper();
        JsonNode newsletterNode = realMapper.valueToTree(testNewsletter);
        JsonNode patchedNode = patch.apply(newsletterNode);
        Newsletter patchedNewsletter = realMapper.treeToValue(patchedNode, Newsletter.class);
        patchedNewsletter.setId(testNewsletterId);

        when(objectMapper.valueToTree(any(Newsletter.class))).thenReturn(newsletterNode);
        when(objectMapper.treeToValue(any(JsonNode.class), eq(Newsletter.class))).thenReturn(patchedNewsletter);

        // Mock validation to pass
        Set<ConstraintViolation<Newsletter>> emptyViolations = Collections.emptySet();
        when(validator.validate(any(Newsletter.class))).thenReturn(emptyViolations);

        when(newsletterRepository.save(patchedNewsletter)).thenReturn(patchedNewsletter);

        // Act
        Newsletter result = newsletterService.applyPatchToNewsletter(patch, testNewsletterId);

        // Assert
        assertNotNull(result);
        assertEquals("Weekly Update", result.getTitle());
        assertEquals("Latest news", result.getDescription()); // unchanged
        verify(newsletterRepository, times(1)).findById(testNewsletterId);
        verify(newsletterRepository, times(1)).save(patchedNewsletter);
    }

    @Test
    @Transactional
    void applyPatchToNewsletter_InvalidUserIdPatch_ShouldThrowException() throws Exception {
        // Arrange
        String invalidPatchJson = "[{\"op\":\"replace\",\"path\":\"/user/invalidField\",\"value\":\"value\"}]";
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(invalidPatchJson));

        when(newsletterRepository.findById(testNewsletterId)).thenReturn(Optional.of(testNewsletter));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            newsletterService.applyPatchToNewsletter(patch, testNewsletterId);
        });
    }
}