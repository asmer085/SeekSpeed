package com.example.events;

import com.example.events.entity.Event;
import com.example.events.entity.Review;
import com.example.events.repository.EventRepository;
import com.example.events.repository.ReviewRepository;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class HibernateNPlusOneTest {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    void testNPlusOneProblem() {
        // Omogući i očisti statistiku
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        Statistics stats = sessionFactory.getStatistics();
        stats.setStatisticsEnabled(true);
        stats.clear();

        // Dohvati sve evente
        Iterable<Event> events = eventRepository.findAll();

        // Iteriraj i za svaki event dohvatiti recenzije (simulacija servisa)
        for (Event e : events) {
            List<Review> reviews = reviewRepository.findByEvent(e);
            // Simulacija rada sa recenzijama
            System.out.println("Event: " + e.getName() + " ima recenzija: " + reviews.size());
        }

        System.out.println("Broj SQL upita: " + stats.getPrepareStatementCount());
    }
}

