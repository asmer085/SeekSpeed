package com.example.users.mappers;

import com.example.users.dto.UserDTO;
import com.example.users.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO usersToUserDTO(Users user);
    Users userDTOToUsers(UserDTO userDTO);
}
