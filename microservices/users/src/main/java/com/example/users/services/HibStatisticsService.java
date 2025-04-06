package com.example.users.services;

import com.example.users.entity.Newsletter;
import com.example.users.entity.Statistics;
import com.example.users.repository.NewsletterRepository;
import com.example.users.repository.StatisticsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class HibStatisticsService {
    private final StatisticsRepository statisticsRepository;
    private final NewsletterRepository newsletterRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public HibStatisticsService(StatisticsRepository statisticsRepository, NewsletterRepository newsletterRepository) {
        this.statisticsRepository = statisticsRepository;
        this.newsletterRepository = newsletterRepository;
    }

    public void checkNPlusOneProblemForStatistics(UUID statId) {
        int queryCountBefore = getQueryExecutionCount();

        List<Statistics> stats = statisticsRepository.findByUserId(statId);
        for (Statistics stat : stats) {
            System.out.println(stat.getUser().getFirstName());
        }

        int queryCountAfter = getQueryExecutionCount();
        int queryCount = queryCountAfter - queryCountBefore;
        System.out.println("Executed queries for Statistics: " + queryCount);

        if (queryCount > 1) {
            System.out.println("Potential N+1 Problem detected for Statistics!");
        } else {
            System.out.println("No N+1 Problem detected for Statistics.");
        }
    }

    public void checkNPlusOneProblemForNewsletter(UUID newsId) {
        int queryCountBefore = getQueryExecutionCount();

        newsletterRepository.findById(newsId).ifPresent(newsletter -> {
            System.out.println(newsletter.getUser().getFirstName());
        });

        int queryCountAfter = getQueryExecutionCount();
        int queryCount = queryCountAfter - queryCountBefore;
        System.out.println("Executed queries for Newsletter: " + queryCount);

        if (queryCount > 1) {
            System.out.println("Potential N+1 Problem detected for Newsletter!");
        } else {
            System.out.println("No N+1 Problem detected for Newsletter.");
        }
    }

    private int getQueryExecutionCount() {
        Session session = entityManager.unwrap(Session.class);
        return (int) session.getSessionFactory().getStatistics().getQueryExecutionCount();
    }
}
