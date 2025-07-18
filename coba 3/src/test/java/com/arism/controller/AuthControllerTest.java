package com.arism.controller;

import com.arism.dto.ChangePasswordRequest;
import com.arism.dto.LoginRequest;
import com.arism.model.User;
import com.arism.service.JwtService;
import com.arism.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class AuthControllerTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_Success() {
        // Arrange
        User user = new User();
        user.setEmail("register@example.com");

        when(userService.registerUser(user)).thenReturn(user);

        // Act
        ResponseEntity<User> response = authController.register(user);

        // Assert
        verify(userService).registerUser(user);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(user, response.getBody());
    }

    @Test
    void testLogin_Success() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("user@test.com");
        loginRequest.setPassword("password");

        User user = new User();
        user.setEmail(loginRequest.getEmail());
        user.setPassword("hashed-password"); // if needed

        String mockToken = "mock-jwt-token";

        when(userService.getUserByEmail(loginRequest.getEmail())).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn(mockToken);

        // Act
        ResponseEntity<String> response = authController.login(loginRequest);

        // Assert
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService).getUserByEmail(loginRequest.getEmail());
        verify(jwtService).generateToken(user);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("mock-jwt-token", response.getBody());
    }

    @Test
    void testChangePassword_Success() {
        // Arrange
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setCurrentPassword("old123");
        request.setNewPassword("new123");

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("user@test.com");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);

        SecurityContextHolder.setContext(securityContext);

        // Act
        ResponseEntity<?> response = authController.changePassword(request);

        // Assert
        verify(userService).changePassword("user@test.com", request);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Password Changed Successfully", response.getBody());
    }
}
