package com.example.users.services;

import com.example.users.dtos.EquipmentDTO;
import com.example.users.entity.Equipment;
import com.example.users.mappers.EquipmentMapper;
import com.example.users.repository.EquipmentRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EquipmentServiceTests {

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private EquipmentMapper equipmentMapper;

    @InjectMocks
    private EquipmentService equipmentService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Validator validator;

    private Equipment testEquipment;
    private EquipmentDTO testEquipmentDTO;
    private EquipmentDTO updatedEquipmentDTO;
    private UUID testEquipmentId;

    @BeforeEach
    void setUp() {
        testEquipmentId = UUID.randomUUID();

        testEquipment = new Equipment();
        testEquipment.setId(testEquipmentId);
        testEquipment.setName("Treadmill");
        testEquipment.setQuantity(10);

        testEquipmentDTO = new EquipmentDTO();
        testEquipmentDTO.setName("Treadmill");
        testEquipmentDTO.setQuantity(10);

        updatedEquipmentDTO = new EquipmentDTO();
        updatedEquipmentDTO.setName("Elliptical Machine");
        updatedEquipmentDTO.setQuantity(5);
    }

    @Test
    void getAllEquipment_ShouldReturnAllEquipment() {
        // Arrange
        when(equipmentRepository.findAll()).thenReturn(List.of(testEquipment));

        // Act
        Iterable<Equipment> result = equipmentService.getAllEquipment();

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Equipment>) result).size());
        verify(equipmentRepository, times(1)).findAll();
    }

    @Test
    void getEquipmentById_ExistingId_ShouldReturnEquipment() {
        // Arrange
        when(equipmentRepository.findById(testEquipmentId)).thenReturn(Optional.of(testEquipment));

        // Act
        Equipment result = equipmentService.getEquipmentById(testEquipmentId);

        // Assert
        assertNotNull(result);
        assertEquals(testEquipment, result);
        verify(equipmentRepository, times(1)).findById(testEquipmentId);
    }

    @Test
    void getEquipmentById_NonExistingId_ShouldThrowException() {
        // Arrange
        when(equipmentRepository.findById(testEquipmentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EquipmentService.EquipmentNotFoundException.class, () -> {
            equipmentService.getEquipmentById(testEquipmentId);
        });
        verify(equipmentRepository, times(1)).findById(testEquipmentId);
    }

    @Test
    void createEquipment_ValidEquipment_ShouldReturnEquipmentDTO() {
        // Arrange
        when(equipmentMapper.equipmentDTOToEquipment(any(EquipmentDTO.class))).thenReturn(testEquipment);
        when(equipmentRepository.save(any(Equipment.class))).thenReturn(testEquipment);
        when(equipmentMapper.equipmentToEquipmentDTO(any(Equipment.class))).thenReturn(testEquipmentDTO);

        // Act
        EquipmentDTO result = equipmentService.createEquipment(testEquipmentDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testEquipmentDTO, result);
        verify(equipmentRepository, times(1)).save(any(Equipment.class));
    }

    @Test
    void updateEquipment_ExistingEquipment_ShouldReturnUpdatedEquipment() {
        // Arrange
        Equipment updatedEquipment = new Equipment();
        updatedEquipment.setId(testEquipmentId);
        updatedEquipment.setName("Elliptical Machine");
        updatedEquipment.setQuantity(5);

        when(equipmentRepository.findById(testEquipmentId)).thenReturn(Optional.of(testEquipment));
        when(equipmentRepository.save(any(Equipment.class))).thenReturn(updatedEquipment);

        // Act
        ResponseEntity<Equipment> result = equipmentService.updateEquipment(testEquipmentId, updatedEquipmentDTO);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        Equipment updatedResult = result.getBody();
        assertNotNull(updatedResult);
        assertEquals("Elliptical Machine", updatedResult.getName());
        assertEquals(5, updatedResult.getQuantity());
        verify(equipmentRepository, times(1)).findById(testEquipmentId);
        verify(equipmentRepository, times(1)).save(any(Equipment.class));
    }

    @Test
    void updateEquipment_NonExistingEquipment_ShouldReturnNotFound() {
        // Arrange
        when(equipmentRepository.findById(testEquipmentId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Equipment> result = equipmentService.updateEquipment(testEquipmentId, updatedEquipmentDTO);

        // Assert
        assertNotNull(result);
        assertEquals(404, result.getStatusCodeValue());
        verify(equipmentRepository, times(1)).findById(testEquipmentId);
        verify(equipmentRepository, never()).save(any(Equipment.class));
    }

    @Test
    void deleteEquipment_ExistingEquipment_ShouldReturnOk() {
        // Arrange
        when(equipmentRepository.findById(testEquipmentId)).thenReturn(Optional.of(testEquipment));

        // Act
        ResponseEntity<Object> result = equipmentService.deleteEquipment(testEquipmentId);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        verify(equipmentRepository, times(1)).findById(testEquipmentId);
        verify(equipmentRepository, times(1)).delete(testEquipment);
    }

    @Test
    void deleteEquipment_NonExistingEquipment_ShouldReturnNotFound() {
        // Arrange
        when(equipmentRepository.findById(testEquipmentId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Object> result = equipmentService.deleteEquipment(testEquipmentId);

        // Assert
        assertNotNull(result);
        assertEquals(404, result.getStatusCodeValue());
        verify(equipmentRepository, times(1)).findById(testEquipmentId);
        verify(equipmentRepository, never()).delete(any(Equipment.class));
    }

    @Test
    @Transactional
    void applyPatchToEquipment_ValidPatch_ShouldReturnPatchedEquipment() throws Exception {
        // Arrange
        String patchJson = "[{\"op\":\"replace\",\"path\":\"/quantity\",\"value\":15}]";
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchJson));

        when(equipmentRepository.findById(testEquipmentId)).thenReturn(Optional.of(testEquipment));

        // Mock the objectMapper behavior
        ObjectMapper realMapper = new ObjectMapper();
        JsonNode equipmentNode = realMapper.valueToTree(testEquipment);
        JsonNode patchedNode = patch.apply(equipmentNode);
        Equipment patchedEquipment = realMapper.treeToValue(patchedNode, Equipment.class);
        patchedEquipment.setId(testEquipmentId);

        when(objectMapper.valueToTree(any(Equipment.class))).thenReturn(equipmentNode);
        when(objectMapper.treeToValue(any(JsonNode.class), eq(Equipment.class))).thenReturn(patchedEquipment);

        // Mock validation to pass
        Set<ConstraintViolation<Equipment>> emptyViolations = Collections.emptySet();
        when(validator.validate(any(Equipment.class))).thenReturn(emptyViolations);

        when(equipmentRepository.save(patchedEquipment)).thenReturn(patchedEquipment);

        // Act
        Equipment result = equipmentService.applyPatchToEquipment(patch, testEquipmentId);

        // Assert
        assertNotNull(result);
        assertEquals(15, result.getQuantity());
        assertEquals("Treadmill", result.getName()); // unchanged
        verify(equipmentRepository, times(1)).findById(testEquipmentId);
        verify(equipmentRepository, times(1)).save(patchedEquipment);
    }

    @Test
    @Transactional
    void applyPatchToEquipment_InvalidQuantityPatch_ShouldThrowException() throws Exception {
        // Arrange
        String invalidPatchJson = "[{\"op\":\"replace\",\"path\":\"/quantity\",\"value\":\"invalid\"}]";
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(invalidPatchJson));

        when(equipmentRepository.findById(testEquipmentId)).thenReturn(Optional.of(testEquipment));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            equipmentService.applyPatchToEquipment(patch, testEquipmentId);
        });
    }
}