package com.radiuk.securityexample.controller;

import com.radiuk.securityexample.model.User;
import com.radiuk.securityexample.security.JwtCore;
import com.radiuk.securityexample.security.UserDetailsImpl;
import com.radiuk.securityexample.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MainController.class)
@AutoConfigureMockMvc(addFilters = false)
class MainControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtCore jwtCore;

    @Test
    void MainController_testGetAllData() throws Exception {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");

        UserDetailsImpl mockUserDetails = mock(UserDetailsImpl.class);
        when(mockUserDetails.getUser()).thenReturn(mockUser);

        when(userService.getUserDetails()).thenReturn(mockUserDetails);
        when(userService.findById(1L)).thenReturn(mockUser);

        mockMvc.perform(get("/api/v1/main/data"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void MainController_testGetUsername() throws Exception {
        UserDetailsImpl mockUserDetails = mock(UserDetailsImpl.class);
        when(mockUserDetails.getUsername()).thenReturn("testuser");
        when(userService.getUserDetails()).thenReturn(mockUserDetails);

        mockMvc.perform(get("/api/v1/main/username"))
                .andExpect(status().isOk())
                .andExpect(content().string("testuser"));
    }
}
