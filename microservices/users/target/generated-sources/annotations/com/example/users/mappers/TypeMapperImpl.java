package com.example.users.mappers;

import com.example.users.dto.TypeDTO;
import com.example.users.entity.Type;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-30T14:43:46+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class TypeMapperImpl implements TypeMapper {

    @Override
    public TypeDTO typeToTypeDTO(Type type) {
        if ( type == null ) {
            return null;
        }

        TypeDTO typeDTO = new TypeDTO();

        typeDTO.setDistance( type.getDistance() );
        typeDTO.setResults( type.getResults() );

        return typeDTO;
    }

    @Override
    public Type typeDTOToType(TypeDTO typeDTO) {
        if ( typeDTO == null ) {
            return null;
        }

        Type type = new Type();

        type.setDistance( typeDTO.getDistance() );
        type.setResults( typeDTO.getResults() );

        return type;
    }
}
