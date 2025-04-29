package com.example.events.controller;

import com.example.events.dto.TypeDTO;
import com.example.events.entity.Type;
import com.example.events.service.TypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/types")
@RequiredArgsConstructor
public class TypeController {
    private final TypeService typeService;

    @PostMapping
    public Type createType(@Valid @RequestBody TypeDTO typeDTO) {
        return typeService.createType(typeDTO);
    }

    @GetMapping
    public Iterable<Type> getAllTypes() {
        return typeService.getAllTypes();
    }

    @GetMapping("/event/{eventId}")
    public List<Type> getTypesByEventId(@PathVariable UUID eventId) {
        return typeService.getTypesByEventId(eventId);
    }

    @PutMapping("/batch")
    public List<Type> updateTypes(@RequestBody List<TypeDTO> typeDTOs) {
        return typeService.updateTypes(typeDTOs);
    }

    @GetMapping("/byDistance")
    public List<Type> getTypesByDistance(@RequestParam UUID eventId, @RequestParam double minDistance) {
        return typeService.getTypesByEventIdAndMinDistance(eventId, minDistance);
    }

}