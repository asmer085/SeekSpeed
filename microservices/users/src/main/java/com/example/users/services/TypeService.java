package com.example.users.services;

import com.example.users.dto.TypeDTO;
import com.example.users.entity.Type;
import com.example.users.mappers.TypeMapper;
import com.example.users.repository.TypeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class TypeService {

    @Autowired
    private TypeRepository typeRepository;

    @Autowired
    private TypeMapper typeMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Validator validator;

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

    @Transactional
    public ResponseEntity<Type> updateType(UUID typeId, TypeDTO updatedType) {
        return typeRepository.findById(typeId)
                .map(type -> {
                    if (updatedType.getDistance() != null) type.setDistance(updatedType.getDistance());
                    if (updatedType.getResults() != null) type.setResults(updatedType.getResults());
                    if (updatedType.getUuid() != null) type.setTypeId(updatedType.getUuid());
                    return ResponseEntity.ok(typeRepository.save(type));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    public Type applyPatchToType(JsonPatch patch, UUID typeId) {
        try {
            Type type = typeRepository.findById(typeId)
                    .orElseThrow(() -> new TypeNotFoundException("Type with id " + typeId + " not found"));
            JsonNode typeNode = objectMapper.valueToTree(type);
            JsonNode patchedNode = patch.apply(typeNode);
            Type patchedType = objectMapper.treeToValue(patchedNode, Type.class);

            Set<ConstraintViolation<Type>> violations = validator.validate(patchedType);
            if (!violations.isEmpty()) {
                StringBuilder errorMessage = new StringBuilder("Validation failed: ");
                for (ConstraintViolation<Type> violation : violations) {
                    errorMessage.append(violation.getMessage()).append(" ");
                }
                throw new RuntimeException(errorMessage.toString());
            }
            return typeRepository.save(patchedType);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
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
