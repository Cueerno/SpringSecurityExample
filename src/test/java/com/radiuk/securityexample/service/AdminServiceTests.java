package com.radiuk.securityexample.service;

import com.radiuk.securityexample.model.Role;
import com.radiuk.securityexample.model.User;
import com.radiuk.securityexample.repository.UserRepository;
import com.radiuk.securityexample.service.impl.AdminServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    @Test
    public void AdminService_GetUsers_Success() {
        List<User> users = List.of(
                User.builder().id(1L).username("user1").email("user1@example.com").build(),
                User.builder().id(2L).username("user2").email("user2@example.com").build()
        );

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = adminService.getUsers();

        assertFalse(result.isEmpty());
        assertEquals(2, result.size());

        assertEquals("user1", result.get(0).getUsername());
        assertEquals("user2", result.get(1).getUsername());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void AdminService_MakeAdmin_Success() {
        Long userId = 1L;
        User existingUser = User.builder()
                .id(userId)
                .username("username")
                .role(Role.ROLE_USER)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        adminService.makeAdmin(userId);

        assertEquals(Role.ROLE_ADMIN, existingUser.getRole());

        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    public void AdminService_DeleteById_Success() {
        Long userId = 1L;
        User existingUser = User.builder().id(userId).username("username").build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        adminService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }
}
