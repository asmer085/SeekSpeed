package com.example.users.controllers;

import com.example.users.dtos.TypeDTO;
import com.example.users.entity.Type;
import com.example.users.services.TypeService;
import com.example.users.services.UserService;
import com.github.fge.jsonpatch.JsonPatch;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/type")
public class TypeController {

    @Autowired
    private TypeService typeService;

    @GetMapping("/all")
    public @ResponseBody Iterable<Type> getAllTypes() {
        return typeService.getAllTypes();
    }

    @GetMapping("/{typeId}")
    public ResponseEntity<Type> getTypeById(@PathVariable UUID typeId) {
        try {
            Type type = typeService.getTypeById(typeId);
            return ResponseEntity.ok(type);
        } catch (TypeService.TypeNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<TypeDTO> addType(@Valid @RequestBody TypeDTO typeDTO) {
        TypeDTO createdType = typeService.addType(typeDTO);
        return ResponseEntity.ok(createdType);
    }

    @PutMapping("/{typeId}")
    public ResponseEntity<Type> updateType(@PathVariable UUID typeId, @Valid @RequestBody TypeDTO updatedTypeDTO) {
        return typeService.updateType(typeId, updatedTypeDTO);
    }

    @PatchMapping("/{typeId}")
    public ResponseEntity<?> patchUpdateType(@PathVariable UUID typeId, @RequestBody JsonPatch patch) {
        try {
            Type updatedType = typeService.applyPatchToType(patch, typeId);
            return ResponseEntity.ok(updatedType);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{typeId}")
    public @ResponseBody ResponseEntity<Object> deleteType(@PathVariable UUID typeId) {
        return typeService.deleteType(typeId);
    }

    @ExceptionHandler(TypeService.TypeNotFoundException.class)
    public ResponseEntity<Object> handleTypeNotFound(TypeService.TypeNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }
}
