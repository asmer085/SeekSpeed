package com.example.events.messaging;

import com.example.events.config.RabbitMQConfig;
import com.example.events.dto.EventUserDTO;
import com.example.events.dto.UserUpdateEventDTO;
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

    @RabbitListener(queues = RabbitMQConfig.USER_UPDATE_QUEUE)
    @Transactional
    public void receiveUpdateUser(UserUpdateEventDTO message) {
        userRepository.findByUserId(message.getUserId()).ifPresent(user -> {
            user.setFirstName(message.getFirstName());
            user.setLastName(message.getLastName());
            user.setEmailAddress(message.getEmailAddress());
            user.setRole(message.getRole());
            user.setDateOfBirth(message.getDateOfBirth());
            user.setGender(message.getGender());
            user.setTShirtSize(message.getTShirtSize());
            user.setCountry(message.getCountry());
            user.setPicture(message.getPicture());

            userRepository.save(user);
        });
    }

    @RabbitListener(queues = RabbitMQConfig.USER_DELETE_QUEUE)
    @Transactional
    public void receiveDeleteUser(UserUpdateEventDTO message) {
        userRepository.findByUserId(message.getUserId()).ifPresent(
                userRepository::delete
        );
        System.out.println("*****Obrisan korisnik!******");
    }

    @RabbitListener(queues = RabbitMQConfig.USER_POST_QUEUE)
    @Transactional
    public void receiveCreateUser(UserUpdateEventDTO message) {
        User user = new User();
        user.setUserId(message.getUserId());
        user.setFirstName(message.getFirstName());
        user.setLastName(message.getLastName());
        user.setEmailAddress(message.getEmailAddress());
        user.setRole(message.getRole());
        user.setDateOfBirth(message.getDateOfBirth());
        user.setGender(message.getGender());
        user.setTShirtSize(message.getTShirtSize());
        user.setCountry(message.getCountry());
        user.setPicture(message.getPicture());

        userRepository.save(user);
        System.out.println("*****Sačuvan korisnik POST!******");
    }

    @RabbitListener(queues = RabbitMQConfig.USER_BATCH_QUEUE)
    @Transactional
    public void receiveUserBatch(List<UserUpdateEventDTO> users) {
        for (UserUpdateEventDTO message : users) {
            User user = new User();
            user.setUserId(message.getUserId());
            user.setFirstName(message.getFirstName());
            user.setLastName(message.getLastName());
            user.setEmailAddress(message.getEmailAddress());
            user.setRole(message.getRole());
            user.setDateOfBirth(message.getDateOfBirth());
            user.setGender(message.getGender());
            user.setTShirtSize(message.getTShirtSize());
            user.setCountry(message.getCountry());
            user.setPicture(message.getPicture());

            userRepository.save(user);
            System.out.println("****Sačuvan korisnik (batch): " + message.getFirstName() + " " + message.getLastName());
        }
    }


    @RabbitListener(queues = RabbitMQConfig.USER_RECEIVE_QUEUE)
    @Transactional
    public synchronized void receiveUsers(List<Map<String, Object>> usersMap) {
        try {
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
                user.setRole(userDto.getRole());
                user.setGender(userDto.getGender());
                user.setCountry(userDto.getCountry());
                user.setTShirtSize(userDto.getTShirtSize());
                user.setUserId(userDto.getUserId());

                userRepository.save(user);
                System.out.println("Sačuvan korisnik: " + userDto.getUsername());
            }
            usersReceived = true;
            notifyAll();
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
