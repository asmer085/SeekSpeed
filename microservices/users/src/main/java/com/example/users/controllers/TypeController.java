package com.example.users.controllers;

import com.example.users.entity.Type;
import com.example.users.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/type")
public class TypeController {
    
    @Autowired
    private TypeRepository typeRepository;
    
    @GetMapping("/all")
    public @ResponseBody Iterable<Type> getAllTypes() { return typeRepository.findAll(); }
    
    @GetMapping("/{typeId}")
    public @ResponseBody Type getTypeById(@PathVariable UUID typeId) {
        return typeRepository.findById(typeId).orElseThrow(() -> new IllegalArgumentException("Type not found"));
    }
    
    @PostMapping("/add")
    public @ResponseBody Type addType(@RequestBody Type type) {
        return typeRepository.save(type);
    }

    @DeleteMapping("/{typeId}")
    public @ResponseBody ResponseEntity<Object> deleteType(@PathVariable UUID typeId) {
        return typeRepository.findById(typeId)
                .map(type -> {
                    typeRepository.delete(type);
                    return ResponseEntity.ok().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{typeId}")
    public @ResponseBody ResponseEntity<Type> updateType(@PathVariable UUID typeId, @RequestBody Type updatedType) {
        return typeRepository.findById(typeId)
                .map(type -> {
                    if (updatedType.getDistance() != null) type.setDistance(updatedType.getDistance());
                    if (updatedType.getResults() != null) type.setResults(updatedType.getResults());
                    return ResponseEntity.ok(typeRepository.save(type));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
