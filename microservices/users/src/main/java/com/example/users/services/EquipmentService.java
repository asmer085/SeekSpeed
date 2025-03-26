package com.example.users.services;

import com.example.users.entity.Equipment;
import com.example.users.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    public Iterable<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    public Equipment getEquipmentById(UUID equipmentId) {
        return equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new RuntimeException("Equipment with id " + equipmentId + " not found"));
    }

    public Equipment createEquipment(Equipment equipment) {
        return equipmentRepository.save(equipment);
    }

    public ResponseEntity<Equipment> updateEquipment(UUID equipmentId, Equipment updatedEquipment) {
        return equipmentRepository.findById(equipmentId)
                .map(equipment -> {
                    if (updatedEquipment.getName() != null)
                        equipment.setName(updatedEquipment.getName());
                    if (updatedEquipment.getQuantity() != 0)
                        equipment.setQuantity(updatedEquipment.getQuantity());
                    return ResponseEntity.ok(equipmentRepository.save(equipment));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Object> deleteEquipment(UUID equipmentId) {
        return equipmentRepository.findById(equipmentId)
                .map(equipment -> {
                    equipmentRepository.delete(equipment);
                    return ResponseEntity.ok().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
