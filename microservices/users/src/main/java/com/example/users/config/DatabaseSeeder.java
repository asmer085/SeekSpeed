package com.example.users.config;

import com.example.users.entity.*;
import com.example.users.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TypeRepository typeRepository;
    private final StatisticsRepository statisticsRepository;
    private final OrderRepository orderRepository;
    private final NewsletterRepository newsletterRepository;
    private final EquipmentRepository equipmentRepository;

    public DatabaseSeeder(UserRepository userRepository,
                          TypeRepository typeRepository,
                          StatisticsRepository statisticsRepository,
                          OrderRepository orderRepository,
                          NewsletterRepository newsletterRepository,
                          EquipmentRepository equipmentRepository) {
        this.userRepository = userRepository;
        this.typeRepository = typeRepository;
        this.statisticsRepository = statisticsRepository;
        this.orderRepository = orderRepository;
        this.newsletterRepository = newsletterRepository;
        this.equipmentRepository = equipmentRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        seedUsers();
        seedTypes();
        seedEquipment();
        seedStatistics();
        seedOrders();
        seedNewsletters();
    }

    private void seedUsers() throws JsonProcessingException {
        if (userRepository.count() == 0) {
            String usersJson = """
                [
                    {
                        "id": "1685148a-d460-406a-babe-adf96016ecbd",
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
                        "id": "5bd1e72b-0ab0-4fc0-be55-69b1b43d8bd6",
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
                        "id": "666d19e0-56ba-4865-90f9-9b229d466697",
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
                        "id": "80d45932-cc33-41ff-af76-6a7853a5ca76",
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
                        "id": "8b28ff41-6133-4513-8635-b8e481b347c1",
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
                        "id": "93ff08d4-7153-4453-b7d6-8fea6911770d",
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
                        "id": "97ab7a35-f239-4ee4-a01c-a293e0fdbfcc",
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
                        "id": "a2b31b60-79bf-4205-9726-b8765c91f6a5",
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
                        "id": "b126ed50-7412-48ee-a8ca-3702badfba30",
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
                        "id": "cb2e5f28-f329-45be-b47e-210dfd0dcfe4",
                        "firstName": "Ivan",
                        "lastName": "Ivić",
                        "username": "ivanivi1",
                        "emailAddress": "ivan.ivic@example.com",
                        "password": "$2a$12$X8VCyX/eFCKJmgnt.Q8laO6B1N8G.VOaQ5ku0AbS325KSrHD6kBLm",
                        "role": "admin",
                        "picture": "ivan.jpg",
                        "dateOfBirth": "1995-11-30",
                        "gender": "Female",
                        "organisationFile": "org3.pdf",
                        "country": "Bosnia and Herzegovina",
                        "tshirtSize": "XL"
                    },
                    {
                        "id": "d09c6d51-df26-4700-a31d-d3b7f85ab511",
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
                        "id": "d22da5bf-c912-474d-95da-17f229465af7",
                        "distance": "2km",
                        "results": "1km",
                        "uuid": "3fa85f64-5717-4562-b3fc-2c963f66afa6"
                    },
                    {
                        "id": "e406ba35-a52d-421e-8473-8d75eeb6f87c",
                        "distance": "4km",
                        "results": "string",
                        "uuid": "3fa85f64-5717-4562-b3fc-2c963f66afa6"
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
                        "id": "8359b467-996d-4d5e-ad4b-762cfd66060e",
                        "name": "string",
                        "quantity": 1073741820
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
                        "id": "3d1b7ead-051a-4584-ac5f-2db53476eb67",
                        "averagePace": 0.1,
                        "bestPace": 0.1,
                        "totalTime": 0.1,
                        "userId": "666d19e0-56ba-4865-90f9-9b229d466697",
                        "typeId": "d22da5bf-c912-474d-95da-17f229465af7"
                    },
                    {
                        "id": "4018316f-52d8-4027-94ce-a36dae8887bb",
                        "averagePace": 0.1,
                        "bestPace": 0.1,
                        "totalTime": 0.1,
                        "userId": "cb2e5f28-f329-45be-b47e-210dfd0dcfe4",
                        "typeId": "d22da5bf-c912-474d-95da-17f229465af7"
                    },
                    {
                        "id": "40877710-c6b9-4886-be51-6fec97c6361c",
                        "averagePace": 0.1,
                        "bestPace": 0.1,
                        "totalTime": 0.1,
                        "userId": "a2b31b60-79bf-4205-9726-b8765c91f6a5",
                        "typeId": "d22da5bf-c912-474d-95da-17f229465af7"
                    },
                    {
                        "id": "529debf7-b3be-4847-a62e-7a644d70904e",
                        "averagePace": 0.1,
                        "bestPace": 0.1,
                        "totalTime": 0.1,
                        "userId": "b126ed50-7412-48ee-a8ca-3702badfba30",
                        "typeId": "d22da5bf-c912-474d-95da-17f229465af7"
                    },
                    {
                        "id": "79341ff8-7580-4add-b8f7-a26b138cff49",
                        "averagePace": 0.1,
                        "bestPace": 0.1,
                        "totalTime": 0.1,
                        "userId": "cb2e5f28-f329-45be-b47e-210dfd0dcfe4",
                        "typeId": "e406ba35-a52d-421e-8473-8d75eeb6f87c"
                    },
                    {
                        "id": "b5b8c4f3-0ff4-448e-9ad1-34c278d5f637",
                        "averagePace": 1.5,
                        "bestPace": 10.1,
                        "totalTime": 4.1,
                        "userId": "80d45932-cc33-41ff-af76-6a7853a5ca76",
                        "typeId": "d22da5bf-c912-474d-95da-17f229465af7"
                    },
                    {
                        "id": "bce27238-412b-40ce-938d-19466f61f46c",
                        "averagePace": 0.1,
                        "bestPace": 0.1,
                        "totalTime": 0.1,
                        "userId": "5bd1e72b-0ab0-4fc0-be55-69b1b43d8bd6",
                        "typeId": "e406ba35-a52d-421e-8473-8d75eeb6f87c"
                    },
                    {
                        "id": "c15bcf85-874f-4044-b86b-eaf62b953cb1",
                        "averagePace": 0.1,
                        "bestPace": 0.1,
                        "totalTime": 0.1,
                        "userId": "8b28ff41-6133-4513-8635-b8e481b347c1",
                        "typeId": "d22da5bf-c912-474d-95da-17f229465af7"
                    },
                    {
                        "id": "c432d7fb-09c1-4b3f-a7a7-dff5aef292c2",
                        "averagePace": 0.1,
                        "bestPace": 0.1,
                        "totalTime": 0.1,
                        "userId": "93ff08d4-7153-4453-b7d6-8fea6911770d",
                        "typeId": "d22da5bf-c912-474d-95da-17f229465af7"
                    },
                    {
                        "id": "d18e3ce5-fb7f-459b-9834-71ba790c175b",
                        "averagePace": 0.1,
                        "bestPace": 0.1,
                        "totalTime": 0.1,
                        "userId": "97ab7a35-f239-4ee4-a01c-a293e0fdbfcc",
                        "typeId": "d22da5bf-c912-474d-95da-17f229465af7"
                    }
                ]
                """;

            ObjectMapper objectMapper = new ObjectMapper();
            List<StatisticsDTO> statisticsDTOs = objectMapper.readValue(statisticsJson, new TypeReference<List<StatisticsDTO>>() {});

            List<Statistics> statistics = statisticsDTOs.stream().map(dto -> {
                Statistics stat = new Statistics();
                stat.setId(UUID.fromString(dto.getId()));
                stat.setAveragePace(dto.getAveragePace());
                stat.setBestPace(dto.getBestPace());
                stat.setTotalTime(dto.getTotalTime());

                Users user = userRepository.findById(UUID.fromString(dto.getUserId()))
                        .orElseThrow(() -> new RuntimeException("User not found: " + dto.getUserId()));
                stat.setUser(user);

                Type type = typeRepository.findById(UUID.fromString(dto.getTypeId()))
                        .orElseThrow(() -> new RuntimeException("Type not found: " + dto.getTypeId()));
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
                        "id": "d92e4437-65f0-403d-a4a0-ec95213cb6b6",
                        "equipmentId": "8359b467-996d-4d5e-ad4b-762cfd66060e",
                        "userId": "80d45932-cc33-41ff-af76-6a7853a5ca76"
                    }
                ]
                """;

            ObjectMapper objectMapper = new ObjectMapper();
            List<OrderDTO> orderDTOs = objectMapper.readValue(ordersJson, new TypeReference<List<OrderDTO>>() {});

            List<Orders> orders = orderDTOs.stream().map(dto -> {
                Orders order = new Orders();
                order.setId(UUID.fromString(dto.getId()));

                Equipment equipment = equipmentRepository.findById(UUID.fromString(dto.getEquipmentId()))
                        .orElseThrow(() -> new RuntimeException("Equipment not found: " + dto.getEquipmentId()));
                order.setEquipmentId(equipment.getId());

                Users user = userRepository.findById(UUID.fromString(dto.getUserId()))
                        .orElseThrow(() -> new RuntimeException("User not found: " + dto.getUserId()));
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
                        "id": "27cacc96-0e8e-4530-a073-d65ce1171902",
                        "title": "striaaa",
                        "description": "lalalalalal",
                        "userId": "666d19e0-56ba-4865-90f9-9b229d466697"
                    },
                    {
                        "id": "32a0f85b-9d59-48bd-b694-8e9d366ac1ee",
                        "title": "striaaa",
                        "description": "lalalalalal",
                        "userId": "cb2e5f28-f329-45be-b47e-210dfd0dcfe4"
                    },
                    {
                        "id": "441cbc2e-fa1a-44f8-bdd5-bcaee0f760b9",
                        "title": "striaaa",
                        "description": "lalalalalal",
                        "userId": "97ab7a35-f239-4ee4-a01c-a293e0fdbfcc"
                    },
                    {
                        "id": "47facd1f-ed9d-40b5-8d1d-038457e37eb6",
                        "title": "striaaa",
                        "description": "lalalalalal",
                        "userId": "a2b31b60-79bf-4205-9726-b8765c91f6a5"
                    },
                    {
                        "id": "5ebcef67-d810-4507-bc2b-2718cbeab95e",
                        "title": "striaaa",
                        "description": "lalalalalal",
                        "userId": "80d45932-cc33-41ff-af76-6a7853a5ca76"
                    },
                    {
                        "id": "62795c5f-591c-40e8-beb6-caa44296c806",
                        "title": "string",
                        "description": "stringstri",
                        "userId": "5bd1e72b-0ab0-4fc0-be55-69b1b43d8bd6"
                    },
                    {
                        "id": "c08500dc-8892-4289-87c5-4329ba0adf6b",
                        "title": "striaaa",
                        "description": "lalalalalal",
                        "userId": "d09c6d51-df26-4700-a31d-d3b7f85ab511"
                    },
                    {
                        "id": "cac409fa-c280-45d9-94ba-2d345dab00bd",
                        "title": "striaaa",
                        "description": "lalalalalal",
                        "userId": "b126ed50-7412-48ee-a8ca-3702badfba30"
                    },
                    {
                        "id": "e8b530a5-a48a-4697-965e-78f215bdaf3d",
                        "title": "striaaa",
                        "description": "lalalalalal",
                        "userId": "8b28ff41-6133-4513-8635-b8e481b347c1"
                    },
                    {
                        "id": "ee1793dc-dcd6-4e35-929c-cd0fe7a0532e",
                        "title": "striaaa",
                        "description": "lalalalalal",
                        "userId": "93ff08d4-7153-4453-b7d6-8fea6911770d"
                    }
                ]
                """;

            ObjectMapper objectMapper = new ObjectMapper();
            List<NewsletterDTO> newsletterDTOs = objectMapper.readValue(newslettersJson, new TypeReference<List<NewsletterDTO>>() {});

            List<Newsletter> newsletters = newsletterDTOs.stream().map(dto -> {
                Newsletter newsletter = new Newsletter();
                newsletter.setId(UUID.fromString(dto.getId()));
                newsletter.setTitle(dto.getTitle());
                newsletter.setDescription(dto.getDescription());

                Users user = userRepository.findById(UUID.fromString(dto.getUserId()))
                        .orElseThrow(() -> new RuntimeException("User not found: " + dto.getUserId()));
                newsletter.setUser(user);

                return newsletter;
            }).toList();

            newsletterRepository.saveAll(newsletters);
            System.out.println("Newsletters seeded successfully!");
        }
    }

    // DTO classes for deserialization
    private static class StatisticsDTO {
        private String id;
        private double averagePace;
        private double bestPace;
        private double totalTime;
        private String userId;
        private String typeId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

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

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getTypeId() {
            return typeId;
        }

        public void setTypeId(String typeId) {
            this.typeId = typeId;
        }
    }

    private static class OrderDTO {
        private String id;
        private String equipmentId;
        private String userId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEquipmentId() {
            return equipmentId;
        }

        public void setEquipmentId(String equipmentId) {
            this.equipmentId = equipmentId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }

    private static class NewsletterDTO {
        private String id;
        private String title;
        private String description;
        private String userId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

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

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }

}