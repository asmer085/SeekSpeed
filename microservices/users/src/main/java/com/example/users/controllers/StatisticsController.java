package com.example.users.controllers;

import com.example.users.entity.Statistics;
import com.example.users.services.StatisticsService;
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
    public @ResponseBody Statistics getStatisticsById(@PathVariable UUID statisticId) {
        return statisticsService.getStatisticsById(statisticId);
    }

    @PostMapping("/add")
    public @ResponseBody Statistics addStatistics(@RequestBody Statistics statistics) {
        return statisticsService.addStatistics(statistics);
    }

    @PutMapping("/{statisticId}")
    public @ResponseBody ResponseEntity<Statistics> updateStatistics(@PathVariable UUID statisticId, @RequestBody Statistics updatedStatistics) {
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
}
