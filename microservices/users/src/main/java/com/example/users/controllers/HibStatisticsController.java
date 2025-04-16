package com.example.users.controllers;

import com.example.users.services.HibStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/hib-statistics")
public class HibStatisticsController {

    @Autowired
    private HibStatisticsService hibStatisticsService;

    @GetMapping("/test/statistics/")
    public void testStatistics() {
        hibStatisticsService.checkNPlusOneProblemForStatistics();
    }

    @GetMapping("/test/newsletter/{newsId}")
    public void testNewsletter() {
        hibStatisticsService.checkNPlusOneProblemForNewsletter();
    }
}
