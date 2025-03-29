package com.example.users.services;

import com.example.users.dtos.EquipmentDTO;
import com.example.users.entity.Equipment;
import com.example.users.mappers.EquipmentMapper;
import com.example.users.repository.EquipmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    private Equipment testEquipment;
    private EquipmentDTO testEquipmentDTO;
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
        updatedEquipment.setName("Updated Treadmill");

        when(equipmentRepository.findById(testEquipmentId)).thenReturn(Optional.of(testEquipment));
        when(equipmentRepository.save(any(Equipment.class))).thenReturn(updatedEquipment);

        // Act
        ResponseEntity<Equipment> result = equipmentService.updateEquipment(testEquipmentId, updatedEquipment);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        verify(equipmentRepository, times(1)).findById(testEquipmentId);
        verify(equipmentRepository, times(1)).save(any(Equipment.class));
    }

    @Test
    void updateEquipment_NonExistingEquipment_ShouldReturnNotFound() {
        // Arrange
        when(equipmentRepository.findById(testEquipmentId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Equipment> result = equipmentService.updateEquipment(testEquipmentId, testEquipment);

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
}