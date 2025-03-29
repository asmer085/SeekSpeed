package com.example.users.services;

import com.example.users.dtos.StatisticsDTO;
import com.example.users.entity.Statistics;
import com.example.users.entity.Type;
import com.example.users.entity.Users;
import com.example.users.mappers.StatisticsMapper;
import com.example.users.repository.StatisticsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceTests {

    @Mock
    private StatisticsRepository statisticsRepository;

    @Mock
    private StatisticsMapper statisticsMapper;

    @Mock
    private UserService userService;

    @Mock
    private TypeService typeService;

    @InjectMocks
    private StatisticsService statisticsService;

    private Statistics testStatistic;
    private StatisticsDTO testStatisticDTO;
    private UUID testStatisticId;
    private UUID testUserId;

    @BeforeEach
    void setUp() {
        testStatisticId = UUID.randomUUID();
        testUserId = UUID.randomUUID();

        testStatistic = new Statistics();
        testStatistic.setId(testStatisticId);
        testStatistic.setAveragePace(5.30);
        testStatistic.setBestPace(4.45);
        testStatistic.setTotalTime(3600.0);

        testStatisticDTO = new StatisticsDTO();
        testStatisticDTO.setAveragePace(5.30);
        testStatisticDTO.setBestPace(4.45);
        testStatisticDTO.setTotalTime(3600.0);
    }

    @Test
    void getAllStatistics_ShouldReturnAllStatistics() {
        // Arrange
        when(statisticsRepository.findAll()).thenReturn(Arrays.asList(testStatistic));

        // Act
        Iterable<Statistics> result = statisticsService.getAllStatistics();

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Statistics>) result).size());
        verify(statisticsRepository, times(1)).findAll();
    }

    @Test
    void getStatisticsById_ExistingId_ShouldReturnStatistic() {
        // Arrange
        when(statisticsRepository.findById(testStatisticId)).thenReturn(Optional.of(testStatistic));

        // Act
        Statistics result = statisticsService.getStatisticsById(testStatisticId);

        // Assert
        assertNotNull(result);
        assertEquals(testStatistic, result);
        verify(statisticsRepository, times(1)).findById(testStatisticId);
    }

    @Test
    void getStatisticsById_NonExistingId_ShouldThrowException() {
        // Arrange
        when(statisticsRepository.findById(testStatisticId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(StatisticsService.StatisticsNotFoundException.class, () -> {
            statisticsService.getStatisticsById(testStatisticId);
        });
        verify(statisticsRepository, times(1)).findById(testStatisticId);
    }

    @Test
    void addStatistics_ValidStatistic_ShouldReturnStatisticDTO() {
        // Arrange
        when(statisticsMapper.statisticsDTOToStatistics(any(StatisticsDTO.class), any(UserService.class), any(TypeService.class)))
                .thenReturn(testStatistic);
        when(statisticsRepository.save(any(Statistics.class))).thenReturn(testStatistic);
        when(statisticsMapper.statisticsToStatisticsDTO(any(Statistics.class))).thenReturn(testStatisticDTO);

        // Act
        StatisticsDTO result = statisticsService.addStatistics(testStatisticDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testStatisticDTO, result);
        verify(statisticsRepository, times(1)).save(any(Statistics.class));
    }

    @Test
    void updateStatistics_ExistingStatistic_ShouldReturnUpdatedStatistic() {
        // Arrange
        Statistics updatedStatistic = new Statistics();
        updatedStatistic.setAveragePace(5.00);
        updatedStatistic.setBestPace(4.50);
        updatedStatistic.setTotalTime(3500.0);
        updatedStatistic.setType(testStatistic.getType());

        when(statisticsRepository.findById(testStatisticId)).thenReturn(Optional.of(testStatistic));
        when(statisticsRepository.save(any(Statistics.class))).thenReturn(updatedStatistic);

        // Act
        ResponseEntity<Statistics> result = statisticsService.updateStatistics(testStatisticId, updatedStatistic);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        verify(statisticsRepository, times(1)).findById(testStatisticId);
        verify(statisticsRepository, times(1)).save(any(Statistics.class));
    }

    @Test
    void updateStatistics_NonExistingStatistic_ShouldReturnNotFound() {
        // Arrange
        when(statisticsRepository.findById(testStatisticId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Statistics> result = statisticsService.updateStatistics(testStatisticId, testStatistic);

        // Assert
        assertNotNull(result);
        assertEquals(404, result.getStatusCodeValue());
        verify(statisticsRepository, times(1)).findById(testStatisticId);
        verify(statisticsRepository, never()).save(any(Statistics.class));
    }

    @Test
    void deleteStatistics_ExistingStatistic_ShouldReturnOk() {
        // Arrange
        when(statisticsRepository.findById(testStatisticId)).thenReturn(Optional.of(testStatistic));

        // Act
        ResponseEntity<Object> result = statisticsService.deleteStatistics(testStatisticId);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        verify(statisticsRepository, times(1)).findById(testStatisticId);
        verify(statisticsRepository, times(1)).delete(testStatistic);
    }

    @Test
    void deleteStatistics_NonExistingStatistic_ShouldReturnNotFound() {
        // Arrange
        when(statisticsRepository.findById(testStatisticId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Object> result = statisticsService.deleteStatistics(testStatisticId);

        // Assert
        assertNotNull(result);
        assertEquals(404, result.getStatusCodeValue());
        verify(statisticsRepository, times(1)).findById(testStatisticId);
        verify(statisticsRepository, never()).delete(any(Statistics.class));
    }

    @Test
    void getStatisticsByUserId_WithStatistics_ShouldReturnStatistics() {
        // Arrange
        when(statisticsRepository.findByUserId(testUserId)).thenReturn(Arrays.asList(testStatistic));

        // Act
        ResponseEntity<List<Statistics>> result = statisticsService.getStatisticsByUserId(testUserId);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(1, result.getBody().size());
        verify(statisticsRepository, times(1)).findByUserId(testUserId);
    }

    @Test
    void getStatisticsByUserId_NoStatistics_ShouldReturnNotFound() {
        // Arrange
        when(statisticsRepository.findByUserId(testUserId)).thenReturn(List.of());

        // Act
        ResponseEntity<List<Statistics>> result = statisticsService.getStatisticsByUserId(testUserId);

        // Assert
        assertNotNull(result);
        assertEquals(404, result.getStatusCodeValue());
        verify(statisticsRepository, times(1)).findByUserId(testUserId);
    }
}