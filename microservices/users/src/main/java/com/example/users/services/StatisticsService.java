package com.example.users.services;

import com.example.users.dtos.StatisticsDTO;
import com.example.users.entity.Statistics;
import com.example.users.mappers.StatisticsMapper;
import com.example.users.repository.StatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public ResponseEntity<Statistics> updateStatistics(UUID statisticId, Statistics updatedStatistics) {
        return statisticsRepository.findById(statisticId)
                .map(stat -> {
                    if (updatedStatistics.getAveragePace() != 0) stat.setAveragePace(updatedStatistics.getAveragePace());
                    if (updatedStatistics.getBestPace() != 0) stat.setBestPace(updatedStatistics.getBestPace());
                    if (updatedStatistics.getTotalTime() != 0) stat.setTotalTime(updatedStatistics.getTotalTime());
                    stat.setType(updatedStatistics.getType());
                    return ResponseEntity.ok(statisticsRepository.save(stat));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
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
