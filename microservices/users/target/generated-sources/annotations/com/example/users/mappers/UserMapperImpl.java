package com.example.users.mappers;

import com.example.users.dtos.UserDTO;
import com.example.users.entity.Users;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-10T16:30:35+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO usersToUserDTO(Users user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setFirstName( user.getFirstName() );
        userDTO.setLastName( user.getLastName() );
        userDTO.setUsername( user.getUsername() );
        userDTO.setEmailAddress( user.getEmailAddress() );
        userDTO.setPassword( user.getPassword() );
        userDTO.setRole( user.getRole() );
        userDTO.setDateOfBirth( user.getDateOfBirth() );
        userDTO.setGender( user.getGender() );
        userDTO.setTShirtSize( user.getTShirtSize() );
        userDTO.setOrganisationFile( user.getOrganisationFile() );
        userDTO.setCountry( user.getCountry() );
        userDTO.setPicture( user.getPicture() );

        return userDTO;
    }

    @Override
    public Users userDTOToUsers(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        Users users = new Users();

        users.setFirstName( userDTO.getFirstName() );
        users.setLastName( userDTO.getLastName() );
        users.setUsername( userDTO.getUsername() );
        users.setEmailAddress( userDTO.getEmailAddress() );
        users.setPassword( userDTO.getPassword() );
        users.setRole( userDTO.getRole() );
        users.setPicture( userDTO.getPicture() );
        users.setDateOfBirth( userDTO.getDateOfBirth() );
        users.setGender( userDTO.getGender() );
        users.setTShirtSize( userDTO.getTShirtSize() );
        users.setOrganisationFile( userDTO.getOrganisationFile() );
        users.setCountry( userDTO.getCountry() );

        return users;
    }
}
