package com.example.users.controllers;

import com.example.users.entity.Type;
import com.example.users.services.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/type")
public class TypeController {

    @Autowired
    private TypeService typeService;

    @GetMapping("/all")
    public @ResponseBody Iterable<Type> getAllTypes() {
        return typeService.getAllTypes();
    }

    @GetMapping("/{typeId}")
    public @ResponseBody Type getTypeById(@PathVariable UUID typeId) {
        return typeService.getTypeById(typeId);
    }

    @PostMapping("/add")
    public @ResponseBody Type addType(@RequestBody Type type) {
        return typeService.addType(type);
    }

    @PutMapping("/{typeId}")
    public @ResponseBody ResponseEntity<Type> updateType(@PathVariable UUID typeId, @RequestBody Type updatedType) {
        return typeService.updateType(typeId, updatedType);
    }

    @DeleteMapping("/{typeId}")
    public @ResponseBody ResponseEntity<Object> deleteType(@PathVariable UUID typeId) {
        return typeService.deleteType(typeId);
    }
}
