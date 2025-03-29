package com.example.users.services;

import com.example.users.dtos.EquipmentDTO;
import com.example.users.entity.Equipment;
import com.example.users.mappers.EquipmentMapper;
import com.example.users.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private EquipmentMapper equipmentMapper;

    public Iterable<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    public Equipment getEquipmentById(UUID equipmentId) {
        return equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new EquipmentNotFoundException("Equipment with id " + equipmentId + " not found"));
    }

    public EquipmentDTO createEquipment(EquipmentDTO equipmentDTO) {
        Equipment equipment = equipmentMapper.equipmentDTOToEquipment(equipmentDTO);
        Equipment savedEquipment = equipmentRepository.save(equipment);
        return equipmentMapper.equipmentToEquipmentDTO(savedEquipment);
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

    public static class EquipmentNotFoundException extends RuntimeException {
        public EquipmentNotFoundException(String message) {
            super(message);
        }
    }
}
