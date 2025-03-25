package com.example.users.controllers;

import com.example.users.entity.Equipment;
import com.example.users.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/equipment")
public class EquipmentController {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @GetMapping("/all")
    public @ResponseBody Iterable<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    @GetMapping("/{equipmentId}")
    public @ResponseBody Equipment getEquipmentById(@PathVariable UUID equipmentId) {
        return equipmentRepository.findById(equipmentId).orElseThrow(() -> new RuntimeException("Equipment not found"));
    }

    @PostMapping("/add")
    public Equipment createEquipment(@RequestBody Equipment equipment) {
        return equipmentRepository.save(equipment);
    }

    @PutMapping("/{equipmentId}")
    public @ResponseBody ResponseEntity<Equipment> updateEquipment(@PathVariable UUID equipmentId, @RequestBody Equipment updatedEquipment) {
        return equipmentRepository.findById(equipmentId)
                .map(equipment -> {
                    if(updatedEquipment.getName() != null) equipment.setName(updatedEquipment.getName());
                    if(updatedEquipment.getQuantity() != 0) equipment.setQuantity(updatedEquipment.getQuantity());
                    return ResponseEntity.ok(equipmentRepository.save(equipment));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{equipmentId}")
    public @ResponseBody ResponseEntity<Object> deleteEquipment(@PathVariable UUID equipmentId) {
        return equipmentRepository.findById(equipmentId)
                .map(equipment -> {
                    equipmentRepository.delete(equipment);
                    return ResponseEntity.ok().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}