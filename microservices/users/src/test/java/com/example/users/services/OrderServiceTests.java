package com.example.users.services;

import com.example.users.dtos.OrdersDTO;
import com.example.users.entity.Orders;
import com.example.users.mappers.OrdersMapper;
import com.example.users.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTests {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrdersMapper ordersMapper;

    @InjectMocks
    private OrderService orderService;

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
    }

    @Test
    void getAllOrders_ShouldReturnAllOrders() {
        // Arrange
        when(orderRepository.findAll()).thenReturn(List.of(testOrder));

        // Act
        Iterable<Orders> result = orderService.getAllOrders();

        // Assert
        assertNotNull(result);
        assertEquals(1, ((List<Orders>) result).size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void getOrderById_ExistingId_ShouldReturnOrder() {
        // Arrange
        when(orderRepository.findById(testOrderId)).thenReturn(Optional.of(testOrder));

        // Act
        Orders result = orderService.getOrderById(testOrderId);

        // Assert
        assertNotNull(result);
        assertEquals(testOrder, result);
        verify(orderRepository, times(1)).findById(testOrderId);
    }

    @Test
    void getOrderById_NonExistingId_ShouldThrowException() {
        // Arrange
        when(orderRepository.findById(testOrderId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(OrderService.OrderNotFoundException.class, () -> {
            orderService.getOrderById(testOrderId);
        });
        verify(orderRepository, times(1)).findById(testOrderId);
    }

    @Test
    void createOrder_ValidOrder_ShouldReturnOrderDTO() {
        // Arrange
        when(ordersMapper.ordersDTOToOrders(any(OrdersDTO.class))).thenReturn(testOrder);
        when(orderRepository.save(any(Orders.class))).thenReturn(testOrder);
        when(ordersMapper.ordersToOrdersDTO(any(Orders.class))).thenReturn(testOrderDTO);

        // Act
        OrdersDTO result = orderService.createOrder(testOrderDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testOrderDTO, result);
        verify(orderRepository, times(1)).save(any(Orders.class));
    }

    @Test
    void updateOrder_ExistingOrder_ShouldReturnUpdatedOrder() {
        // Arrange
        Orders updatedOrder = new Orders();
        updatedOrder.setEquipmentId(UUID.randomUUID());

        when(orderRepository.findById(testOrderId)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Orders.class))).thenReturn(updatedOrder);

        // Act
        ResponseEntity<Orders> result = orderService.updateOrder(testOrderId, updatedOrder);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        verify(orderRepository, times(1)).findById(testOrderId);
        verify(orderRepository, times(1)).save(any(Orders.class));
    }

    @Test
    void updateOrder_NonExistingOrder_ShouldReturnNotFound() {
        // Arrange
        when(orderRepository.findById(testOrderId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Orders> result = orderService.updateOrder(testOrderId, testOrder);

        // Assert
        assertNotNull(result);
        assertEquals(404, result.getStatusCodeValue());
        verify(orderRepository, times(1)).findById(testOrderId);
        verify(orderRepository, never()).save(any(Orders.class));
    }

    @Test
    void deleteOrder_ExistingOrder_ShouldReturnOk() {
        // Arrange
        when(orderRepository.findById(testOrderId)).thenReturn(Optional.of(testOrder));

        // Act
        ResponseEntity<Object> result = orderService.deleteOrder(testOrderId);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        verify(orderRepository, times(1)).findById(testOrderId);
        verify(orderRepository, times(1)).delete(testOrder);
    }

    @Test
    void deleteOrder_NonExistingOrder_ShouldReturnNotFound() {
        // Arrange
        when(orderRepository.findById(testOrderId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Object> result = orderService.deleteOrder(testOrderId);

        // Assert
        assertNotNull(result);
        assertEquals(404, result.getStatusCodeValue());
        verify(orderRepository, times(1)).findById(testOrderId);
        verify(orderRepository, never()).delete(any(Orders.class));
    }
}