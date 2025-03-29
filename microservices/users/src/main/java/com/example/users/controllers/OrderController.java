package com.example.users.controllers;

import com.example.users.dtos.OrdersDTO;
import com.example.users.entity.Orders;
import com.example.users.services.OrderService;
import com.example.users.services.UserService;
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
    public ResponseEntity<Orders> getOrderById(@PathVariable UUID orderId) {
        try {
            Orders order = orderService.getOrderById(orderId);
            return ResponseEntity.ok(order);
        } catch (OrderService.OrderNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    public @ResponseBody OrdersDTO createOrder(@RequestBody OrdersDTO orderDTO) {
        return orderService.createOrder(orderDTO);
    }

    @PutMapping("/{orderId}")
    public @ResponseBody ResponseEntity<Orders> updateOrder(@PathVariable UUID orderId, @RequestBody Orders updatedOrder) {
        return orderService.updateOrder(orderId, updatedOrder);
    }

    @DeleteMapping("/{orderId}")
    public @ResponseBody ResponseEntity<Object> deleteOrder(@PathVariable UUID orderId) {
        return orderService.deleteOrder(orderId);
    }

    @ExceptionHandler(OrderService.OrderNotFoundException.class)
    public ResponseEntity<Object> handleOrderNotFound(OrderService.OrderNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

}
