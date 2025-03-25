package com.example.users.repository;

import com.example.users.entity.Equipment;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface EquipmentRepository extends CrudRepository<Equipment, UUID> {
}
