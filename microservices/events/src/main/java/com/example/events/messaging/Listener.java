package com.example.events.messaging;

import com.example.events.config.RabbitMQConfig;
import com.example.events.dto.EventUserDTO;
import com.example.events.entity.User;
import com.example.events.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class Listener {

    private final UserRepository userRepository;
    private volatile boolean usersReceived = false;

    public Listener(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.USER_RECEIVE_QUEUE)
    @Transactional
    public synchronized void receiveUsers(List<Map<String, Object>> usersMap) {
        try {
            System.out.println("\n=== Primljena poruka sa userima ===");
            System.out.println("Podaci:");
            System.out.println(usersMap);

            ObjectMapper mapper = new ObjectMapper();
            List<EventUserDTO> users = mapper.convertValue(usersMap,
                    new TypeReference<List<EventUserDTO>>() {});

            for (EventUserDTO userDto : users) {
                User user = new User();

                user.setId(UUID.randomUUID());
                user.setFirstName(userDto.getFirstName());
                user.setLastName(userDto.getLastName());
                user.setUsername(userDto.getUsername());
                user.setEmailAddress(userDto.getEmailAddress());
                user.setPicture(userDto.getPicture());
                user.setDateOfBirth(userDto.getDateOfBirth());
                user.setGender(userDto.getGender());
                user.setCountry(userDto.getCountry());
                user.setTShirtSize(userDto.getTShirtSize());
                user.setUserId(userDto.getUserId());

                userRepository.save(user);
                System.out.println("Sačuvan korisnik: " + userDto.getUsername());
            }

            usersReceived = true;
            notifyAll();
            System.out.println("=== Kraj poruke ===\n");

        } catch (Exception e) {
            System.err.println("Greška prilikom obrade poruke: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public synchronized void waitForUsers() throws InterruptedException {
        while (!usersReceived) {
            wait();
        }
    }
}
