package com.example.events.controller;

import com.example.events.external.Users;
import com.example.events.service.UserEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user-events")
@RequiredArgsConstructor
public class UserEventController {

    private final UserEventService userEventService;

    @GetMapping("/user/{userId}/events")
    public List<UUID> getEventIdsForUser(@PathVariable UUID userId) {
        return userEventService.getEventIdsByUserId(userId);
    }

    @PostMapping("/user-events")
    public void syncUserEventsToUsersService() {
        userEventService.syncAllUserEventsToUsersService();
    }
}

