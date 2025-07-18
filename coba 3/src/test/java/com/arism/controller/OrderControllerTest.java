package com.arism.controller;

import com.arism.dto.OrderDto;
import com.arism.model.Order;
import com.arism.model.User;
import com.arism.repository.OrderRepository;
import com.arism.service.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    @Mock
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderController orderController;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(123L);
        user.setEmail("user@example.com");
    }

    @Test
    void testSaveOrder_Success() {
        // Arrange
        String address = "Jl. Kebon Jeruk";
        String phoneNumber = "08123456789";

        OrderDto orderDto = new OrderDto();
        orderDto.setUserId(user.getId());
        orderDto.setAddress(address);
        orderDto.setPhoneNumber(phoneNumber);

        when(orderService.createOrder(user.getId(), address, phoneNumber)).thenReturn(orderDto);

        // Act
        ResponseEntity<OrderDto> response = orderController.save(user, address, phoneNumber);

        // Assert
        verify(orderService).createOrder(user.getId(), address, phoneNumber);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(orderDto, response.getBody());
    }

    @Test
    void testGetAllOrders_AdminSuccess() {
        // Arrange
        OrderDto order1 = new OrderDto();
        order1.setId(1L);

        OrderDto order2 = new OrderDto();
        order2.setId(2L);

        List<OrderDto> orderList = Arrays.asList(order1, order2);
        when(orderService.getAllOrders()).thenReturn(orderList);

        // Act
        ResponseEntity<List<OrderDto>> response = orderController.getAllOrders();

        // Assert
        verify(orderService).getAllOrders();
        assertEquals(200, response.getStatusCode().value());
        Assertions.assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetOrdersByUser_Success() {
        // Arrange
        OrderDto order1 = new OrderDto();
        order1.setId(1L);
        order1.setUserId(user.getId());

        List<OrderDto> userOrders = List.of(order1);
        when(orderService.getAllOrdersByUserId(user.getId())).thenReturn(userOrders);

        // Act
        ResponseEntity<List<OrderDto>> response = orderController.getOrdersByUser(user);

        // Assert
        verify(orderService).getAllOrdersByUserId(user.getId());
        assertEquals(200, response.getStatusCode().value());
        assertEquals(userOrders, response.getBody());
    }

    @Test
    void testUpdateOrderStatus_Success() {
        // Arrange
        Long orderId = 10L;
        Order.OrderStatus newStatus = Order.OrderStatus.DELIVERED;

        OrderDto updatedOrder = new OrderDto();
        updatedOrder.setId(orderId);
        updatedOrder.setStatus(newStatus);

        when(orderService.updateOrderStatus(orderId, newStatus)).thenReturn(updatedOrder);

        // Act
        ResponseEntity<OrderDto> response = orderController.getOrdersByUser(orderId, newStatus);

        // Assert
        verify(orderService).updateOrderStatus(orderId, newStatus);
        assertEquals(200, response.getStatusCode().value());
        Assertions.assertNotNull(response.getBody());
        assertEquals(newStatus, response.getBody().getStatus());
    }
}
