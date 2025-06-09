package com.example.users.mappers;

import com.example.users.dto.StatisticsDTO;
import com.example.users.entity.Statistics;
import com.example.users.entity.Type;
import com.example.users.entity.Users;
import com.example.users.services.TypeService;
import com.example.users.services.UserService;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-09T13:56:01+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class StatisticsMapperImpl implements StatisticsMapper {

    @Override
    public StatisticsDTO statisticsToStatisticsDTO(Statistics stat) {
        if ( stat == null ) {
            return null;
        }

        StatisticsDTO statisticsDTO = new StatisticsDTO();

        statisticsDTO.setUserId( statUserId( stat ) );
        statisticsDTO.setTypeId( statTypeId( stat ) );
        statisticsDTO.setAveragePace( stat.getAveragePace() );
        statisticsDTO.setBestPace( stat.getBestPace() );
        statisticsDTO.setTotalTime( stat.getTotalTime() );

        return statisticsDTO;
    }

    @Override
    public Statistics statisticsDTOToStatistics(StatisticsDTO statisticsDTO, UserService userService, TypeService typeService) {
        if ( statisticsDTO == null ) {
            return null;
        }

        Statistics statistics = new Statistics();

        statistics.setUser( StatisticsMapper.mapUserIdToUser( statisticsDTO.getUserId(), userService ) );
        statistics.setType( StatisticsMapper.mapTypeIdToType( statisticsDTO.getTypeId(), typeService ) );
        statistics.setAveragePace( statisticsDTO.getAveragePace() );
        statistics.setBestPace( statisticsDTO.getBestPace() );
        statistics.setTotalTime( statisticsDTO.getTotalTime() );

        return statistics;
    }

    private UUID statUserId(Statistics statistics) {
        if ( statistics == null ) {
            return null;
        }
        Users user = statistics.getUser();
        if ( user == null ) {
            return null;
        }
        UUID id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID statTypeId(Statistics statistics) {
        if ( statistics == null ) {
            return null;
        }
        Type type = statistics.getType();
        if ( type == null ) {
            return null;
        }
        UUID id = type.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
