package com.arism.controller;

import com.arism.dto.OrderDto;
import com.arism.model.Order;
import com.arism.model.User;
import com.arism.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderDto> save(@AuthenticationPrincipal UserDetails userDetails,
                                         @RequestParam String address,
                                         @RequestParam String phoneNumber) {
        // Authenticated user can create order from their cart
        // then the orders persisted into DB by providing their address and phone number
        Long userId = ((User) userDetails).getId();
        OrderDto orderDto = orderService.createOrder(userId, address, phoneNumber);
        return ResponseEntity.ok(orderDto);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        // Here, the User (with ADMIN role) can view All Orders in the form of List of objects
        List<OrderDto> orderDtos = orderService.getAllOrders();
        return ResponseEntity.ok(orderDtos);
    }

    @GetMapping("/user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<OrderDto>> getOrdersByUser(@AuthenticationPrincipal UserDetails userDetails) {
        // Authenticated user can check their own order on this endpoint
        Long userId = ((User) userDetails).getId();
        List<OrderDto> orderDtos = orderService.getAllOrdersByUserId(userId);
        return ResponseEntity.ok(orderDtos);
    }

    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<OrderDto> getOrdersByUser(@PathVariable Long orderId,
                                                    @RequestParam Order.OrderStatus status) {
        // Only ADMIN user can access this endpoint
        // The ADMIN can update the order status e.g to DELIVERING
        OrderDto updatedOrder = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(updatedOrder);
    }
}
