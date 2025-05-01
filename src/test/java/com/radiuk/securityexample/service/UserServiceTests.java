package com.radiuk.securityexample.service;

import com.radiuk.securityexample.dto.UserRegistrationDTO;
import com.radiuk.securityexample.exception.UserNotFoundException;
import com.radiuk.securityexample.model.Role;
import com.radiuk.securityexample.model.User;
import com.radiuk.securityexample.repository.UserRepository;
import com.radiuk.securityexample.service.impl.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void UserService_findById_ReturnUser() {
        long userId = 1;

        User user = User.builder().id(1L).username("username").build();

        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user));

        User userReturn = userService.findById(userId);

        Assertions.assertThat(userReturn).isNotNull();
    }

    @Test
    public void UserService_Register_Success() {
        UserRegistrationDTO userRegistrationDTO = UserRegistrationDTO.builder().username("username").password("password").build();

        when(userRepository.existsUserByUsername(userRegistrationDTO.getUsername())).thenReturn(false);
        when(userRepository.existsUserByEmail(userRegistrationDTO.getEmail())).thenReturn(false);
        when(userRepository.existsUserByPhone(userRegistrationDTO.getPhone())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        userService.register(userRegistrationDTO);

        verify(userRepository, times(1)).save(Mockito.any(User.class));
    }

    @Test
    public void UserService_Update_Success() {
        UserRegistrationDTO userRegistrationDTO = UserRegistrationDTO.builder()
                .username("existingUser")
                .fullname("Updated Name")
                .email("updated@example.com")
                .phone("+123(45)6789012")
                .password("hashedPassword")
                .build();

        User existingUser = User.builder()
                .id(1L)
                .username("existingUser")
                .fullname("Old Name")
                .email("oldemail@email.com")
                .phone("+123(45)6789012")
                .password("hashedPassword")
                .role(Role.ROLE_USER)
                .createdAt(OffsetDateTime.now().minusDays(30))
                .updatedAt(OffsetDateTime.now().minusDays(5))
                .build();

        when(userRepository.findByUsername(existingUser.getUsername())).thenReturn(Optional.of(existingUser));

        userService.update(userRegistrationDTO);

        verify(userRepository, times(1)).save(argThat(user ->
                user.getId().equals(existingUser.getId()) &&
                        user.getFullname().equals("Updated Name") &&
                        user.getEmail().equals("updated@example.com") &&
                        user.getPhone().equals("+123(45)6789012") &&
                        user.getPassword().equals(existingUser.getPassword()) &&
                        user.getRole().equals(existingUser.getRole()) &&
                        user.getCreatedAt().equals(existingUser.getCreatedAt())
        ));
    }

    @Test
    public void UserService_DeleteByUsername_Success() {
        String username = "existingUser";
        User existingUser = User.builder().username(username).build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(existingUser));

        userService.delete(username);

        verify(userRepository, times(1)).deleteByUsername(username);
    }

    @Test
    public void UserService_UserNotFound_Success() {
        String username = "nonExistingUser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.delete(username));

        verify(userRepository, never()).deleteByUsername(anyString());
    }
}
