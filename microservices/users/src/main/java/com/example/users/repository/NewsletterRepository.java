package com.example.users.repository;

import com.example.users.entity.Newsletter;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface NewsletterRepository extends CrudRepository<Newsletter, UUID> {
}
