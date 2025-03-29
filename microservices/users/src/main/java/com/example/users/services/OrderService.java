package com.example.users.services;

import com.example.users.dtos.OrdersDTO;
import com.example.users.entity.Orders;
import com.example.users.mappers.OrdersMapper;
import com.example.users.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrdersMapper ordersMapper;


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

    public ResponseEntity<Orders> updateOrder(UUID orderId, Orders updatedOrder) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    order.setEquipmentId(updatedOrder.getEquipmentId());
                    order.setUserId(updatedOrder.getUserId());
                    return ResponseEntity.ok(orderRepository.save(order));
                }).orElseGet(() -> ResponseEntity.notFound().build());
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
