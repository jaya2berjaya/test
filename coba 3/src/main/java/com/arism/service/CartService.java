package com.arism.service;

import com.arism.dto.CartDto;
import com.arism.exception.InsufficientStockException;
import com.arism.exception.ResourceNotFoundException;
import com.arism.mapper.CartMapper;
import com.arism.model.Cart;
import com.arism.model.CartItem;
import com.arism.model.Product;
import com.arism.model.User;
import com.arism.repository.CartRepository;
import com.arism.repository.ProductRepository;
import com.arism.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;

    public CartDto addToCart(Long userId, Long productId, Integer quantity) throws InsufficientStockException {
        // This method executes the authenticated user adding items into their cart by providing the productId and quantity

        // Checking if user is logged in
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Checking if the item is present on the DB
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Checking if inventory (stock) is less than quantity requested, if true throws an InsufficientStockException
        if (product.getQuantity() < quantity) {
            throw new InsufficientStockException("Not enough stock available for item: " + product.getName());
        }

        // Check if the cart is present, or create their cart object
        Cart cart = cartRepository.findByUserId(userId)
                .orElse(new Cart(null, user, new ArrayList<>()));

        // List existing items on the cart
        Optional<CartItem> existingCartItems = cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        // Check if the item requested is already in the cart, if true, add the quantity
        if (existingCartItems.isPresent()) {
            CartItem cartItem = existingCartItems.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            //  if the item requested is not in the cart, add the item into the cart
            CartItem cartItem = new CartItem(null, cart, product, quantity);
            cart.getItems().add(cartItem);
        }

        // Persist the cart into DB, and return back as cart DTO object
        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toDto(savedCart);
    }

    public CartDto getCart(Long userId) {
        // View the cart object
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        return cartMapper.toDto(cart);
    }

    public void clearCart(Long userId) {
        // Clear cart content if the user have their cart in the DB
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
