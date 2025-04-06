package com.example.users.controllers;

import com.example.users.dtos.OrdersDTO;
import com.example.users.entity.Orders;
import com.example.users.services.OrderService;
import com.example.users.services.UserService;
import com.github.fge.jsonpatch.JsonPatch;
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
    public @ResponseBody ResponseEntity<Orders> updateOrder(@PathVariable UUID orderId, @RequestBody OrdersDTO updatedOrderDTO) {
        return orderService.updateOrder(orderId, updatedOrderDTO);
    }

    @PatchMapping("/{orderId}")
    public @ResponseBody ResponseEntity<?> patchUpdateOrder(@PathVariable UUID orderId, @RequestBody JsonPatch patch) {
        try {
            Orders updatedOrder = orderService.applyPatchToOrder(patch, orderId);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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
