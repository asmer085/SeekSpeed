package com.example.users.mappers;

import com.example.users.dto.StatisticsDTO;
import com.example.users.entity.Statistics;
import com.example.users.entity.Type;
import com.example.users.entity.Users;
import com.example.users.services.TypeService;
import com.example.users.services.UserService;
import org.mapstruct.*;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface StatisticsMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "type.id", target = "typeId")
    StatisticsDTO statisticsToStatisticsDTO(Statistics stat);

    @Mapping(source = "userId", target = "user", qualifiedByName = "mapUserIdToUser")
    @Mapping(source = "typeId", target = "type", qualifiedByName = "mapTypeIdToType")
    Statistics statisticsDTOToStatistics(StatisticsDTO statisticsDTO, @Context UserService userService, @Context TypeService typeService);

    @Named("mapUserIdToUser")
    static Users mapUserIdToUser(UUID userId, @Context UserService userService) {
        return userId != null ? userService.getUserById(userId) : null;
    }

    @Named("mapTypeIdToType")
    static Type mapTypeIdToType(UUID typeId, @Context TypeService typeService) {
        return typeId != null ? typeService.getTypeById(typeId) : null;
    }
}
