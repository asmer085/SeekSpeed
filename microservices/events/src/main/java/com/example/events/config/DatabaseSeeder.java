package com.example.events.config;

import com.example.events.dto.UserTypeDTO;
import com.example.events.entity.Event;
import com.example.events.entity.Review;
import com.example.events.entity.Types;
import com.example.events.entity.User;
import com.example.events.messaging.Listener;
import com.example.events.repository.EventRepository;
import com.example.events.repository.ReviewRepository;
import com.example.events.repository.TypeRepository;
import com.example.events.repository.UserRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final TypeRepository typeRepository;
    private final ReviewRepository reviewRepository;
    private final RabbitTemplate rabbitTemplate;
    private final Listener listener;

    public DatabaseSeeder(UserRepository userRepository,
                          EventRepository eventRepository,
                          TypeRepository typeRepository,
                          ReviewRepository reviewRepository,
                          RabbitTemplate rabbitTemplate,
                          Listener listener) {

        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.typeRepository = typeRepository;
        this.reviewRepository = reviewRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.listener = listener;
    }

    @Override
    public void run(String... args) throws InterruptedException {
        listener.waitForUsers();
        seedEvents();
        seedTypes();
        seedReviews();
        sendTypesToUsersService();
    }

    /*private void seedUsers() {
        if (userRepository.count() == 0) {
            List<User> users = List.of(
                    new User(UUID.randomUUID(),"JwtSignUp", "JwtToken", "jwtXoxo", "string@gmail.com", "jwt.jpg", "string", "user", "Female", "string", "L", UUID.fromString("1685148a-d460-406a-babe-adf96016ecbd")),
                    new User(UUID.randomUUID(),"stringAPRILproba1", "Promjena", "stringApril", "string@proba.com", "string", "string", "user",  "Male", "string", "M", UUID.fromString("5bd1e72b-0ab0-4fc0-be55-69b1b43d8bd6")),
                    new User(UUID.randomUUID(),"probaNoviAddtetete", "probaPatchLastName", "tetete", "string@gmail.com", "string", "string", "user",  "Male", "string", "XL", UUID.fromString("666d19e0-56ba-4865-90f9-9b229d466697")),
                    new User(UUID.randomUUID(),"probaNoviAdd", "probaPatchLastName", "probaNoviAdd", "string@gmail.com", "string", "string", "user", "Male", "string", "M", UUID.fromString("80d45932-cc33-41ff-af76-6a7853a5ca76")),
                    new User(UUID.randomUUID(),"stringAPRILproba11010", "Promjena", "proba11010", "string@proba.com", "string", "string", "organizer", "Male", "string", "M", UUID.fromString("8b28ff41-6133-4513-8635-b8e481b347c1")),
                    new User(UUID.randomUUID(),"Ana", "Anić", "anaAnic1", "ana.anic@example.com", "ana.jpg", "1985-08-22", "user", "Female", "Croatia", "M", UUID.fromString("93ff08d4-7153-4453-b7d6-8fea6911770d")),
                    new User(UUID.randomUUID(),"Marko", "Marković", "markoMark1", "marko.markovic@example.com", "marko.jpg", "1990-05-15", "admin", "Male", "Serbia", "L", UUID.fromString("97ab7a35-f239-4ee4-a01c-a293e0fdbfcc")),
                    new User(UUID.randomUUID(),"postmanTest", "patchPostmanPromjena", "postmanUsername1", "postmanTest@gmail", "string", "string", "user", "Male", "Croatia", "L", UUID.fromString("a2b31b60-79bf-4205-9726-b8765c91f6a5")),
                    new User(UUID.randomUUID(),"EMIR", "EMIRR", "emiremir1", "emiremir1@gmail.com", "emir2506.jpg", "1995-11-30", "organizer", "Male", "Bosnia and Herzegovina", "XL", UUID.fromString("b126ed50-7412-48ee-a8ca-3702badfba30")),
                    new User(UUID.randomUUID(),"Ivan", "Ivić", "ivanivi1", "ivan.ivic@example.com", "ivan.jpg", "1995-11-30","admin", "Male", "Bosnia and Herzegovina", "XL", UUID.fromString("cb2e5f28-f329-45be-b47e-210dfd0dcfe4")),
                    new User(UUID.randomUUID(),"oookay", "Ivić", "testUsername", "ivan.ivic@example.com", "ivan.jpg", "1995-11-30", "user", "Male", "Bosnia and Herzegovina", "XL", UUID.fromString("d09c6d51-df26-4700-a31d-d3b7f85ab511"))
            );

            userRepository.saveAll(users);
        }
    }*/

    private void seedEvents() {
        if (eventRepository.count() == 0) {
            // Get some organizer IDs from existing users
            User organizer1 = userRepository.findByUsername("emiremir1").orElseThrow();
            User organizer2 = userRepository.findByUsername("proba11010").orElseThrow();

            List<Event> events = List.of(
                    new Event("Bistrik 1", "Sarajevo", "Bosnia and Herzegovina", "running",
                            organizer1.getId(), "Sarajevo Marathon",
                            "Annual marathon through the beautiful streets of Sarajevo",
                            "2023-10-15", "09:00"),
                    new Event("Titova 15", "Mostar", "Bosnia and Herzegovina", "cycling",
                            organizer2.getId(), "Mostar Cycling Challenge",
                            "Enjoy cycling through the scenic routes around Mostar",
                            "2023-09-20", "10:30"),
                    new Event("Vrbaska 33", "Banja Luka", "Bosnia and Herzegovina", "hiking",
                            organizer1.getId(), "Banja Luka Mountain Trail",
                            "Hiking adventure in the beautiful mountains near Banja Luka",
                            "2023-11-05", "08:00")
            );

            eventRepository.saveAll(events);
        }
    }

    private void sendTypesToUsersService() {
        List<Types> types = typeRepository.findAll();
        List<UserTypeDTO> usersType = types.stream()
                .map(type -> new UserTypeDTO(
                        type.getDistance(),
                        type.getResults(),
                        type.getPrice(),
                        type.getId()
                )).toList();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.TYPE_EXCHANGE,
                RabbitMQConfig.TYPE_ROUTING_KEY,
                usersType,
                        message -> {
                            message.getMessageProperties().getHeaders().put("__TypeId__", "java.util.List");
                            return message;
                        }
        );
        System.out.println("****Types data sent to Users service****");
    }
    private void seedTypes() {
        if (typeRepository.count() == 0) {
            List<Event> events = (List<Event>) eventRepository.findAll();

            Event runningEvent = events.stream()
                    .filter(e -> e.getCategory().equals("running"))
                    .findFirst()
                    .orElseThrow();

            Event cyclingEvent = events.stream()
                    .filter(e -> e.getCategory().equals("cycling"))
                    .findFirst()
                    .orElseThrow();

            Event hikingEvent = events.stream()
                    .filter(e -> e.getCategory().equals("hiking"))
                    .findFirst()
                    .orElseThrow();

            List<Types> types = List.of(
                    // Running event types
                    new Types(25.0, "5km", "Individual results", runningEvent),
                    new Types(40.0, "10km", "Individual results", runningEvent),

                    // Cycling event types
                    new Types(30.0, "6km", "Team results", cyclingEvent),
                    new Types(50.0, "12km", "Team results", cyclingEvent),

                    // Hiking event type
                    new Types(15.0, "8km", "Group participation", hikingEvent)
            );

            typeRepository.saveAll(types);
        }
    }

    private void seedReviews() {
        if (reviewRepository.count() == 0) {
            List<Event> events = (List<Event>) eventRepository.findAll();
            List<User> users = (List<User>) userRepository.findAll();

            List<Review> reviews = List.of(
                    new Review(5, events.get(0), users.get(0).getId()),
                    new Review(4, events.get(1), users.get(1).getId()),
                    new Review(3, events.get(2), users.get(2).getId())
            );

            reviewRepository.saveAll(reviews);
        }
    }
}