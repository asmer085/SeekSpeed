package com.example.users.repository;

import com.example.users.entity.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.List;

public interface StatisticsRepository extends JpaRepository<Statistics, UUID> {
    List<Statistics> findByUserId(UUID userId);
}
