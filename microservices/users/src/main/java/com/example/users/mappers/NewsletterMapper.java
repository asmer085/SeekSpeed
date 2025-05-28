package com.example.users.mappers;

import com.example.users.dto.NewsletterDTO;
import com.example.users.entity.Newsletter;
import com.example.users.entity.Users;
import com.example.users.services.UserService;
import org.mapstruct.*;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface NewsletterMapper {

    @Mapping(source = "user.id", target = "userId")
    NewsletterDTO newsletterToNewsletterDTO(Newsletter newsletter);

    @Mapping(source = "userId", target = "user", qualifiedByName = "mapUserIdToUser") // userId -> Users
    Newsletter newsletterDTOToNewsletter(NewsletterDTO newsletterDTO, @Context UserService userService);

    @Named("mapUserIdToUser")
    static Users mapUserIdToUser(UUID userId, @Context UserService userService) {
        return userId != null ? userService.getUserById(userId) : null;
    }
}

