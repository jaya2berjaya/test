package com.arism.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import com.arism.dto.ChangePasswordRequest;
import com.arism.dto.LoginRequest;
import com.arism.model.User;
import com.arism.service.JwtService;
import com.arism.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Authentication", description = "Login & Register")
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;

    @Operation(summary = "Login", description = "Autentikasi pengguna dan menghasilkan JWT token")
    @ApiResponse(responseCode = "200", description = "Berhasil login")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        // Unauthenticated user can login into the service by providing their email and password
        // the authentication service then give the user JWT token to validate their session
        final UserDetails userDetails = userService.getUserByEmail(request.getEmail());
        final String jwtToken = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(jwtToken);
    }

    @Operation(summary = "Register", description = "Registrasi pengguna baru")
    @ApiResponse(responseCode = "200", description = "Berhasil registrasi")
    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        // Anauthenticated user can register to the service by providing their required user details
        return ResponseEntity.ok(userService.registerUser(user));
    }

    @PostMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        // Authenticated user can change their password by providing their existing password and new password
        // then the user getting their password validated
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        userService.changePassword(email, request);
        return ResponseEntity.ok().body("Password Changed Successfully");
    }
}