package com.example.events.service;

import com.example.events.dto.TypeDTO;
import com.example.events.entity.Event;
import com.example.events.entity.Type;
import com.example.events.exception.ResourceNotFoundException;
import com.example.events.repository.EventRepository;
import com.example.events.repository.TypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TypeServiceTest {

    @Mock
    private TypeRepository typeRepository;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private TypeService typeService;

    private UUID typeId;
    private UUID eventId;
    private Event event;
    private Type type;
    private TypeDTO typeDTO;

    @BeforeEach
    void setUp() {
        typeId = UUID.randomUUID();
        eventId = UUID.randomUUID();

        event = new Event();
        event.setId(eventId);

        type = new Type();
        type.setId(typeId);
        type.setPrice(10.0);
        type.setDistance("5.0");
        type.setResults("Results");
        type.setEvent(event);

        typeDTO = new TypeDTO();
        typeDTO.setId(typeId);
        typeDTO.setPrice(10.0);
        typeDTO.setDistance("5.0");
        typeDTO.setResults("Results");
        typeDTO.setEventId(eventId);
    }

    @Test
    void createType_ValidInput_ShouldReturnType() {
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(typeRepository.save(any(Type.class))).thenReturn(type);

        Type result = typeService.createType(typeDTO);

        assertNotNull(result);
        assertEquals(type.getPrice(), result.getPrice());
    }

    @Test
    void createType_EventNotFound_ShouldThrowException() {
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> typeService.createType(typeDTO));
    }

    @Test
    void getAllTypes_ShouldReturnList() {
        when(typeRepository.findAll()).thenReturn(List.of(type));

        Iterable<Type> result = typeService.getAllTypes();

        assertTrue(result.iterator().hasNext());
    }

    @Test
    void getTypesByEventId_ValidEvent_ShouldReturnTypes() {
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(typeRepository.findByEvent(event)).thenReturn(List.of(type));

        List<Type> result = typeService.getTypesByEventId(eventId);

        assertEquals(1, result.size());
    }

    @Test
    void getTypesByEventId_EventNotFound_ShouldThrowException() {
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> typeService.getTypesByEventId(eventId));
    }

    @Test
    void updateTypes_ValidInput_ShouldReturnUpdatedTypes() {
        when(typeRepository.findById(typeId)).thenReturn(Optional.of(type));
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(typeRepository.saveAll(anyList())).thenReturn(List.of(type));

        List<Type> result = typeService.updateTypes(List.of(typeDTO));

        assertEquals(1, result.size());
        verify(typeRepository).saveAll(anyList());
    }

    @Test
    void updateTypes_TypeNotFound_ShouldThrowException() {
        when(typeRepository.findById(typeId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> typeService.updateTypes(List.of(typeDTO)));
    }

    @Test
    void getTypesByEventIdAndMinDistance_ShouldReturnFilteredTypes() {
        when(typeRepository.findByEventIdAndDistanceGreaterThan(eventId, 3.0)).thenReturn(List.of(type));

        List<Type> result = typeService.getTypesByEventIdAndMinDistance(eventId, 3.0);

        assertEquals(1, result.size());
    }
}
