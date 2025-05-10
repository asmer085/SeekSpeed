package com.example.users.controllers;

import com.example.users.dtos.UserEventDTO;
import com.example.users.entity.UserEvent;
import com.example.users.entity.Users;
import com.example.users.services.UserEventService;
import com.example.users.services.UserService;
import org.mapstruct.control.MappingControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserEventController {

    @Autowired
    private UserEventService userEventService;

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public @ResponseBody Iterable<UserEvent> getAll() {
        return userEventService.getAll();
    }


    @PostMapping("/add")
    public @ResponseBody ResponseEntity<UserEventDTO> createUserEvent(@RequestBody UserEventDTO userEvent) {
        UserEventDTO createdUserEvent = userEventService.createUserEvent(userEvent);
        return ResponseEntity.ok(createdUserEvent);
    }

}
