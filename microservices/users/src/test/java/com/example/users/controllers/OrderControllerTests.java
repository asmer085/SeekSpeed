package com.example.users.controllers;

import com.example.users.dtos.OrdersDTO;
import com.example.users.entity.Orders;
import com.example.users.services.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTests {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Orders testOrder;
    private OrdersDTO testOrderDTO;
    private UUID testOrderId;
    private UUID testUserId;
    private UUID testEquipmentId;

    @BeforeEach
    void setUp() {
        testOrderId = UUID.randomUUID();
        testUserId = UUID.randomUUID();
        testEquipmentId = UUID.randomUUID();

        testOrder = new Orders();
        testOrder.setId(testOrderId);
        testOrder.setUserId(testUserId);
        testOrder.setEquipmentId(testEquipmentId);

        testOrderDTO = new OrdersDTO();
        testOrderDTO.setUserId(testUserId);
        testOrderDTO.setEquipmentId(testEquipmentId);

        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllOrders_ShouldReturnOrders() throws Exception {
        // Arrange
        Iterable<Orders> orders = Arrays.asList(testOrder);
        given(orderService.getAllOrders()).willReturn(orders);

        // Act & Assert
        mockMvc.perform(get("/orders/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(testUserId.toString()))
                .andExpect(jsonPath("$[0].equipmentId").value(testEquipmentId.toString()));
    }

    @Test
    void getOrderById_ExistingId_ShouldReturnOrder() throws Exception {
        // Arrange
        given(orderService.getOrderById(testOrderId)).willReturn(testOrder);

        // Act & Assert
        mockMvc.perform(get("/orders/{orderId}", testOrderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(testUserId.toString()))
                .andExpect(jsonPath("$.equipmentId").value(testEquipmentId.toString()));
    }

    @Test
    void getOrderById_NonExistingId_ShouldThrowException() throws Exception {
        // Arrange
        given(orderService.getOrderById(testOrderId))
                .willThrow(new OrderService.OrderNotFoundException("Order not found"));

        // Act & Assert
        mockMvc.perform(get("/orders/{orderId}", testOrderId))
                .andExpect(status().isNotFound());
    }

    @Test
    void createOrder_ValidOrder_ShouldReturnCreatedOrder() throws Exception {
        // Arrange
        given(orderService.createOrder(any(OrdersDTO.class))).willReturn(testOrderDTO);

        // Act & Assert
        mockMvc.perform(post("/orders/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testOrderDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(testUserId.toString()))
                .andExpect(jsonPath("$.equipmentId").value(testEquipmentId.toString()));
    }

    @Test
    void updateOrder_ExistingOrder_ShouldReturnUpdatedOrder() throws Exception {
        // Arrange
        UUID newEquipmentId = UUID.randomUUID();
        Orders updatedOrder = new Orders();
        updatedOrder.setUserId(testUserId);
        updatedOrder.setEquipmentId(newEquipmentId);

        given(orderService.updateOrder(eq(testOrderId), any(Orders.class)))
                .willReturn(ResponseEntity.ok(updatedOrder));

        // Act & Assert
        mockMvc.perform(put("/orders/{orderId}", testOrderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedOrder)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(testUserId.toString()))
                .andExpect(jsonPath("$.equipmentId").value(newEquipmentId.toString()));
    }

    @Test
    void updateOrder_NonExistingOrder_ShouldReturnNotFound() throws Exception {
        // Arrange
        given(orderService.updateOrder(eq(testOrderId), any(Orders.class)))
                .willReturn(ResponseEntity.notFound().build());

        // Act & Assert
        mockMvc.perform(put("/orders/{orderId}", testOrderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testOrder)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteOrder_ExistingOrder_ShouldReturnOk() throws Exception {
        // Arrange
        given(orderService.deleteOrder(testOrderId))
                .willReturn(ResponseEntity.ok().build());

        // Act & Assert
        mockMvc.perform(delete("/orders/{orderId}", testOrderId))
                .andExpect(status().isOk());
    }

    @Test
    void deleteOrder_NonExistingOrder_ShouldReturnNotFound() throws Exception {
        // Arrange
        given(orderService.deleteOrder(testOrderId))
                .willReturn(ResponseEntity.notFound().build());

        // Act & Assert
        mockMvc.perform(delete("/orders/{orderId}", testOrderId))
                .andExpect(status().isNotFound());
    }
}