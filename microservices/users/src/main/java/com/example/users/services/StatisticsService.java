package com.example.users.services;

import com.example.users.dto.StatisticsDTO;
import com.example.users.entity.Statistics;
import com.example.users.entity.Type;
import com.example.users.entity.Users;
import com.example.users.mappers.StatisticsMapper;
import com.example.users.repository.StatisticsRepository;
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

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class StatisticsService {

    @Autowired
    private StatisticsRepository statisticsRepository;

    @Autowired
    private StatisticsMapper statisticsMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Validator validator;

    public Iterable<Statistics> getAllStatistics() {
        return statisticsRepository.findAll();
    }

    public Statistics getStatisticsById(UUID statisticId) {
        return statisticsRepository.findById(statisticId)
                .orElseThrow(() -> new StatisticsNotFoundException("Statistics with id " + statisticId + " not found"));
    }

    public StatisticsDTO addStatistics(StatisticsDTO statisticsDTO) {
        Statistics stat = statisticsMapper.statisticsDTOToStatistics(statisticsDTO, userService, typeService);
        Statistics savedStat = statisticsRepository.save(stat);
        return statisticsMapper.statisticsToStatisticsDTO(savedStat);
    }

    @Transactional
    public ResponseEntity<Statistics> updateStatistics(UUID statisticId, StatisticsDTO updatedStatistics) {
        return statisticsRepository.findById(statisticId)
                .map(stat -> {
                    if (updatedStatistics.getAveragePace() != 0) stat.setAveragePace(updatedStatistics.getAveragePace());
                    if (updatedStatistics.getBestPace() != 0) stat.setBestPace(updatedStatistics.getBestPace());
                    if (updatedStatistics.getTotalTime() != 0) stat.setTotalTime(updatedStatistics.getTotalTime());
                    if (updatedStatistics.getTypeId() != null) {
                        Type newType = typeService.getTypeById(updatedStatistics.getTypeId());
                        stat.setType(newType);
                    }
                    if (updatedStatistics.getUserId() != null) {
                        Users newUser = userService.getUserById(updatedStatistics.getUserId());
                        stat.setUser(newUser);
                    }
                    return ResponseEntity.ok(statisticsRepository.save(stat));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    public Statistics applyPatchToStatistics(JsonPatch patch, UUID statisticId) {
        try {
            Statistics statistics = statisticsRepository.findById(statisticId)
                    .orElseThrow(() -> new StatisticsNotFoundException("Statistics with id " + statisticId + " not found"));
            JsonNode statisticsNode = objectMapper.valueToTree(statistics);
            JsonNode patchedNode = patch.apply(statisticsNode);
            Statistics patchedStatistics = objectMapper.treeToValue(patchedNode, Statistics.class);

            Set<ConstraintViolation<Statistics>> violations = validator.validate(patchedStatistics);
            if (!violations.isEmpty()) {
                StringBuilder errorMessage = new StringBuilder("Validation failed: ");
                for (ConstraintViolation<Statistics> violation : violations) {
                    errorMessage.append(violation.getMessage()).append(" ");
                }
                throw new RuntimeException(errorMessage.toString());
            }
            return statisticsRepository.save(patchedStatistics);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public ResponseEntity<Object> deleteStatistics(UUID statisticId) {
        return statisticsRepository.findById(statisticId)
                .map(stat -> {
                    statisticsRepository.delete(stat);
                    return ResponseEntity.ok().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<Statistics>> getStatisticsByUserId(UUID userId) {
        List<Statistics> userStatistics = statisticsRepository.findByUserId(userId);
        return userStatistics.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(userStatistics);
    }

    public static class StatisticsNotFoundException extends RuntimeException {
        public StatisticsNotFoundException(String message) {
            super(message);
        }
    }
}
