package com.example.events.service;

import com.example.events.config.RabbitMQConfig;
import com.example.events.dto.TypeDTO;
import com.example.events.dto.UserTypeDTO;
import com.example.events.entity.Event;
import com.example.events.entity.Types;
import com.example.events.exception.ResourceNotFoundException;
import com.example.events.repository.EventRepository;
import com.example.events.repository.TypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
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
    @Autowired
    private final RabbitTemplate rabbitTemplate;

    public Types createType(TypeDTO typeDTO) {
        Types types = new Types();
        types.setPrice(typeDTO.getPrice());
        types.setDistance(typeDTO.getDistance());
        types.setResults(typeDTO.getResults());

        Event event = eventRepository.findById(typeDTO.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + typeDTO.getEventId()));
        types.setEvent(event);

        UserTypeDTO message = new UserTypeDTO();
        message.setTypeId(types.getId());
        message.setDistance(types.getDistance());
        message.setResults(types.getResults());
        message.setPrice(types.getPrice());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.TYPE_UPDATE_EXCHANGE,
                RabbitMQConfig.TYPE_UPDATE_ROUTING_KEY,
                message
        );

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
    public ResponseEntity<Types> updateType(UUID typeId, TypeDTO updatedType) {
        return typeRepository.findById(typeId)
                .map(existingType -> {
                    if (updatedType.getPrice() != 0.0) existingType.setPrice(updatedType.getPrice());
                    if (updatedType.getDistance() != null) existingType.setDistance(updatedType.getDistance());
                    if (updatedType.getResults() != null) existingType.setResults(updatedType.getResults());

                    if (updatedType.getEventId() != null) {
                        Event event = eventRepository.findById(updatedType.getEventId())
                                .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + updatedType.getEventId()));
                        existingType.setEvent(event);
                    }

                    Types saved = typeRepository.save(existingType);

                    UserTypeDTO message = new UserTypeDTO();
                    message.setTypeId(saved.getId());
                    message.setDistance(saved.getDistance());
                    message.setResults(saved.getResults());
                    message.setPrice(saved.getPrice());

                    rabbitTemplate.convertAndSend(
                            RabbitMQConfig.TYPE_UPDATE_EXCHANGE,
                            RabbitMQConfig.TYPE_UPDATE_ROUTING_KEY,
                            message
                    );

                    return ResponseEntity.ok(saved);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public List<Types> getTypesByEventIdAndMinDistance(UUID eventId, double minDistance) {
        return typeRepository.findByEventIdAndDistanceGreaterThan(eventId, minDistance);
    }


}
