package com.arism.service;

import com.arism.exception.ResourceNotFoundException;
import com.arism.model.User;
import com.arism.repository.UserRepository;
import com.arism.dto.ChangePasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Register User
    public User registerUser(User user) {
        // Check if the email sent have already exxisted in the user table and throw exception if its already exist
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalStateException("User already exists");
        }
        // Setting the User object properties, and encode password
        user.setName(user.getName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(User.Role.USER);
        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        // Get user object by email
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public void changePassword(String email, ChangePasswordRequest request) {
        // Method to update password by providing the request containing currentPassword and newPassword
        User user = getUserByEmail(email);

        // Check if the current password sent in the request matches the user's password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadCredentialsException("Incorrect password");
        }

        // Update user's password and persist in the users table
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
