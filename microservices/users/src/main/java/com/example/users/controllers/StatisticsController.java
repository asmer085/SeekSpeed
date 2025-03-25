package com.example.users.controllers;

import com.example.users.entity.Newsletter;
import com.example.users.entity.Statistics;
import com.example.users.repository.StatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsRepository statisticsRepository;

    @GetMapping("/all")
    public @ResponseBody Iterable<Statistics> getStatistics() {return statisticsRepository.findAll();}

    @GetMapping("/{statisticId}")
    public @ResponseBody Statistics getStatisticsById(@PathVariable UUID statisticId) {
        return statisticsRepository.findById(statisticId).orElseThrow(() -> new RuntimeException("Statistic not found"));
    }

    @PostMapping("/add")
    public @ResponseBody Statistics addStatistics(@RequestBody Statistics statistics) {
        return statisticsRepository.save(statistics);
    }

    @PutMapping("/{statisticId}")
    public @ResponseBody ResponseEntity<Statistics> updateNewsletter(@PathVariable UUID statisticId, @RequestBody Statistics updatedStatistics) {
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

    @DeleteMapping("/{statisticId}")
    public @ResponseBody ResponseEntity<Object> deleteStatistics(@PathVariable UUID statisticId) {
        return statisticsRepository.findById(statisticId)
                .map(stat -> {
                    statisticsRepository.delete(stat);
                    return ResponseEntity.ok().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Statistics>> getStatisticsByUserId(@PathVariable UUID userId) {
        List<Statistics> userStatistics = statisticsRepository.findByUserId(userId);
        return userStatistics.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(userStatistics);
    }

}
