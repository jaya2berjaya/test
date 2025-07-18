package com.arism.service;

import com.arism.dto.ChangePasswordRequest;
import com.arism.model.User;
import com.arism.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Spy
    @InjectMocks
    private UserService userService;

    @Test
    public void UserService_RegisterUser() {
        User user = new User();
        user.setName("User Test");
        user.setPassword("123"); // raw password
        user.setRole(User.Role.USER);

        when(passwordEncoder.encode("123")).thenReturn("encoded123");

        // Set expected return object with ID
        User savedUserMock = new User();
        savedUserMock.setId(1L);
        savedUserMock.setName("User Test");
        savedUserMock.setPassword("encoded123");
        savedUserMock.setRole(User.Role.USER);

        when(userRepository.save(Mockito.any(User.class))).thenReturn(savedUserMock);

        User savedUser = userService.registerUser(user);

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
        Assertions.assertThat(savedUser.getName()).isEqualTo("User Test");
    }

    @Test
    void changePassword_ShouldUpdatePassword_WhenCurrentPasswordIsCorrect() {
        String email = "user@example.com";
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setCurrentPassword("oldPass");
        request.setNewPassword("newPass");

        User user = new User();
        user.setEmail(email);
        user.setPassword("encodedOldPassword");

        // Mock internal method call
        Mockito.doReturn(user).when(userService).getUserByEmail(email);

        // Mock password matching and encoding
        when(passwordEncoder.matches("oldPass", "encodedOldPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNewPassword");

        // Execute
        userService.changePassword(email, request);

        // Verify the password was updated and saved
        Assertions.assertThat(user.getPassword()).isEqualTo("encodedNewPassword");
        verify(userRepository).save(user);
    }

    @Test
    void changePassword_ShouldThrowException_WhenCurrentPasswordIsWrong() {
        String email = "user@example.com";
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setCurrentPassword("wrongPass");
        request.setNewPassword("newPass");

        User user = new User();
        user.setEmail(email);
        user.setPassword("encodedOldPassword");

        Mockito.doReturn(user).when(userService).getUserByEmail(email);
        when(passwordEncoder.matches("wrongPass", "encodedOldPassword")).thenReturn(false);

        // Expect exception
        Assertions.assertThatThrownBy(() -> userService.changePassword(email, request))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Incorrect password");

        verify(userRepository, never()).save(any());
    }

}
