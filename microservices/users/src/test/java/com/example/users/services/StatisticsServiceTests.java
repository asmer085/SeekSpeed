package com.example.users.services;

import com.example.users.dto.StatisticsDTO;
import com.example.users.entity.Statistics;
import com.example.users.entity.Type;
import com.example.users.entity.Users;
import com.example.users.mappers.StatisticsMapper;
import com.example.users.repository.StatisticsRepository;
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

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Validator validator;

    private Statistics testStatistic;
    private StatisticsDTO testStatisticDTO;
    private StatisticsDTO updatedStatisticDTO;
    private UUID testStatisticId;
    private UUID testUserId;
    private UUID testTypeId;

    @BeforeEach
    void setUp() {
        testStatisticId = UUID.randomUUID();
        testUserId = UUID.randomUUID();
        testTypeId = UUID.randomUUID();

        testStatistic = new Statistics();
        testStatistic.setId(testStatisticId);
        testStatistic.setAveragePace(5.30);
        testStatistic.setBestPace(4.45);
        testStatistic.setTotalTime(3600.0);

        testStatisticDTO = new StatisticsDTO();
        testStatisticDTO.setAveragePace(5.30);
        testStatisticDTO.setBestPace(4.45);
        testStatisticDTO.setTotalTime(3600.0);

        updatedStatisticDTO = new StatisticsDTO();
        updatedStatisticDTO.setAveragePace(5.00);
        updatedStatisticDTO.setBestPace(4.50);
        updatedStatisticDTO.setTotalTime(3500.0);
        updatedStatisticDTO.setUserId(testUserId);
        updatedStatisticDTO.setTypeId(testTypeId);
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
        Users testUser = new Users();
        testUser.setId(testUserId);
        Type testType = new Type();
        testType.setId(testTypeId);

        Statistics updatedStatistic = new Statistics();
        updatedStatistic.setId(testStatisticId);
        updatedStatistic.setAveragePace(5.00);
        updatedStatistic.setBestPace(4.50);
        updatedStatistic.setTotalTime(3500.0);
        updatedStatistic.setUser(testUser);
        updatedStatistic.setType(testType);

        when(statisticsRepository.findById(testStatisticId)).thenReturn(Optional.of(testStatistic));
        when(userService.getUserById(testUserId)).thenReturn(testUser);
        when(typeService.getTypeById(testTypeId)).thenReturn(testType);
        when(statisticsRepository.save(any(Statistics.class))).thenReturn(updatedStatistic);

        // Act
        ResponseEntity<Statistics> result = statisticsService.updateStatistics(testStatisticId, updatedStatisticDTO);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        Statistics updatedResult = result.getBody();
        assertNotNull(updatedResult);
        assertEquals(5.00, updatedResult.getAveragePace());
        assertEquals(4.50, updatedResult.getBestPace());
        assertEquals(3500.0, updatedResult.getTotalTime());
        assertEquals(testUserId, updatedResult.getUser().getId());
        assertEquals(testTypeId, updatedResult.getType().getId());
        verify(statisticsRepository, times(1)).findById(testStatisticId);
        verify(statisticsRepository, times(1)).save(any(Statistics.class));
    }

    @Test
    void updateStatistics_NonExistingStatistic_ShouldReturnNotFound() {
        // Arrange
        when(statisticsRepository.findById(testStatisticId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Statistics> result = statisticsService.updateStatistics(testStatisticId, updatedStatisticDTO);

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


    @Test
    @Transactional
    void applyPatchToStatistics_ValidPatch_ShouldReturnPatchedStatistics() throws Exception {
        // Arrange
        String patchJson = "[{\"op\":\"replace\",\"path\":\"/averagePace\",\"value\":6.0}]";
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchJson));

        Statistics originalStat = new Statistics();
        originalStat.setId(testStatisticId);
        originalStat.setAveragePace(5.30);
        originalStat.setBestPace(4.45);
        originalStat.setTotalTime(3600.0);

        when(statisticsRepository.findById(testStatisticId)).thenReturn(Optional.of(originalStat));

        // Mock the objectMapper behavior
        ObjectMapper realMapper = new ObjectMapper();
        JsonNode statNode = realMapper.valueToTree(originalStat);
        JsonNode patchedNode = patch.apply(statNode);
        Statistics patchedStat = realMapper.treeToValue(patchedNode, Statistics.class);
        patchedStat.setId(testStatisticId);

        when(objectMapper.valueToTree(any(Statistics.class))).thenReturn(statNode);
        when(objectMapper.treeToValue(any(JsonNode.class), eq(Statistics.class))).thenReturn(patchedStat);

        // Mock validation to pass
        Set<ConstraintViolation<Statistics>> emptyViolations = Collections.emptySet();
        when(validator.validate(any(Statistics.class))).thenReturn(emptyViolations);

        when(statisticsRepository.save(patchedStat)).thenReturn(patchedStat);

        // Act
        Statistics result = statisticsService.applyPatchToStatistics(patch, testStatisticId);

        // Assert
        assertNotNull(result);
        assertEquals(6.0, result.getAveragePace());
        assertEquals(4.45, result.getBestPace()); // unchanged
        verify(statisticsRepository, times(1)).findById(testStatisticId);
        verify(statisticsRepository, times(1)).save(patchedStat);
    }

    @Test
    @Transactional
    void applyPatchToStatistics_InvalidPatchOperation_ShouldThrowException() throws Exception {
        // Arrange
        String invalidPatchJson = "[{\"op\":\"replace\",\"path\":\"/invalidField\",\"value\":\"value\"}]";
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(invalidPatchJson));

        Statistics originalStat = new Statistics();
        originalStat.setId(testStatisticId);
        originalStat.setAveragePace(5.30);

        when(statisticsRepository.findById(testStatisticId)).thenReturn(Optional.of(originalStat));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            statisticsService.applyPatchToStatistics(patch, testStatisticId);
        });
    }
}