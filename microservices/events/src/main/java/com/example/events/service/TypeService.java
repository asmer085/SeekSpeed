package com.example.events.service;

import com.example.events.dto.TypeDTO;
import com.example.events.entity.Event;
import com.example.events.entity.Types;
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

    public Types createType(TypeDTO typeDTO) {
        Types types = new Types();
        types.setPrice(typeDTO.getPrice());
        types.setDistance(typeDTO.getDistance());
        types.setResults(typeDTO.getResults());

        Event event = eventRepository.findById(typeDTO.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + typeDTO.getEventId()));
        types.setEvent(event);

        return typeRepository.save(types);
    }

    public Iterable<Types> getAllTypes() {
        return typeRepository.findAll();
    }

    public List<Types> getTypesByEventId(UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));
        return typeRepository.findByEvent(event);
    }

    @Transactional
    public List<Types> updateTypes(List<TypeDTO> typeDTOs) {
        List<Types> updatedTypes = new ArrayList<>();

        for (TypeDTO dto : typeDTOs) {
            Types types = typeRepository.findById(dto.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Types not found: " + dto.getId()));

            types.setPrice(dto.getPrice());
            types.setDistance(dto.getDistance());
            types.setResults(dto.getResults());
            Event event = eventRepository.findById(dto.getEventId())
                    .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + dto.getEventId()));
            types.setEvent(event);

            updatedTypes.add(types);
        }

        return typeRepository.saveAll(updatedTypes);
    }

    public List<Types> getTypesByEventIdAndMinDistance(UUID eventId, double minDistance) {
        return typeRepository.findByEventIdAndDistanceGreaterThan(eventId, minDistance);
    }


}
