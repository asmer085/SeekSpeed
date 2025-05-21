package com.example.users.messaging;

import com.example.users.dtos.UserUpdateEventDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TestQueueListener {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = "user-updated-queue")
    public void receiveUserUpdate(UserUpdateEventDTO userDTO) {
        try {
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userDTO);
            System.out.println("Primljena poruka:");
            System.out.println(json);
        } catch (Exception e) {
            System.err.println("Greška prilikom konverzije u JSON: " + e.getMessage());
        }
    }
}
