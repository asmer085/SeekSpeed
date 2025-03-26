package com.example.users.controllers;

import com.example.users.entity.Orders;
import com.example.users.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/all")
    public @ResponseBody Iterable<Orders> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{orderId}")
    public @ResponseBody Orders getOrderById(@PathVariable UUID orderId) {
        return orderService.getOrderById(orderId);
    }

    @PostMapping("/add")
    public @ResponseBody Orders createOrder(@RequestBody Orders order) {
        return orderService.createOrder(order);
    }

    @PutMapping("/{orderId}")
    public @ResponseBody ResponseEntity<Orders> updateOrder(@PathVariable UUID orderId, @RequestBody Orders updatedOrder) {
        return orderService.updateOrder(orderId, updatedOrder);
    }

    @DeleteMapping("/{orderId}")
    public @ResponseBody ResponseEntity<Object> deleteOrder(@PathVariable UUID orderId) {
        return orderService.deleteOrder(orderId);
    }
}
