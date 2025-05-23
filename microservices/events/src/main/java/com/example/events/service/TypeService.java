package com.example.events.service;

import com.example.events.dto.TypeDTO;
import com.example.events.entity.Event;
import com.example.events.entity.Type;
import com.example.events.exception.ResourceNotFoundException;
import com.example.events.repository.EventRepository;
import com.example.events.repository.TypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TypeService {
    @Autowired
    private final TypeRepository typeRepository;
    @Autowired
    private final EventRepository eventRepository;

    public Type createType(TypeDTO typeDTO) {
        Type type = new Type();
        type.setPrice(typeDTO.getPrice());
        type.setDistance(typeDTO.getDistance());
        type.setResults(typeDTO.getResults());

        Event event = eventRepository.findById(typeDTO.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + typeDTO.getEventId()));
        type.setEvent(event);

        return typeRepository.save(type);
    }

    public Iterable<Type> getAllTypes() {
        return typeRepository.findAll();
    }

    public List<Type> getTypesByEventId(UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));
        return typeRepository.findByEvent(event);
    }

    @Transactional
    public List<Type> updateTypes(List<TypeDTO> typeDTOs) {
        List<Type> updatedTypes = new ArrayList<>();

        for (TypeDTO dto : typeDTOs) {
            Type type = typeRepository.findById(dto.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Type not found: " + dto.getId()));

            type.setPrice(dto.getPrice());
            type.setDistance(dto.getDistance());
            type.setResults(dto.getResults());
            Event event = eventRepository.findById(dto.getEventId())
                    .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + dto.getEventId()));
            type.setEvent(event);

            updatedTypes.add(type);
        }

        return typeRepository.saveAll(updatedTypes);
    }

    public List<Type> getTypesByEventIdAndMinDistance(UUID eventId, double minDistance) {
        return typeRepository.findByEventIdAndDistanceGreaterThan(eventId, minDistance);
    }


}
