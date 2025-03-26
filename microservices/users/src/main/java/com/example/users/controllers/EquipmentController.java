package com.example.users.controllers;

import com.example.users.entity.Equipment;
import com.example.users.services.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/equipment")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    @GetMapping("/all")
    public @ResponseBody Iterable<Equipment> getAllEquipment() {
        return equipmentService.getAllEquipment();
    }

    @GetMapping("/{equipmentId}")
    public @ResponseBody Equipment getEquipmentById(@PathVariable UUID equipmentId) {
        return equipmentService.getEquipmentById(equipmentId);
    }

    @PostMapping("/add")
    public Equipment createEquipment(@RequestBody Equipment equipment) {
        return equipmentService.createEquipment(equipment);
    }

    @PutMapping("/{equipmentId}")
    public @ResponseBody ResponseEntity<Equipment> updateEquipment(@PathVariable UUID equipmentId, @RequestBody Equipment updatedEquipment) {
        return equipmentService.updateEquipment(equipmentId, updatedEquipment);
    }

    @DeleteMapping("/{equipmentId}")
    public @ResponseBody ResponseEntity<Object> deleteEquipment(@PathVariable UUID equipmentId) {
        return equipmentService.deleteEquipment(equipmentId);
    }
}
