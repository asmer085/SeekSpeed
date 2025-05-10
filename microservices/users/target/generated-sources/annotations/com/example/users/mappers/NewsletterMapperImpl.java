package com.example.users.mappers;

import com.example.users.dtos.NewsletterDTO;
import com.example.users.entity.Newsletter;
import com.example.users.entity.Users;
import com.example.users.services.UserService;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-10T16:30:35+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class NewsletterMapperImpl implements NewsletterMapper {

    @Override
    public NewsletterDTO newsletterToNewsletterDTO(Newsletter newsletter) {
        if ( newsletter == null ) {
            return null;
        }

        NewsletterDTO newsletterDTO = new NewsletterDTO();

        newsletterDTO.setUserId( newsletterUserId( newsletter ) );
        newsletterDTO.setTitle( newsletter.getTitle() );
        newsletterDTO.setDescription( newsletter.getDescription() );

        return newsletterDTO;
    }

    @Override
    public Newsletter newsletterDTOToNewsletter(NewsletterDTO newsletterDTO, UserService userService) {
        if ( newsletterDTO == null ) {
            return null;
        }

        Newsletter newsletter = new Newsletter();

        newsletter.setUser( NewsletterMapper.mapUserIdToUser( newsletterDTO.getUserId(), userService ) );
        newsletter.setTitle( newsletterDTO.getTitle() );
        newsletter.setDescription( newsletterDTO.getDescription() );

        return newsletter;
    }

    private UUID newsletterUserId(Newsletter newsletter) {
        if ( newsletter == null ) {
            return null;
        }
        Users user = newsletter.getUser();
        if ( user == null ) {
            return null;
        }
        UUID id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
