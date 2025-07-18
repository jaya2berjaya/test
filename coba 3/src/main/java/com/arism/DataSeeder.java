package com.arism;

import com.arism.model.*;
import com.arism.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ROLE_ADMIN");
            userRepository.save(admin);

            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRole("ROLE_USER");
            userRepository.save(user);

            Cart userCart = new Cart();
            userCart.setUser(user);
            cartRepository.save(userCart);
        }

        if (categoryRepository.count() == 0) {
            Category pakaian = new Category();
            pakaian.setName("Pakaian");
            categoryRepository.save(pakaian);

            Category sepatu = new Category();
            sepatu.setName("Sepatu");
            categoryRepository.save(sepatu);

            Product p1 = new Product("Kemeja Polos", "Kemeja bahan katun warna putih", 150000.0, 10);
            p1.setCategory(pakaian);
            Product p2 = new Product("Celana Jeans", "Celana jeans slim fit biru", 200000.0, 15);
            p2.setCategory(pakaian);
            Product p3 = new Product("Sneakers Putih", "Sneakers putih unisex", 300000.0, 5);
            p3.setCategory(sepatu);
            productRepository.save(p1);
            productRepository.save(p2);
            productRepository.save(p3);
        }
    }
}