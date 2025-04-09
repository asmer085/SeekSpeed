package com.example.events.service;

import com.example.events.dto.TypeDTO;
import com.example.events.entity.Event;
import com.example.events.entity.Type;
import com.example.events.repository.EventRepository;
import com.example.events.repository.TypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                .orElseThrow(() -> new RuntimeException("Event not found"));
        type.setEvent(event);

        return typeRepository.save(type);
    }

    public Iterable<Type> getAllTypes() {
        return typeRepository.findAll();
    }

    public List<Type> getTypesByEventId(UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        return typeRepository.findByEvent(event);
    }
}
