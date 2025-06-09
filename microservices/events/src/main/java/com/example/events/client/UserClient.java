package com.example.events.client;

import com.example.events.dto.UserEventDTO;
import com.example.events.external.Users;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "users")
public interface UserClient {

    @GetMapping("/users/{userId}")
    Users getUserById(@PathVariable("userId") UUID userId);

    @PostMapping("/api/user-events")
    void sendUserEvent(@RequestBody UserEventDTO userEventDTO);
}
