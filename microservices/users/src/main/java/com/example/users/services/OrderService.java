package com.example.users.services;

import com.example.users.entity.Orders;
import com.example.users.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    // Metoda za dobijanje svih porudžbina
    public Iterable<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    public Orders getOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order with id " + orderId + " not found"));
    }

    public Orders createOrder(Orders order) {
        return orderRepository.save(order);
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
}
