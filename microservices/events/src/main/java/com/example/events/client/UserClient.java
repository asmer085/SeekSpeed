package com.example.events.client;

import com.example.events.external.Users; // DTO kopija iz user-service
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "users")
public interface UserClient {

    @GetMapping("/users/{userId}")
    Users getUserById(@PathVariable("userId") UUID userId);
}
