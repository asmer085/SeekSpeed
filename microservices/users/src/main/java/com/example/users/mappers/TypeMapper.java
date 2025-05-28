package com.example.users.mappers;

import com.example.users.dto.TypeDTO;
import com.example.users.entity.Type;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TypeMapper {
    TypeMapper INSTANCE = Mappers.getMapper(TypeMapper.class);

    TypeDTO typeToTypeDTO(Type type);
    Type typeDTOToType(TypeDTO typeDTO);
}
