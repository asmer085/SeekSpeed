package com.example.users.messaging;

import com.example.users.config.RabbitMQConfig;
import com.example.users.dto.UserTypeDTO;
import com.example.users.entity.Type;
import com.example.users.repository.TypeRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class Listener {

    private final TypeRepository typeRepository;

    public Listener(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.TYPE_RECEIVE_QUEUE)
    public void receiveTypes(List<Map<String, Object>> typesMap) {
        try {
            System.out.println("\n=== Primljena poruka sa tipovima ===");
            System.out.println("Podaci:");
            System.out.println(typesMap);
            System.out.println("=== Kraj poruke ===\n");

            ObjectMapper mapper = new ObjectMapper();
            List<UserTypeDTO> types = mapper.convertValue(typesMap,
                    new TypeReference<List<UserTypeDTO>>() {});

            for (UserTypeDTO typesDTO :types) {
                Type type = new Type();
                //type.setTypeId(UUID.randomUUID());
                type.setDistance(typesDTO.getDistance());
                type.setResults(typesDTO.getResults());
                type.setPrice(typesDTO.getPrice());
                type.setTypeId(typesDTO.getTypeId());
                typeRepository.save(type);
                System.out.println("******Sačuvan tip******");
            }

        } catch (Exception e) {
            System.err.println("Greška prilikom obrade poruke: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
