package com.example.users.controllers;

import com.example.users.entity.Orders;
import com.example.users.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/all")
    public @ResponseBody Iterable<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    @GetMapping("/{orderId}")
    public @ResponseBody Orders getOrderById(@PathVariable UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @PostMapping("/add")
    public @ResponseBody Orders createOrder(@RequestBody Orders order) {
        return orderRepository.save(order);
    }

    @PutMapping("/{orderId}")
    public @ResponseBody ResponseEntity<Orders> updateOrders(@PathVariable UUID orderId, @RequestBody Orders updatedOrder) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    order.setEquipmentId(updatedOrder.getEquipmentId());
                    order.setUserId(updatedOrder.getUserId());
                    return ResponseEntity.ok(orderRepository.save(order));
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{orderId}")
    public @ResponseBody ResponseEntity<Object> deleteOrder(@PathVariable UUID orderId) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    orderRepository.delete(order);
                    return ResponseEntity.ok().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}