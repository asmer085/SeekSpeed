package com.example.events.controller;

import com.example.events.dto.TypeDTO;
import com.example.events.entity.Types;
import com.example.events.service.TypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/types")
@RequiredArgsConstructor
public class TypeController {
    private final TypeService typeService;

    @PostMapping
    public Types createType(@Valid @RequestBody TypeDTO typeDTO) {
        return typeService.createType(typeDTO);
    }

    @GetMapping
    public Iterable<Types> getAllTypes() {
        return typeService.getAllTypes();
    }

    @GetMapping("/event/{eventId}")
    public List<Types> getTypesByEventId(@PathVariable UUID eventId) {
        return typeService.getTypesByEventId(eventId);
    }

    @PutMapping("/{typeId}")
    public @ResponseBody ResponseEntity<Types> updateType(@PathVariable UUID typeId, @Valid @RequestBody TypeDTO typeDTO) {
        return typeService.updateType(typeId, typeDTO);
    }

    @GetMapping("/byDistance")
    public List<Types> getTypesByDistance(@RequestParam UUID eventId, @RequestParam double minDistance) {
        return typeService.getTypesByEventIdAndMinDistance(eventId, minDistance);
    }

}