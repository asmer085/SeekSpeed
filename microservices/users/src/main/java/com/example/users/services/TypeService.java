package com.example.users.services;

import com.example.users.dtos.TypeDTO;
import com.example.users.entity.Type;
import com.example.users.mappers.TypeMapper;
import com.example.users.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TypeService {

    @Autowired
    private TypeRepository typeRepository;

    @Autowired
    private TypeMapper typeMapper;

    public Iterable<Type> getAllTypes() {
        return typeRepository.findAll();
    }

    public Type getTypeById(UUID typeId) {
        return typeRepository.findById(typeId)
                .orElseThrow(() -> new TypeNotFoundException("Type with id " + typeId + " not found"));
    }

    public TypeDTO addType(TypeDTO typeDTO) {
        Type type = typeMapper.typeDTOToType(typeDTO);
        Type savedType = typeRepository.save(type);
        return typeMapper.typeToTypeDTO(savedType);
    }

    public ResponseEntity<Type> updateType(UUID typeId, Type updatedType) {
        return typeRepository.findById(typeId)
                .map(type -> {
                    if (updatedType.getDistance() != null) type.setDistance(updatedType.getDistance());
                    if (updatedType.getResults() != null) type.setResults(updatedType.getResults());
                    return ResponseEntity.ok(typeRepository.save(type));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Object> deleteType(UUID typeId) {
        return typeRepository.findById(typeId)
                .map(type -> {
                    typeRepository.delete(type);
                    return ResponseEntity.ok().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public static class TypeNotFoundException extends RuntimeException {
        public TypeNotFoundException(String message) {
            super(message);
        }
    }
}
