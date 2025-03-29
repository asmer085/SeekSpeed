package com.example.users.controllers;

import com.example.users.dtos.TypeDTO;
import com.example.users.entity.Type;
import com.example.users.services.TypeService;
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

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TypeControllerTests {

    @Mock
    private TypeService typeService;

    @InjectMocks
    private TypeController typeController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Type testType;
    private TypeDTO testTypeDTO;
    private UUID testTypeId;

    @BeforeEach
    void setUp() {
        testTypeId = UUID.randomUUID();

        testType = new Type();
        testType.setId(testTypeId);
        testType.setDistance("10km");
        testType.setResults("Top 3");

        testTypeDTO = new TypeDTO();
        testTypeDTO.setDistance("10km");
        testTypeDTO.setResults("Top 3");

        mockMvc = MockMvcBuilders.standaloneSetup(typeController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllTypes_ShouldReturnTypes() throws Exception {
        // Arrange
        Iterable<Type> types = Collections.singletonList(testType);
        given(typeService.getAllTypes()).willReturn(types);

        // Act & Assert
        mockMvc.perform(get("/type/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].distance").value("10km"))
                .andExpect(jsonPath("$[0].results").value("Top 3"));
    }

    @Test
    void getTypeById_ExistingId_ShouldReturnType() throws Exception {
        // Arrange
        given(typeService.getTypeById(testTypeId)).willReturn(testType);

        // Act & Assert
        mockMvc.perform(get("/type/{typeId}", testTypeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.distance").value("10km"))
                .andExpect(jsonPath("$.results").value("Top 3"));
    }

    @Test
    void getTypeById_NonExistingId_ShouldThrowException() throws Exception {
        // Arrange
        given(typeService.getTypeById(testTypeId))
                .willThrow(new TypeService.TypeNotFoundException("Type not found"));

        // Act & Assert
        mockMvc.perform(get("/type/{typeId}", testTypeId))
                .andExpect(status().isNotFound());
    }

    @Test
    void addType_ValidType_ShouldReturnCreatedType() throws Exception {
        // Arrange
        given(typeService.addType(any(TypeDTO.class))).willReturn(testTypeDTO);

        // Act & Assert
        mockMvc.perform(post("/type/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTypeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.distance").value("10km"))
                .andExpect(jsonPath("$.results").value("Top 3"));
    }

    @Test
    void updateType_ExistingType_ShouldReturnUpdatedType() throws Exception {
        // Arrange
        Type updatedType = new Type();
        updatedType.setDistance("5km");
        updatedType.setResults("Top 5");

        given(typeService.updateType(eq(testTypeId), any(Type.class)))
                .willReturn(ResponseEntity.ok(updatedType));

        // Act & Assert
        mockMvc.perform(put("/type/{typeId}", testTypeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedType)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.distance").value("5km"))
                .andExpect(jsonPath("$.results").value("Top 5"));
    }

    @Test
    void updateType_NonExistingType_ShouldReturnNotFound() throws Exception {
        // Arrange
        given(typeService.updateType(eq(testTypeId), any(Type.class)))
                .willReturn(ResponseEntity.notFound().build());

        // Act & Assert
        mockMvc.perform(put("/type/{typeId}", testTypeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testType)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteType_ExistingType_ShouldReturnOk() throws Exception {
        // Arrange
        given(typeService.deleteType(testTypeId))
                .willReturn(ResponseEntity.ok().build());

        // Act & Assert
        mockMvc.perform(delete("/type/{typeId}", testTypeId))
                .andExpect(status().isOk());
    }

    @Test
    void deleteType_NonExistingType_ShouldReturnNotFound() throws Exception {
        // Arrange
        given(typeService.deleteType(testTypeId))
                .willReturn(ResponseEntity.notFound().build());

        // Act & Assert
        mockMvc.perform(delete("/type/{typeId}", testTypeId))
                .andExpect(status().isNotFound());
    }
}