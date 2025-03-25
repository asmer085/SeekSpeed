package com.example.users.repository;

import com.example.users.entity.Statistics;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;
import java.util.List;

public interface StatisticsRepository extends CrudRepository<Statistics, UUID> {
    List<Statistics> findByUserId(UUID userId);
}
