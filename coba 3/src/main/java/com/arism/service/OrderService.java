package com.arism.service;

import com.arism.dto.CartDto;
import com.arism.dto.OrderDto;
import com.arism.exception.InsufficientStockException;
import com.arism.exception.ResourceNotFoundException;
import com.arism.mapper.CartMapper;
import com.arism.mapper.OrderMapper;
import com.arism.model.*;
import com.arism.repository.OrderRepository;
import com.arism.repository.ProductRepository;
import com.arism.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final CartMapper cartMapper;

    @Transactional
    public OrderDto createOrder(Long userId, String address, String phoneNumber) {
        //  Check if the user is authenticated
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Get the cartDto object by the user's id
        CartDto cartDto = cartService.getCart(userId);

        // Convert cartDto into Cart object
        Cart cart = cartMapper.toEntity(cartDto);

        // Check if the cart is empty, throws exception if the user's cart is empty
        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Can't create order when the cart is empty");
        }

        // Create Order object and fill in the from request, then persist in the DB
        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setPhoneNumber(phoneNumber);
        order.setStatus(Order.OrderStatus.PREPARING);
        order.setCreatedAt(LocalDateTime.now());
        orderRepository.save(order);

        // Create order items in the order object from the user's cart
        List<OrderItem> orderItems = createOrderItems(cart, order);
        order.setItems(orderItems);

        // Clear the user's cart after order is saved
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(userId);

        // Returning the order as orderDto object to the user
        return orderMapper.toDto(savedOrder);
    }

    // The implementation of converting cart items into order items
    private List<OrderItem> createOrderItems(Cart cart, Order order) {
        return cart.getItems().stream().map(
                cartItem -> {
                    Product product = productRepository.findById(cartItem.getProduct().getId())
                            .orElseThrow(() -> new EntityNotFoundException("Product not found: " + cartItem.getProduct().getName()));

                    // Check if the product quantity requested is null
                    if(product.getQuantity() == null){
                        throw new IllegalStateException("Product quantity is not set for product "+product.getName());
                    }

                    // Check if the inventory quantity is less than requested quantity
                    if(product.getQuantity() < cartItem.getQuantity()){
                        try {
                            throw new InsufficientStockException("Not enough stock for product "+product.getName());
                        } catch (InsufficientStockException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    // Substract product/inventory qty with order quantity when order placed/saved
                    product.setQuantity(product.getQuantity() - cartItem.getQuantity());
                    productRepository.save(product);

                    return new OrderItem(null, cartItem.getQuantity(), product.getPrice(), order, product);
                }).collect(Collectors.toList());
    }

    public List<OrderDto> getAllOrders() {
        // Get all orders from the order repository
        return orderMapper.toDtos(orderRepository.findAll());
    }

    public List<OrderDto> getAllOrdersByUserId(Long userId) {
        // Get the order object based on user id
        return orderMapper.toDtos(orderRepository.findByUserId(userId));
    }

    public OrderDto updateOrderStatus(Long orderId, Order.OrderStatus status) {
        // Check if order exists
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Set order status, and persist in the DB
        order.setStatus(status);
        orderRepository.save(order);

        // Return the order as orderDto object to the user
        return orderMapper.toDto(order);
    }
}
