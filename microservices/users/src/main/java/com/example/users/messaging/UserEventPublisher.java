package com.example.users.messaging;

import com.example.users.dtos.UserUpdateEventDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.users.config.RabbitMQConfig.USER_UPDATE_QUEUE;

@Service
public class UserEventPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendUserUpdate(UserUpdateEventDTO userDTO) {
        rabbitTemplate.convertAndSend(USER_UPDATE_QUEUE, userDTO);
    }

}
