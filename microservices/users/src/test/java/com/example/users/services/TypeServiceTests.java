package com.example.users.services;

import com.example.users.dtos.TypeDTO;
import com.example.users.entity.Type;
import com.example.users.mappers.TypeMapper;
import com.example.users.repository.TypeRepository;
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
class TypeServiceTests {

    @Mock
    private TypeRepository typeRepository;

    @Mock
    private TypeMapper typeMapper;

    @InjectMocks
    private TypeService typeService;

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
        Type updatedType = new Type();
        updatedType.setDistance("5km");

        when(typeRepository.findById(testTypeId)).thenReturn(Optional.of(testType));
        when(typeRepository.save(any(Type.class))).thenReturn(updatedType);

        // Act
        ResponseEntity<Type> result = typeService.updateType(testTypeId, updatedType);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        verify(typeRepository, times(1)).findById(testTypeId);
        verify(typeRepository, times(1)).save(any(Type.class));
    }

    @Test
    void updateType_NonExistingType_ShouldReturnNotFound() {
        // Arrange
        when(typeRepository.findById(testTypeId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Type> result = typeService.updateType(testTypeId, testType);

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
}