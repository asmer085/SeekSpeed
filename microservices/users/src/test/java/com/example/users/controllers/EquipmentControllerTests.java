package com.example.users.controllers;

import com.example.users.dto.EquipmentDTO;
import com.example.users.entity.Equipment;
import com.example.users.services.EquipmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EquipmentControllerTests {

    @Mock
    private EquipmentService equipmentService;

    @InjectMocks
    private EquipmentController equipmentController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

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

        mockMvc = MockMvcBuilders.standaloneSetup(equipmentController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllEquipment_ShouldReturnEquipmentList() throws Exception {
        // Arrange
        Iterable<Equipment> equipmentList = Arrays.asList(testEquipment);
        given(equipmentService.getAllEquipment()).willReturn(equipmentList);

        // Act & Assert
        mockMvc.perform(get("/equipment/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Treadmill"))
                .andExpect(jsonPath("$[0].quantity").value(10));
    }

    @Test
    void getEquipmentById_ExistingId_ShouldReturnEquipment() throws Exception {
        // Arrange
        given(equipmentService.getEquipmentById(testEquipmentId)).willReturn(testEquipment);

        // Act & Assert
        mockMvc.perform(get("/equipment/{equipmentId}", testEquipmentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Treadmill"))
                .andExpect(jsonPath("$.quantity").value(10));
    }

    @Test
    void getEquipmentById_NonExistingId_ShouldThrowException() throws Exception {
        // Arrange
        given(equipmentService.getEquipmentById(testEquipmentId))
                .willThrow(new EquipmentService.EquipmentNotFoundException("Equipment not found"));

        // Act & Assert
        mockMvc.perform(get("/equipment/{equipmentId}", testEquipmentId))
                .andExpect(status().isNotFound());
    }

    @Test
    void createEquipment_ValidEquipment_ShouldReturnCreatedEquipment() throws Exception {
        // Arrange
        given(equipmentService.createEquipment(any(EquipmentDTO.class))).willReturn(testEquipmentDTO);

        // Act & Assert
        mockMvc.perform(post("/equipment/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testEquipmentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Treadmill"))
                .andExpect(jsonPath("$.quantity").value(10));
    }

    @Test
    void updateEquipment_ExistingEquipment_ShouldReturnUpdatedEquipment() throws Exception {
        // Arrange
        Equipment updatedEquipment = new Equipment();
        updatedEquipment.setName("Updated Treadmill");
        updatedEquipment.setQuantity(15);

        given(equipmentService.updateEquipment(eq(testEquipmentId), any(EquipmentDTO.class)))
                .willReturn(ResponseEntity.ok(updatedEquipment));

        // Act & Assert
        mockMvc.perform(put("/equipment/{equipmentId}", testEquipmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEquipment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Treadmill"))
                .andExpect(jsonPath("$.quantity").value(15));
    }

    @Test
    void updateEquipment_NonExistingEquipment_ShouldReturnNotFound() throws Exception {
        // Arrange
        given(equipmentService.updateEquipment(eq(testEquipmentId), any(EquipmentDTO.class)))
                .willReturn(ResponseEntity.notFound().build());

        // Act & Assert
        mockMvc.perform(put("/equipment/{equipmentId}", testEquipmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testEquipment)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteEquipment_ExistingEquipment_ShouldReturnOk() throws Exception {
        // Arrange
        given(equipmentService.deleteEquipment(testEquipmentId))
                .willReturn(ResponseEntity.ok().build());

        // Act & Assert
        mockMvc.perform(delete("/equipment/{equipmentId}", testEquipmentId))
                .andExpect(status().isOk());
    }

    @Test
    void deleteEquipment_NonExistingEquipment_ShouldReturnNotFound() throws Exception {
        // Arrange
        given(equipmentService.deleteEquipment(testEquipmentId))
                .willReturn(ResponseEntity.notFound().build());

        // Act & Assert
        mockMvc.perform(delete("/equipment/{equipmentId}", testEquipmentId))
                .andExpect(status().isNotFound());
    }

    @Test
    void patchUpdateEquipment_ValidPatch_ShouldReturnUpdatedEquipment() throws Exception {
        // Arrange
        String patchJson = "[{\"op\":\"replace\",\"path\":\"/name\",\"value\":\"New Equipment Name\"}]";

        Equipment patchedEquipment = new Equipment();
        patchedEquipment.setId(UUID.randomUUID());
        patchedEquipment.setName("New Equipment Name");
        patchedEquipment.setQuantity(10);

        given(equipmentService.applyPatchToEquipment(any(), any(UUID.class)))
                .willReturn(patchedEquipment);

        // Act & Assert
        mockMvc.perform(patch("/equipment/{equipmentId}", UUID.randomUUID())
                        .contentType("application/json-patch+json")
                        .content(patchJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Equipment Name"))
                .andExpect(jsonPath("$.quantity").value(10));
    }

    @Test
    void patchUpdateEquipment_InvalidPatch_ShouldReturnBadRequest() throws Exception {
        // Arrange
        String invalidPatchJson = "[{\"op\":\"replace\",\"path\":\"/quantity\",\"value\":-5}]"; // Negative quantity

        given(equipmentService.applyPatchToEquipment(any(), any(UUID.class)))
                .willThrow(new RuntimeException("Validation failed: Quantity cannot be negative"));

        // Act & Assert
        mockMvc.perform(patch("/equipment/{equipmentId}", UUID.randomUUID())
                        .contentType("application/json-patch+json")
                        .content(invalidPatchJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Validation failed: Quantity cannot be negative"));
    }
}