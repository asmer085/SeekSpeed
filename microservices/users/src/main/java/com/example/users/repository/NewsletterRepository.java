package com.example.users.repository;

import com.example.users.entity.Newsletter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NewsletterRepository extends JpaRepository<Newsletter, UUID> {
}
