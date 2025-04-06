package com.example.users.services;

import com.example.users.dtos.EquipmentDTO;
import com.example.users.entity.Equipment;
import com.example.users.mappers.EquipmentMapper;
import com.example.users.repository.EquipmentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Validator validator;

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

    @Transactional
    public ResponseEntity<Equipment> updateEquipment(UUID equipmentId, EquipmentDTO updatedEquipment) {
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

    @Transactional
    public Equipment applyPatchToEquipment(JsonPatch patch, UUID equipId) {
        try {
            Equipment equipment = equipmentRepository.findById(equipId)
                    .orElseThrow(() -> new EquipmentNotFoundException("Equipment with id " + equipId + " not found"));
            JsonNode equipmentNode = objectMapper.valueToTree(equipment);
            JsonNode patchedNode = patch.apply(equipmentNode);
            Equipment patchedEquipment = objectMapper.treeToValue(patchedNode, Equipment.class);

            Set<ConstraintViolation<Equipment>> violations = validator.validate(patchedEquipment);
            if (!violations.isEmpty()) {
                StringBuilder errorMessage = new StringBuilder("Validation failed: ");
                for (ConstraintViolation<Equipment> violation : violations) {
                    errorMessage.append(violation.getMessage()).append(" ");
                }
                throw new RuntimeException(errorMessage.toString());
            }
            return equipmentRepository.save(patchedEquipment);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
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
