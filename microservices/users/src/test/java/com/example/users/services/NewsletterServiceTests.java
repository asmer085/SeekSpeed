package com.example.users.services;

import com.example.users.dtos.NewsletterDTO;
import com.example.users.entity.Newsletter;
import com.example.users.entity.Users;
import com.example.users.mappers.NewsletterMapper;
import com.example.users.repository.NewsletterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    private Newsletter testNewsletter;
    private NewsletterDTO testNewsletterDTO;
    private UUID testNewsletterId;
    private UUID testUserId;

    @BeforeEach
    void setUp() {
        testNewsletterId = UUID.randomUUID();
        testUserId = UUID.randomUUID();

        Users testUser = new Users();
        testUser.setId(testUserId);

        testNewsletter = new Newsletter();
        testNewsletter.setId(testNewsletterId);
        testNewsletter.setTitle("Monthly Update");
        testNewsletter.setDescription("Latest news");
        testNewsletter.setUser(testUser);

        testNewsletterDTO = new NewsletterDTO();
        testNewsletterDTO.setTitle("Monthly Update");
        testNewsletterDTO.setDescription("Latest news");
        testNewsletterDTO.setUserId(testUserId);
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
        Newsletter updatedNewsletter = new Newsletter();
        updatedNewsletter.setTitle("Updated Title");

        when(newsletterRepository.findById(testNewsletterId)).thenReturn(Optional.of(testNewsletter));
        when(newsletterRepository.save(any(Newsletter.class))).thenReturn(updatedNewsletter);

        // Act
        ResponseEntity<Newsletter> result = newsletterService.updateNewsletter(testNewsletterId, updatedNewsletter);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        verify(newsletterRepository, times(1)).findById(testNewsletterId);
        verify(newsletterRepository, times(1)).save(any(Newsletter.class));
    }

    @Test
    void updateNewsletter_NonExistingNewsletter_ShouldReturnNotFound() {
        // Arrange
        when(newsletterRepository.findById(testNewsletterId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Newsletter> result = newsletterService.updateNewsletter(testNewsletterId, testNewsletter);

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
}