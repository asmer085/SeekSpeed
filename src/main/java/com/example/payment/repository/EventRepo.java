package com.example.payment.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.payment.entity.Event;
@Repository
public interface EventRepo extends JpaRepository<Event, Long> {
}
