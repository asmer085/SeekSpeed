package com.example.users.mappers;

import com.example.users.dtos.EquipmentDTO;
import com.example.users.entity.Equipment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EquipmentMapper {
    EquipmentMapper INSTANCE = Mappers.getMapper(EquipmentMapper.class);

    EquipmentDTO equipmentToEquipmentDTO(Equipment equipment);
    Equipment equipmentDTOToEquipment(EquipmentDTO equipmentDTO);

}
