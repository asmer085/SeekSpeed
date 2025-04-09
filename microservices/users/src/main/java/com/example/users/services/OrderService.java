package com.example.users.services;

import com.example.users.dtos.OrdersDTO;
import com.example.users.entity.Orders;
import com.example.users.mappers.OrdersMapper;
import com.example.users.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Validator validator;


    public Iterable<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    public Orders getOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with id " + orderId + " not found"));
    }

    public OrdersDTO createOrder(OrdersDTO orderDTO) {
        Orders order = ordersMapper.ordersDTOToOrders(orderDTO);
        Orders savedOrder = orderRepository.save(order);
        return ordersMapper.ordersToOrdersDTO(savedOrder);
    }

    @Transactional
    public ResponseEntity<Orders> updateOrder(UUID orderId, OrdersDTO updatedOrder) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    order.setEquipmentId(updatedOrder.getEquipmentId());
                    order.setUserId(updatedOrder.getUserId());
                    return ResponseEntity.ok(orderRepository.save(order));
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    public Orders applyPatchToOrder(JsonPatch patch, UUID orderId) {
        try {
            Orders order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new OrderNotFoundException("Order with id " + orderId + " not found"));
            JsonNode orderNode = objectMapper.valueToTree(order);
            JsonNode patchedNode = patch.apply(orderNode);
            Orders patchedOrder = objectMapper.treeToValue(patchedNode, Orders.class);

            Set<ConstraintViolation<Orders>> violations = validator.validate(patchedOrder);
            if (!violations.isEmpty()) {
                StringBuilder errorMessage = new StringBuilder("Validation failed: ");
                for (ConstraintViolation<Orders> violation : violations) {
                    errorMessage.append(violation.getMessage()).append(" ");
                }
                throw new RuntimeException(errorMessage.toString());
            }
            return orderRepository.save(patchedOrder);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public ResponseEntity<Object> deleteOrder(UUID orderId) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    orderRepository.delete(order);
                    return ResponseEntity.ok().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public static class OrderNotFoundException extends RuntimeException {
        public OrderNotFoundException(String message) {
            super(message);
        }
    }
}
