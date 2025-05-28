package com.example.users.config;

import com.example.users.dto.EventUserDTO;
import com.example.users.entity.*;
import com.example.users.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TypeRepository typeRepository;
    private final StatisticsRepository statisticsRepository;
    private final OrderRepository orderRepository;
    private final NewsletterRepository newsletterRepository;
    private final EquipmentRepository equipmentRepository;
    private final RabbitTemplate rabbitTemplate;

    public DatabaseSeeder(UserRepository userRepository,
                          TypeRepository typeRepository,
                          StatisticsRepository statisticsRepository,
                          OrderRepository orderRepository,
                          NewsletterRepository newsletterRepository,
                          EquipmentRepository equipmentRepository,
                          RabbitTemplate rabbitTemplate) {
        this.userRepository = userRepository;
        this.typeRepository = typeRepository;
        this.statisticsRepository = statisticsRepository;
        this.orderRepository = orderRepository;
        this.newsletterRepository = newsletterRepository;
        this.equipmentRepository = equipmentRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        seedUsers();
        //seedTypes();
        seedEquipment();
        seedStatistics();
        seedOrders();
        seedNewsletters();
        sendUsersToEventsService();
    }

    private void sendUsersToEventsService() {
        List<Users> users = userRepository.findAll();
        List<EventUserDTO> eventsUsers = users.stream()
                .map(user -> new EventUserDTO(
                        user.getFirstName(),
                        user.getLastName(),
                        user.getUsername(),
                        user.getEmailAddress(),
                        user.getPicture(),
                        user.getDateOfBirth(),
                        user.getGender(),
                        user.getCountry(),
                        user.getTShirtSize(),
                        user.getId()
                )).toList();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.USER_EXCHANGE,
                RabbitMQConfig.USER_ROUTING_KEY,
                eventsUsers,
                message -> {
                    message.getMessageProperties().getHeaders().put("__TypeId__", "java.util.List");
                    return message;
                }
        );
        System.out.println("****Users data sent to Events service****");
    }

    private void seedUsers() throws JsonProcessingException {
        if (userRepository.count() == 0) {
            String usersJson = """
                [
                    {
                        "firstName": "JwtSignUp",
                        "lastName": "JwtToken",
                        "username": "jwtXoxo",
                        "emailAddress": "string@gmail.com",
                        "password": "$2a$12$SVhe2z4QEijdGr6gbgHmsOCLr4iA8R2gNKOMqymj4j5ZzuVB4dC6u",
                        "role": "user",
                        "picture": "jwt.jpg",
                        "dateOfBirth": "string",
                        "gender": "Female",
                        "organisationFile": "string",
                        "country": "string",
                        "tshirtSize": "L"
                    },
                    {
                        "firstName": "stringAPRILproba1",
                        "lastName": "Promjena",
                        "username": "stringApril",
                        "emailAddress": "string@proba.com",
                        "password": "$2a$12$78Tlgk6PUPjUB1F1Gjsf7.iO/b2QdpvRm1FM8QpURCk/imWqPEaOm",
                        "role": "user",
                        "picture": "string",
                        "dateOfBirth": "string",
                        "gender": "Male",
                        "organisationFile": "string",
                        "country": "string",
                        "tshirtSize": "M"
                    },
                    {
                        "firstName": "probaNoviAddtetete",
                        "lastName": "probaPatchLastName",
                        "username": "tetete",
                        "emailAddress": "string@gmail.com",
                        "password": "$2a$12$UF9VUN4V6RWh9.WJe8ViZunN1OWUx5PjBiKREKrJ3YbPzJYjIg1zy",
                        "role": "user",
                        "picture": "string",
                        "dateOfBirth": "string",
                        "gender": "Male",
                        "organisationFile": "string",
                        "country": "string",
                        "tshirtSize": "XL"
                    },
                    {
                        "firstName": "probaNoviAdd",
                        "lastName": "probaPatchLastName",
                        "username": "probaNoviAdd",
                        "emailAddress": "string@gmail.com",
                        "password": "$2a$12$OxlIC7cLTv0TpFtPTgn/T.fcClv0FS9/ZX9cUCV0yp2bW4GweuREW",
                        "role": "user",
                        "picture": "string",
                        "dateOfBirth": "string",
                        "gender": "Male",
                        "organisationFile": "string",
                        "country": "string",
                        "tshirtSize": "M"
                    },
                    {
                        "firstName": "stringAPRILproba11010",
                        "lastName": "Promjena",
                        "username": "proba11010",
                        "emailAddress": "string@proba.com",
                        "password": "$2a$12$un2FynGQbeP7McnYiVtO2Ov0Odlaf0Zge.XTgDWn/MoeQBECnNrBa",
                        "role": "organizer",
                        "picture": "string",
                        "dateOfBirth": "string",
                        "gender": "Male",
                        "organisationFile": "string",
                        "country": "string",
                        "tshirtSize": "M"
                    },
                    {
                        "firstName": "Ana",
                        "lastName": "Anić",
                        "username": "anaAnic1",
                        "emailAddress": "ana.anic@example.com",
                        "password": "$2a$12$6rfC6OQ.IlD.ccytX7hMH.if6jt5wvj/b33YDhCu.Ytsl9A7vT0rC",
                        "role": "user",
                        "picture": "ana.jpg",
                        "dateOfBirth": "1985-08-22",
                        "gender": "Female",
                        "organisationFile": "org2.pdf",
                        "country": "Croatia",
                        "tshirtSize": "M"
                    },
                    {
                        "firstName": "Marko",
                        "lastName": "Marković",
                        "username": "markoMark1",
                        "emailAddress": "marko.markovic@example.com",
                        "password": "$2a$12$eQwX5J4i0ulp/ZwZq5AACOYETa1CPoDhDrUnotY54aut6o1KFM4d2",
                        "role": "admin",
                        "picture": "marko.jpg",
                        "dateOfBirth": "1990-05-15",
                        "gender": "Male",
                        "organisationFile": "org1.pdf",
                        "country": "Serbia",
                        "tshirtSize": "L"
                    },
                    {
                        "firstName": "postmanTest",
                        "lastName": "patchPostmanPromjena",
                        "username": "postmanUsername1",
                        "emailAddress": "postmanTest@gmail",
                        "password": "$2a$12$kDuESvMqtVxp8t9BwG2T6ejCKXecboe8zJyMSVjfISINLCHoan8mq",
                        "role": "user",
                        "picture": "string",
                        "dateOfBirth": "string",
                        "gender": "Male",
                        "organisationFile": "string",
                        "country": "Croati",
                        "tshirtSize": "L"
                    },
                    {
                        "firstName": "EMIR",
                        "lastName": "EMIRR",
                        "username": "emiremir1",
                        "emailAddress": "emiremir1@gmail.com",
                        "password": "$2a$12$ykdygD3PNh6i1ele/xl2i.e3D3IxrNhLEhwtZmraxWgJuspOsZF8i",
                        "role": "organizer",
                        "picture": "emir2506.jpg",
                        "dateOfBirth": "1995-11-30",
                        "gender": "Male",
                        "organisationFile": "org3.pdf",
                        "country": "Bosnia and Herzegovina",
                        "tshirtSize": "XL"
                    },
                    {
                        "firstName": "Ivan",
                        "lastName": "Ivić",
                        "username": "ivanivi1",
                        "emailAddress": "ivan.ivic@example.com",
                        "password": "$2a$12$X8VCyX/eFCKJmgnt.Q8laO6B1N8G.VOaQ5ku0AbS325KSrHD6kBLm",
                        "role": "admin",
                        "picture": "ivan.jpg",
                        "dateOfBirth": "1995-11-30",
                        "gender": "Male",
                        "organisationFile": "org3.pdf",
                        "country": "Bosnia and Herzegovina",
                        "tshirtSize": "XL"
                    },
                    {
                        "firstName": "oookay",
                        "lastName": "Ivić",
                        "username": "testUsername",
                        "emailAddress": "ivan.ivic@example.com",
                        "password": "$2a$12$N.oPJ0zoemQSf1QKmgWrQenrD9f.m0yYUpmZ75xBgWalr2Q7f.ZIm",
                        "role": "user",
                        "picture": "ivan.jpg",
                        "dateOfBirth": "1995-11-30",
                        "gender": "Male",
                        "organisationFile": "org3.pdf",
                        "country": "Bosnia and Herzegovina",
                        "tshirtSize": "XL"
                    }
                ]
                """;

            ObjectMapper objectMapper = new ObjectMapper();
            List<Users> users = objectMapper.readValue(usersJson, new TypeReference<List<Users>>() {});
            userRepository.saveAll(users);
            System.out.println("Users seeded successfully!");
        }
    }

    private void seedTypes() throws JsonProcessingException {
        if (typeRepository.count() == 0) {
            String typesJson = """
                [
                    {
                        "distance": "8km",
                        "results": "Group pariticipation"
                    },
                    {
                        "distance": "10km",
                        "results": "Individual results"
                    },
                    {
                        "distance": "12km",
                        "results": "Team results"
                    },
                    {
                        "distance": "5km",
                        "results": "Individual results"
                    },
                    {
                        "distance": "6km",
                        "results": "Team results"
                    }
                ]
                """;

            ObjectMapper objectMapper = new ObjectMapper();
            List<Type> types = objectMapper.readValue(typesJson, new TypeReference<List<Type>>() {});
            typeRepository.saveAll(types);
            System.out.println("Types seeded successfully!");
        }
    }

    private void seedEquipment() throws JsonProcessingException {
        if (equipmentRepository.count() == 0) {
            String equipmentJson = """
                [
                    {
                        "name": "string",
                        "quantity": 1073
                    }
                ]
                """;

            ObjectMapper objectMapper = new ObjectMapper();
            List<Equipment> equipment = objectMapper.readValue(equipmentJson, new TypeReference<List<Equipment>>() {});
            equipmentRepository.saveAll(equipment);
            System.out.println("Equipment seeded successfully!");
        }
    }

    private void seedStatistics() throws JsonProcessingException {
        if (statisticsRepository.count() == 0) {
            String statisticsJson = """
                [
                    {
                        "averagePace": 0.1,
                        "bestPace": 0.1,
                        "totalTime": 0.1,
                        "username": "tetete",
                        "distance": "8km"
                    },
                    {
                        "averagePace": 0.1,
                        "bestPace": 0.1,
                        "totalTime": 0.1,
                        "username": "ivanivi1",
                        "distance": "8km"
                    },
                    {
                        "averagePace": 0.1,
                        "bestPace": 0.1,
                        "totalTime": 0.1,
                        "username": "postmanUsername1",
                        "distance": "12km"
                    },
                    {
                        "averagePace": 0.1,
                        "bestPace": 0.1,
                        "totalTime": 0.1,
                        "username": "emiremir1",
                        "distance": "5km"
                    },
                    {
                        "averagePace": 0.1,
                        "bestPace": 0.1,
                        "totalTime": 0.1,
                        "username": "ivanivi1",
                        "distance": "5km"
                    },
                    {
                        "averagePace": 1.5,
                        "bestPace": 10.1,
                        "totalTime": 4.1,
                        "username": "probaNoviAdd",
                        "distance": "6km"
                    },
                    {
                        "averagePace": 0.1,
                        "bestPace": 0.1,
                        "totalTime": 0.1,
                        "username": "stringApril",
                        "distance": "10km"
                    },
                    {
                        "averagePace": 0.1,
                        "bestPace": 0.1,
                        "totalTime": 0.1,
                        "username": "proba11010",
                        "distance": "8km"
                    },
                    {
                        "averagePace": 0.1,
                        "bestPace": 0.1,
                        "totalTime": 0.1,
                        "username": "anaAnic1",
                        "distance": "6km"
                    },
                    {
                        "averagePace": 0.1,
                        "bestPace": 0.1,
                        "totalTime": 0.1,
                        "username": "markoMark1",
                        "distance": "6km"
                    }
                ]
                """;

            ObjectMapper objectMapper = new ObjectMapper();
            List<StatisticsDTO> statisticsDTOs = objectMapper.readValue(statisticsJson, new TypeReference<List<StatisticsDTO>>() {});

            List<Statistics> statistics = statisticsDTOs.stream().map(dto -> {
                Statistics stat = new Statistics();
                stat.setAveragePace(dto.getAveragePace());
                stat.setBestPace(dto.getBestPace());
                stat.setTotalTime(dto.getTotalTime());

                Users user = userRepository.findByUsername(dto.getUsername());
                stat.setUser(user);

                Type type = typeRepository.findByDistance(dto.getDistance());
                stat.setType(type);

                return stat;
            }).toList();

            statisticsRepository.saveAll(statistics);
            System.out.println("Statistics seeded successfully!");
        }
    }

    private void seedOrders() throws JsonProcessingException {
        if (orderRepository.count() == 0) {
            String ordersJson = """
                [
                    {
                        "equipmentName": "string",
                        "username": "probaNoviAdd"
                    }
                ]
                """;

            ObjectMapper objectMapper = new ObjectMapper();
            List<OrderDTO> orderDTOs = objectMapper.readValue(ordersJson, new TypeReference<List<OrderDTO>>() {});

            List<Orders> orders = orderDTOs.stream().map(dto -> {
                Orders order = new Orders();
                Equipment equipment = equipmentRepository.findByName(dto.getEquipmentName());
                order.setEquipmentId(equipment.getId());

                Users user = userRepository.findByUsername(dto.getUsername());
                order.setUserId(user.getId());

                return order;
            }).toList();

            orderRepository.saveAll(orders);
            System.out.println("Orders seeded successfully!");
        }
    }

    private void seedNewsletters() throws JsonProcessingException {
        if (newsletterRepository.count() == 0) {
            String newslettersJson = """
                [
                    {
                        "title": "striaaa",
                        "description": "lalalalalal",
                        "username": "tetete"
                    },
                    {
                        "title": "striaaa",
                        "description": "lalalalalal",
                        "username": "ivanivi1"
                    },
                    {
                        "title": "striaaa",
                        "description": "lalalalalal",
                        "username": "markoMark1"
                    },
                    {
                        "title": "striaaa",
                        "description": "lalalalalal",
                        "username": "postmanUsername1"
                    },
                    {
                        "title": "striaaa",
                        "description": "lalalalalal",
                        "username": "probaNoviAdd"
                    },
                    {
                        "title": "string",
                        "description": "stringstri",
                        "username": "stringApril"
                    },
                    {
                        "title": "striaaa",
                        "description": "lalalalalal",
                        "username": "testUsername"
                    },
                    {
                        "title": "striaaa",
                        "description": "lalalalalal",
                        "username": "emiremir1"
                    },
                    {
                        "title": "striaaa",
                        "description": "lalalalalal",
                        "username": "proba11010"
                    },
                    {
                        "title": "striaaa",
                        "description": "lalalalalal",
                        "username": "anaAnic1"
                    }
                ]
                """;

            ObjectMapper objectMapper = new ObjectMapper();
            List<NewsletterDTO> newsletterDTOs = objectMapper.readValue(newslettersJson, new TypeReference<List<NewsletterDTO>>() {});

            List<Newsletter> newsletters = newsletterDTOs.stream().map(dto -> {
                Newsletter newsletter = new Newsletter();
                newsletter.setTitle(dto.getTitle());
                newsletter.setDescription(dto.getDescription());

                Users user = userRepository.findByUsername(dto.getUsername());
                newsletter.setUser(user);

                return newsletter;
            }).toList();

            newsletterRepository.saveAll(newsletters);
            System.out.println("Newsletters seeded successfully!");
        }
    }

    // DTO classes for deserialization
    private static class StatisticsDTO {
        private double averagePace;
        private double bestPace;
        private double totalTime;
        private String username;
        private String distance;

        public double getAveragePace() {
            return averagePace;
        }

        public void setAveragePace(double averagePace) {
            this.averagePace = averagePace;
        }

        public double getBestPace() {
            return bestPace;
        }

        public void setBestPace(double bestPace) {
            this.bestPace = bestPace;
        }

        public double getTotalTime() {
            return totalTime;
        }

        public void setTotalTime(double totalTime) {
            this.totalTime = totalTime;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }
    }

    private static class OrderDTO {
        private String equipmentName;
        private String username;

        public String getEquipmentName() {
            return equipmentName;
        }

        public void setEquipmentName(String equipmentName) {
            this.equipmentName = equipmentName;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    private static class NewsletterDTO {
        private String title;
        private String description;
        private String username;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}