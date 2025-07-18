package com.arism.controller;

import com.arism.dto.CartDto;
import com.arism.exception.InsufficientStockException;
import com.arism.model.User;
import com.arism.repository.CartRepository;
import com.arism.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(100L);
        user.setEmail("user@example.com");
        // Add more fields as needed
    }

    @Test
    void testAddToCart_Success() throws InsufficientStockException {
        // Arrange
        Long productId = 1L;
        int quantity = 2;

        CartDto expectedCart = new CartDto();
        expectedCart.setUserId(user.getId());

        when(cartService.addToCart(user.getId(), productId, quantity)).thenReturn(expectedCart);

        // Act
        ResponseEntity<CartDto> response = cartController.addToCart(user, productId, quantity);

        // Assert
        verify(cartService).addToCart(user.getId(), productId, quantity);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedCart, response.getBody());
    }

    @Test
    void testGetCart_Success() {
        // Arrange
        CartDto cartDto = new CartDto();
        cartDto.setUserId(user.getId());

        when(cartService.getCart(user.getId())).thenReturn(cartDto);

        // Act
        ResponseEntity<CartDto> response = cartController.getCart(user);

        // Assert
        verify(cartService).getCart(user.getId());
        assertEquals(200, response.getStatusCode().value());
        assertEquals(cartDto, response.getBody());
    }

    @Test
    void testClearCart_Success() {
        // Act
        ResponseEntity<Void> response = cartController.clearCart(user);

        // Assert
        verify(cartService).clearCart(user.getId());
        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void testAddToCart_ThrowsInsufficientStockException() throws InsufficientStockException {
        // Arrange
        Long productId = 2L;
        int quantity = 10;

        when(cartService.addToCart(user.getId(), productId, quantity))
                .thenThrow(new InsufficientStockException("Not enough stock"));

        // Act & Assert
        assertThrows(InsufficientStockException.class, () ->
                cartController.addToCart(user, productId, quantity));

        verify(cartService).addToCart(user.getId(), productId, quantity);
    }

}
