package com.example.users.controllers;

import com.example.users.dtos.EquipmentDTO;
import com.example.users.entity.Equipment;
import com.example.users.services.EquipmentService;
import com.example.users.services.UserService;
import jakarta.validation.Valid;
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
    public ResponseEntity<Equipment> getEquipmentById(@PathVariable UUID equipmentId) {
        try {
            Equipment eq = equipmentService.getEquipmentById(equipmentId);
            return ResponseEntity.ok(eq);
        } catch (EquipmentService.EquipmentNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<EquipmentDTO> createEquipment(@Valid @RequestBody EquipmentDTO equipmentDTO) {
        EquipmentDTO createdEquipment = equipmentService.createEquipment(equipmentDTO);
        return ResponseEntity.ok(createdEquipment);
    }

    @PutMapping("/{equipmentId}")
    public ResponseEntity<Equipment> updateEquipment(
            @PathVariable UUID equipmentId,
            @Valid @RequestBody Equipment updatedEquipment) {
        return equipmentService.updateEquipment(equipmentId, updatedEquipment);
    }

    @DeleteMapping("/{equipmentId}")
    public @ResponseBody ResponseEntity<Object> deleteEquipment(@PathVariable UUID equipmentId) {
        return equipmentService.deleteEquipment(equipmentId);
    }

    @ExceptionHandler(EquipmentService.EquipmentNotFoundException.class)
    public ResponseEntity<Object> handleEquipmentNotFound(EquipmentService.EquipmentNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

}
