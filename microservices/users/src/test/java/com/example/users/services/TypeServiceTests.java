package com.example.users.services;

import com.example.users.dtos.TypeDTO;
import com.example.users.entity.Type;
import com.example.users.mappers.TypeMapper;
import com.example.users.repository.TypeRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import org.hibernate.validator.constraints.ModCheck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TypeServiceTests {

    @Mock
    private TypeRepository typeRepository;

    @Mock
    private TypeMapper typeMapper;

    @Mock
    private Validator validator;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private TypeService typeService;

    private Type testType;
    private TypeDTO testTypeDTO;
    private TypeDTO updatedTypeDTO;
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

        updatedTypeDTO = new TypeDTO();
        updatedTypeDTO.setDistance("5km");
        updatedTypeDTO.setResults("Top 5");
    }

    @Test
    void getAllTypes_ShouldReturnAllTypes() {
        // Arrange
        when(typeRepository.findAll()).thenReturn(List.of(testType));

        // Act
        Iterable<Type> result = typeService.getAllTypes();

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Type>) result).size());
        verify(typeRepository, times(1)).findAll();
    }

    @Test
    void getTypeById_ExistingId_ShouldReturnType() {
        // Arrange
        when(typeRepository.findById(testTypeId)).thenReturn(Optional.of(testType));

        // Act
        Type result = typeService.getTypeById(testTypeId);

        // Assert
        assertNotNull(result);
        assertEquals(testType, result);
        verify(typeRepository, times(1)).findById(testTypeId);
    }

    @Test
    void getTypeById_NonExistingId_ShouldThrowException() {
        // Arrange
        when(typeRepository.findById(testTypeId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TypeService.TypeNotFoundException.class, () -> {
            typeService.getTypeById(testTypeId);
        });
        verify(typeRepository, times(1)).findById(testTypeId);
    }

    @Test
    void addType_ValidType_ShouldReturnTypeDTO() {
        // Arrange
        when(typeMapper.typeDTOToType(any(TypeDTO.class))).thenReturn(testType);
        when(typeRepository.save(any(Type.class))).thenReturn(testType);
        when(typeMapper.typeToTypeDTO(any(Type.class))).thenReturn(testTypeDTO);

        // Act
        TypeDTO result = typeService.addType(testTypeDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testTypeDTO, result);
        verify(typeRepository, times(1)).save(any(Type.class));
    }

    @Test
    void updateType_ExistingType_ShouldReturnUpdatedType() {
        // Arrange
        Type updatedTypeEntity = new Type();
        updatedTypeEntity.setId(testTypeId);
        updatedTypeEntity.setDistance("5km");
        updatedTypeEntity.setResults("Top 5");

        when(typeRepository.findById(testTypeId)).thenReturn(Optional.of(testType));
        when(typeRepository.save(any(Type.class))).thenReturn(updatedTypeEntity);

        // Act
        ResponseEntity<Type> result = typeService.updateType(testTypeId, updatedTypeDTO);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertNotNull(result.getBody());
        assertEquals("5km", result.getBody().getDistance());
        assertEquals("Top 5", result.getBody().getResults());
        verify(typeRepository, times(1)).findById(testTypeId);
        verify(typeRepository, times(1)).save(any(Type.class));
    }

    @Test
    void updateType_NonExistingType_ShouldReturnNotFound() {
        // Arrange
        when(typeRepository.findById(testTypeId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Type> result = typeService.updateType(testTypeId, updatedTypeDTO);

        // Assert
        assertNotNull(result);
        assertEquals(404, result.getStatusCodeValue());
        verify(typeRepository, times(1)).findById(testTypeId);
        verify(typeRepository, never()).save(any(Type.class));
    }

    @Test
    void deleteType_ExistingType_ShouldReturnOk() {
        // Arrange
        when(typeRepository.findById(testTypeId)).thenReturn(Optional.of(testType));

        // Act
        ResponseEntity<Object> result = typeService.deleteType(testTypeId);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        verify(typeRepository, times(1)).findById(testTypeId);
        verify(typeRepository, times(1)).delete(testType);
    }

    @Test
    void deleteType_NonExistingType_ShouldReturnNotFound() {
        // Arrange
        when(typeRepository.findById(testTypeId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Object> result = typeService.deleteType(testTypeId);

        // Assert
        assertNotNull(result);
        assertEquals(404, result.getStatusCodeValue());
        verify(typeRepository, times(1)).findById(testTypeId);
        verify(typeRepository, never()).delete(any(Type.class));
    }

    @Test
    @Transactional
    void applyPatchToType_ValidPatch_ShouldReturnPatchedType() throws Exception {
        // Arrange
        String patchJson = "[{\"op\":\"replace\",\"path\":\"/distance\",\"value\":\"21km\"}]";
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchJson));

        testType.setDistance("10km"); // Set initial value

        when(typeRepository.findById(testTypeId)).thenReturn(Optional.of(testType));

        // Mock the objectMapper behavior
        ObjectMapper realMapper = new ObjectMapper();
        JsonNode typeNode = realMapper.valueToTree(testType);
        JsonNode patchedNode = patch.apply(typeNode);
        Type patchedType = realMapper.treeToValue(patchedNode, Type.class);
        patchedType.setId(testTypeId); // Preserve ID

        when(objectMapper.valueToTree(any(Type.class))).thenReturn(typeNode);
        when(objectMapper.treeToValue(any(JsonNode.class), eq(Type.class))).thenReturn(patchedType);

        // Mock validation to pass
        when(validator.validate(patchedType)).thenReturn(Collections.emptySet());

        when(typeRepository.save(patchedType)).thenReturn(patchedType);

        // Act
        Type result = typeService.applyPatchToType(patch, testTypeId);

        // Assert
        assertNotNull(result);
        assertEquals("21km", result.getDistance());
        verify(typeRepository, times(1)).findById(testTypeId);
        verify(typeRepository, times(1)).save(patchedType);
    }

    @Test
    @Transactional
    void applyPatchToType_InvalidPatchOperation_ShouldThrowException() throws Exception {
        // Arrange
        String invalidPatchJson = "[{\"op\":\"replace\",\"path\":\"/invalidField\",\"value\":\"value\"}]";
        JsonPatch patch = new ObjectMapper().readValue(invalidPatchJson, JsonPatch.class);

        when(typeRepository.findById(testTypeId)).thenReturn(Optional.of(testType));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            typeService.applyPatchToType(patch, testTypeId);
        });
    }

    @Test
    @Transactional
    void applyPatchToType_NonExistingType_ShouldThrowException() throws Exception {
        // Arrange
        String patchJson = "[{\"op\":\"replace\",\"path\":\"/distance\",\"value\":\"21km\"}]";
        JsonPatch patch = new ObjectMapper().readValue(patchJson, JsonPatch.class);

        when(typeRepository.findById(testTypeId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TypeService.TypeNotFoundException.class, () -> {
            typeService.applyPatchToType(patch, testTypeId);
        });
    }
}