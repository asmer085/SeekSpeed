package com.example.users.mappers;

import com.example.users.dtos.EquipmentDTO;
import com.example.users.entity.Equipment;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-14T09:33:59+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class EquipmentMapperImpl implements EquipmentMapper {

    @Override
    public EquipmentDTO equipmentToEquipmentDTO(Equipment equipment) {
        if ( equipment == null ) {
            return null;
        }

        EquipmentDTO equipmentDTO = new EquipmentDTO();

        equipmentDTO.setName( equipment.getName() );
        equipmentDTO.setQuantity( equipment.getQuantity() );

        return equipmentDTO;
    }

    @Override
    public Equipment equipmentDTOToEquipment(EquipmentDTO equipmentDTO) {
        if ( equipmentDTO == null ) {
            return null;
        }

        Equipment equipment = new Equipment();

        equipment.setName( equipmentDTO.getName() );
        equipment.setQuantity( equipmentDTO.getQuantity() );

        return equipment;
    }
}
