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
    public static final String USER_UPDATE_QUEUE = "user.update.queue";

    public static final String USER_EXCHANGE = "user.exchange";
    public static final String USER_SEND_QUEUE = "user.send.queue";
    public static final String USER_ROUTING_KEY = "user.routing.key";

    public static final String TYPE_EXCHANGE = "type.exchange";
    public static final String TYPE_RECEIVE_QUEUE = "type.send.queue";
    public static final String TYPE_ROUTING_KEY = "type.routing.key";

    @Bean
    public Queue userUpdateQueue() {
        return new Queue(USER_UPDATE_QUEUE, false);
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
