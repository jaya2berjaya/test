package com.arism.controller;

import com.arism.model.Product;
import com.arism.model.User;
import com.arism.model.Wishlist;
import com.arism.repository.ProductRepository;
import com.arism.repository.UserRepository;
import com.arism.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/{productId}")
    public Wishlist addToWishlist(@PathVariable Long productId, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        Product product = productRepository.findById(productId).orElseThrow();
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setProduct(product);
        return wishlistRepository.save(wishlist);
    }

    @GetMapping
    public List<Wishlist> getUserWishlist(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        return wishlistRepository.findByUser(user);
    }
}