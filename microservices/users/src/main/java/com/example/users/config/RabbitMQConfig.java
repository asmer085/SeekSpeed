package com.example.users.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.ClassMapper;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    public static final String USER_EXCHANGE = "user.exchange";
    public static final String USER_SEND_QUEUE = "user.send.queue";
    public static final String USER_ROUTING_KEY = "user.routing.key";

    public static final String USER_UPDATE_EXCHANGE = "user.update.exchange";
    public static final String USER_UPDATE_QUEUE = "user.update.queue";
    public static final String USER_UPDATE_ROUTING_KEY = "user.update.routing.key";

    public static final String USER_DELETE_EXCHANGE = "user.delete.exchange";
    public static final String USER_DELETE_QUEUE = "user.delete.queue";
    public static final String USER_DELETE_ROUTING_KEY = "user.delete.routing.key";

    public static final String USER_POST_EXCHANGE = "user.create.exchange";
    public static final String USER_POST_QUEUE = "user.crate.queue";
    public static final String USER_POST_ROUTING_KEY = "user.create.routing.key";

    public static final String USER_BATCH_EXCHANGE = "user.batch.create.exchange";
    public static final String USER_BATCH_QUEUE = "user.batch.create.queue";
    public static final String USER_BATCH_ROUTING_KEY = "user.batch.create:routing.key";

    public static final String TYPE_EXCHANGE = "type.exchange";
    public static final String TYPE_RECEIVE_QUEUE = "type.send.queue";
    public static final String TYPE_ROUTING_KEY = "type.routing.key";

    public static final String TYPE_UPDATE_EXCHANGE = "type.update.exchange";
    public static final String TYPE_UPDATE_QUEUE = "type.update.queue";
    public static final String TYPE_UPDATE_ROUTING_KEY = "type.update.routing.key";

    public static final String TYPE_POST_EXCHANGE = "type.create.exchange";
    public static final String TYPE_POST_QUEUE = "type.create.queue";
    public static final String TYPE_POST_ROUTING_KEY = "type.create.routing.key";

    // Configuration for updating users data to Events service
    @Bean
    public TopicExchange userUpdate() {
        return new TopicExchange(USER_UPDATE_EXCHANGE);
    }

    @Bean
    public Queue userUpdateQueue() {
        return new Queue(USER_UPDATE_QUEUE, false);
    }

    @Bean
    public Binding userUpdateBinding() {
        return BindingBuilder.bind(userUpdateQueue())
                .to(userUpdate())
                .with(USER_UPDATE_ROUTING_KEY);
    }

    // Configuration for creating batch users data to Events service
    @Bean
    public TopicExchange userBatch() {
        return new TopicExchange(USER_BATCH_EXCHANGE);
    }

    @Bean
    public Queue userBatchQueue() {
        return new Queue(USER_BATCH_QUEUE, false);
    }

    @Bean
    public Binding userBatchBinding() {
        return BindingBuilder.bind(userBatchQueue())
                .to(userBatch())
                .with(USER_BATCH_ROUTING_KEY);
    }

    // Configuration for deleting user data to Events service
    @Bean
    public TopicExchange userDelete() {
        return new TopicExchange(USER_DELETE_EXCHANGE);
    }

    @Bean
    public Queue userDeleteQueue() {
        return new Queue(USER_DELETE_QUEUE, false);
    }

    @Bean
    public Binding userDeleteBinding() {
        return BindingBuilder.bind(userDeleteQueue())
                .to(userDelete())
                .with(USER_DELETE_ROUTING_KEY);
    }

    // Configuration for creating user data to Events service
    @Bean
    public TopicExchange userCreate() {
        return new TopicExchange(USER_POST_EXCHANGE);
    }

    @Bean
    public Queue userCreateQueue() {
        return new Queue(USER_POST_QUEUE, false);
    }

    @Bean
    public Binding userCreateBinding() {
        return BindingBuilder.bind(userCreateQueue())
                .to(userCreate())
                .with(USER_POST_ROUTING_KEY);
    }

    // Configuration for sending users data to Events service
    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange(USER_EXCHANGE);
    }

    @Bean
    public Queue userSendQueue() {
        return new Queue(USER_SEND_QUEUE, true);
    }

    @Bean
    public Binding userBinding() {
        return BindingBuilder.bind(userSendQueue())
                .to(userExchange())
                .with(USER_ROUTING_KEY);
    }

    // Configuration for receiving type data from Events service
    @Bean
    public TopicExchange typeExchange() {
        return new TopicExchange(TYPE_EXCHANGE);
    }

    @Bean
    public Queue typeReceiveQueue() {
        return new Queue(TYPE_RECEIVE_QUEUE, true);
    }

    @Bean
    public Binding typeBinding() {
        return BindingBuilder.bind(typeReceiveQueue())
                .to(typeExchange())
                .with(TYPE_ROUTING_KEY);
    }

    // Configuration for creating type data to Users service
    @Bean
    public TopicExchange typeCreate() {
        return new TopicExchange(TYPE_POST_EXCHANGE);
    }

    @Bean
    public Queue typeCreateQueue() {
        return new Queue(TYPE_POST_QUEUE, false);
    }

    @Bean
    public Binding typeCreateBinding() {
        return BindingBuilder.bind(typeCreateQueue())
                .to(typeCreate())
                .with(TYPE_POST_ROUTING_KEY);
    }

    // Configuration for updating type data to Users service
    @Bean
    public TopicExchange typeUpdate() {
        return new TopicExchange(TYPE_UPDATE_EXCHANGE);
    }

    @Bean
    public Queue typeUpdateQueue() {
        return new Queue(TYPE_UPDATE_QUEUE, false);
    }

    @Bean
    public Binding typeUpdateBinding() {
        return BindingBuilder.bind(typeUpdateQueue())
                .to(typeUpdate())
                .with(TYPE_UPDATE_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setClassMapper(classMapper());
        return converter;
    }

    @Bean
    public ClassMapper classMapper() {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("com.example.users.dto.EventUserDTO",
                com.example.users.dto.EventUserDTO.class);
        idClassMapping.put("com.example.events.dto.UserTypeDTO",
                com.example.users.dto.UserTypeDTO.class);
        idClassMapping.put("java.util.List", List.class);
        idClassMapping.put("com.example.users.dto.UserUpdateEventDT0",
                com.example.users.dto.UserUpdateEventDTO.class);
        classMapper.setIdClassMapping(idClassMapping);

        classMapper.setTrustedPackages("*");
        return classMapper;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

}
