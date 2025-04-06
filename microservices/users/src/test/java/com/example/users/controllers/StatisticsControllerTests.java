package com.example.users.controllers;

import com.example.users.dtos.StatisticsDTO;
import com.example.users.entity.Statistics;
import com.example.users.services.StatisticsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class StatisticsControllerTests {

    @Mock
    private StatisticsService statisticsService;

    @InjectMocks
    private StatisticsController statisticsController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

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

        mockMvc = MockMvcBuilders.standaloneSetup(statisticsController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllStatistics_ShouldReturnStatistics() throws Exception {
        // Arrange
        Iterable<Statistics> statistics = Arrays.asList(testStatistic);
        given(statisticsService.getAllStatistics()).willReturn(statistics);

        // Act & Assert
        mockMvc.perform(get("/statistics/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].averagePace").value(5.30))
                .andExpect(jsonPath("$[0].bestPace").value(4.45))
                .andExpect(jsonPath("$[0].totalTime").value(3600));
    }

    @Test
    void getStatisticsById_ExistingId_ShouldReturnStatistic() throws Exception {
        // Arrange
        given(statisticsService.getStatisticsById(testStatisticId)).willReturn(testStatistic);

        // Act & Assert
        mockMvc.perform(get("/statistics/{statisticId}", testStatisticId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averagePace").value(5.30))
                .andExpect(jsonPath("$.bestPace").value(4.45))
                .andExpect(jsonPath("$.totalTime").value(3600));
    }

    @Test
    void getStatisticsById_NonExistingId_ShouldThrowException() throws Exception {
        // Arrange
        given(statisticsService.getStatisticsById(testStatisticId))
                .willThrow(new StatisticsService.StatisticsNotFoundException("Statistics not found"));

        // Act & Assert
        mockMvc.perform(get("/statistics/{statisticId}", testStatisticId))
                .andExpect(status().isNotFound());
    }

    @Test
    void addStatistics_ValidStatistic_ShouldReturnCreatedStatistic() throws Exception {
        // Arrange
        given(statisticsService.addStatistics(any(StatisticsDTO.class))).willReturn(testStatisticDTO);

        // Act & Assert
        mockMvc.perform(post("/statistics/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testStatisticDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averagePace").value(5.30))
                .andExpect(jsonPath("$.bestPace").value(4.45))
                .andExpect(jsonPath("$.totalTime").value(3600));
    }

    @Test
    void updateStatistics_ExistingStatistic_ShouldReturnUpdatedStatistic() throws Exception {
        // Arrange
        Statistics updatedStatistic = new Statistics();
        updatedStatistic.setAveragePace(5.00);
        updatedStatistic.setBestPace(4.30);
        updatedStatistic.setTotalTime(3500.0);

        given(statisticsService.updateStatistics(eq(testStatisticId), any(StatisticsDTO.class)))
                .willReturn(ResponseEntity.ok(updatedStatistic));

        // Act & Assert
        mockMvc.perform(put("/statistics/{statisticId}", testStatisticId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedStatistic)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averagePace").value(5.00))
                .andExpect(jsonPath("$.bestPace").value(4.30))
                .andExpect(jsonPath("$.totalTime").value(3500));
    }

    @Test
    void updateStatistics_NonExistingStatistic_ShouldReturnNotFound() throws Exception {
        // Arrange
        given(statisticsService.updateStatistics(eq(testStatisticId), any(StatisticsDTO.class)))
                .willReturn(ResponseEntity.notFound().build());

        // Act & Assert
        mockMvc.perform(put("/statistics/{statisticId}", testStatisticId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testStatistic)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteStatistics_ExistingStatistic_ShouldReturnOk() throws Exception {
        // Arrange
        given(statisticsService.deleteStatistics(testStatisticId))
                .willReturn(ResponseEntity.ok().build());

        // Act & Assert
        mockMvc.perform(delete("/statistics/{statisticId}", testStatisticId))
                .andExpect(status().isOk());
    }

    @Test
    void deleteStatistics_NonExistingStatistic_ShouldReturnNotFound() throws Exception {
        // Arrange
        given(statisticsService.deleteStatistics(testStatisticId))
                .willReturn(ResponseEntity.notFound().build());

        // Act & Assert
        mockMvc.perform(delete("/statistics/{statisticId}", testStatisticId))
                .andExpect(status().isNotFound());
    }

    @Test
    void getStatisticsByUserId_WithStatistics_ShouldReturnStatistics() throws Exception {
        // Arrange
        List<Statistics> userStatistics = Arrays.asList(testStatistic);
        given(statisticsService.getStatisticsByUserId(testUserId))
                .willReturn(ResponseEntity.ok(userStatistics));

        // Act & Assert
        mockMvc.perform(get("/statistics/user/{userId}", testUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].averagePace").value(5.30))
                .andExpect(jsonPath("$[0].bestPace").value(4.45))
                .andExpect(jsonPath("$[0].totalTime").value(3600));
    }

    @Test
    void getStatisticsByUserId_NoStatistics_ShouldReturnNotFound() throws Exception {
        // Arrange
        given(statisticsService.getStatisticsByUserId(testUserId))
                .willReturn(ResponseEntity.notFound().build());

        // Act & Assert
        mockMvc.perform(get("/statistics/user/{userId}", testUserId))
                .andExpect(status().isNotFound());
    }

    @Test
    void patchUpdateStatistics_ValidPatch_ShouldReturnUpdatedStatistics() throws Exception {
        // Arrange
        String patchJson = "[{\"op\":\"replace\",\"path\":\"/averagePace\",\"value\":6.0}]";

        Statistics patchedStatistic = new Statistics();
        patchedStatistic.setId(testStatisticId);
        patchedStatistic.setAveragePace(6.0);
        patchedStatistic.setBestPace(4.45);
        patchedStatistic.setTotalTime(3600.0);

        given(statisticsService.applyPatchToStatistics(any(), eq(testStatisticId))).willReturn(patchedStatistic);

        // Act & Assert
        mockMvc.perform(patch("/statistics/{statisticId}", testStatisticId)
                        .contentType("application/json-patch+json")
                        .content(patchJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averagePace").value(6.0))
                .andExpect(jsonPath("$.bestPace").value(4.45))
                .andExpect(jsonPath("$.totalTime").value(3600.0));
    }

    @Test
    void patchUpdateStatistics_InvalidPatch_ShouldReturnBadRequest() throws Exception {
        // Arrange
        String invalidPatchJson = "[{\"op\":\"replace\",\"path\":\"/averagePace\",\"value\":-1.0}]";

        given(statisticsService.applyPatchToStatistics(any(), eq(testStatisticId)))
                .willThrow(new RuntimeException("Validation failed: Average pace must be positive or zero"));

        // Act & Assert
        mockMvc.perform(patch("/statistics/{statisticId}", testStatisticId)
                        .contentType("application/json-patch+json")
                        .content(invalidPatchJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Validation failed: Average pace must be positive or zero"));
    }
}