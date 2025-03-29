package com.example.users.controllers;

import com.example.users.dtos.StatisticsDTO;
import com.example.users.entity.Statistics;
import com.example.users.services.StatisticsService;
import com.example.users.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/all")
    public @ResponseBody Iterable<Statistics> getStatistics() {
        return statisticsService.getAllStatistics();
    }

    @GetMapping("/{statisticId}")
    public ResponseEntity<Statistics> getStatisticsById(@PathVariable UUID statisticId) {
        try {
            Statistics stat = statisticsService.getStatisticsById(statisticId);
            return ResponseEntity.ok(stat);
        } catch (StatisticsService.StatisticsNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<StatisticsDTO> addStatistics(@Valid @RequestBody StatisticsDTO statisticsDTO) {
        StatisticsDTO createdStats = statisticsService.addStatistics(statisticsDTO);
        return ResponseEntity.ok(createdStats);
    }

    @PutMapping("/{statisticId}")
    public ResponseEntity<Statistics> updateStatistics(
            @PathVariable UUID statisticId,
            @Valid @RequestBody Statistics updatedStatistics) {
        return statisticsService.updateStatistics(statisticId, updatedStatistics);
    }

    @DeleteMapping("/{statisticId}")
    public @ResponseBody ResponseEntity<Object> deleteStatistics(@PathVariable UUID statisticId) {
        return statisticsService.deleteStatistics(statisticId);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Statistics>> getStatisticsByUserId(@PathVariable UUID userId) {
        return statisticsService.getStatisticsByUserId(userId);
    }

    @ExceptionHandler(StatisticsService.StatisticsNotFoundException.class)
    public ResponseEntity<Object> handleStatisticsNotFound(StatisticsService.StatisticsNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }
}
