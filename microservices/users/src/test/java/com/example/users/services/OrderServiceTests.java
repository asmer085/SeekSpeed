package com.example.users.services;

import com.example.users.dto.OrdersDTO;
import com.example.users.entity.Orders;
import com.example.users.mappers.OrdersMapper;
import com.example.users.repository.OrderRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.*;

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

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Validator validator;

    private Orders testOrder;
    private OrdersDTO testOrderDTO;
    private OrdersDTO updatedOrderDTO;
    private UUID testOrderId;
    private UUID testUserId;
    private UUID testEquipmentId;
    private UUID newEquipmentId;

    @BeforeEach
    void setUp() {
        testOrderId = UUID.randomUUID();
        testUserId = UUID.randomUUID();
        testEquipmentId = UUID.randomUUID();
        newEquipmentId = UUID.randomUUID();

        testOrder = new Orders();
        testOrder.setId(testOrderId);
        testOrder.setUserId(testUserId);
        testOrder.setEquipmentId(testEquipmentId);

        testOrderDTO = new OrdersDTO();
        testOrderDTO.setUserId(testUserId);
        testOrderDTO.setEquipmentId(testEquipmentId);

        updatedOrderDTO = new OrdersDTO();
        updatedOrderDTO.setUserId(testUserId);
        updatedOrderDTO.setEquipmentId(newEquipmentId);
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
        updatedOrder.setId(testOrderId);
        updatedOrder.setUserId(testUserId);
        updatedOrder.setEquipmentId(newEquipmentId);

        when(orderRepository.findById(testOrderId)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Orders.class))).thenReturn(updatedOrder);

        // Act
        ResponseEntity<Orders> result = orderService.updateOrder(testOrderId, updatedOrderDTO);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertNotNull(result.getBody());
        assertEquals(newEquipmentId, result.getBody().getEquipmentId());
        assertEquals(testUserId, result.getBody().getUserId());
        verify(orderRepository, times(1)).findById(testOrderId);
        verify(orderRepository, times(1)).save(any(Orders.class));
    }

    @Test
    void updateOrder_NonExistingOrder_ShouldReturnNotFound() {
        // Arrange
        when(orderRepository.findById(testOrderId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Orders> result = orderService.updateOrder(testOrderId, updatedOrderDTO);

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

    @Test
    @Transactional
    void applyPatchToOrder_ValidPatch_ShouldReturnPatchedOrder() throws Exception {
        // Arrange
        String patchJson = "[{\"op\":\"replace\",\"path\":\"/equipmentId\",\"value\":\"" + newEquipmentId + "\"}]";
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchJson));

        when(orderRepository.findById(testOrderId)).thenReturn(Optional.of(testOrder));

        // Mock the objectMapper behavior
        ObjectMapper realMapper = new ObjectMapper();
        JsonNode orderNode = realMapper.valueToTree(testOrder);
        JsonNode patchedNode = patch.apply(orderNode);
        Orders patchedOrder = realMapper.treeToValue(patchedNode, Orders.class);
        patchedOrder.setId(testOrderId);

        when(objectMapper.valueToTree(any(Orders.class))).thenReturn(orderNode);
        when(objectMapper.treeToValue(any(JsonNode.class), eq(Orders.class))).thenReturn(patchedOrder);

        // Mock validation to pass
        Set<ConstraintViolation<Orders>> emptyViolations = Collections.emptySet();
        when(validator.validate(any(Orders.class))).thenReturn(emptyViolations);

        when(orderRepository.save(patchedOrder)).thenReturn(patchedOrder);

        // Act
        Orders result = orderService.applyPatchToOrder(patch, testOrderId);

        // Assert
        assertNotNull(result);
        assertEquals(newEquipmentId, result.getEquipmentId());
        assertEquals(testUserId, result.getUserId()); // unchanged
        verify(orderRepository, times(1)).findById(testOrderId);
        verify(orderRepository, times(1)).save(patchedOrder);
    }

    @Test
    @Transactional
    void applyPatchToOrder_InvalidPatchOperation_ShouldThrowException() throws Exception {
        // Arrange
        String invalidPatchJson = "[{\"op\":\"replace\",\"path\":\"/invalidField\",\"value\":\"value\"}]";
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(invalidPatchJson));

        when(orderRepository.findById(testOrderId)).thenReturn(Optional.of(testOrder));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            orderService.applyPatchToOrder(patch, testOrderId);
        });
    }
}