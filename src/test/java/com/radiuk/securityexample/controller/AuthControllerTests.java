package com.radiuk.securityexample.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radiuk.securityexample.config.SecurityConfigTest;
import com.radiuk.securityexample.dto.UserAuthDTO;
import com.radiuk.securityexample.dto.UserRegistrationDTO;
import com.radiuk.securityexample.security.JwtCore;
import com.radiuk.securityexample.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(SecurityConfigTest.class)
public class AuthControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtCore jwtCore;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void AuthController_signup_ShouldReturnCreated() throws Exception {
        UserRegistrationDTO userRegistrationDTO = UserRegistrationDTO.builder()
                .fullname("fullname")
                .username("username")
                .password("password")
                .email("email@email.com")
                .phone("+123(12)1234567")
                .build();

        mockMvc.perform(post("/api/v1/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegistrationDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string("User created"));

        verify(userService, times(1)).register(any(UserRegistrationDTO.class));
    }

    @Test
    void AuthController_login_ShouldReturnToken() throws Exception {
        UserAuthDTO userAuthDTO = UserAuthDTO.builder()
                .username("username")
                .password("password")
                .build();

        UserDetails userDetails = new User("username", "password", List.of());

        when(userDetailsService.loadUserByUsername("username")).thenReturn(userDetails);
        when(jwtCore.generateToken(userDetails)).thenReturn("mock-token");

        mockMvc.perform(post("/api/v1/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userAuthDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['token:']").value("mock-token"));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtCore).generateToken(userDetails);
    }

    @Test
    void AuthController_logout_ShouldClearContext() throws Exception {
        mockMvc.perform(post("/api/v1/user/logout"))
                .andExpect(status().isOk())
                .andExpect(content().string("Logout successful"));
    }

    @Test
    void AuthController_update_ShouldReturnOk() throws Exception {
        UserRegistrationDTO dto = UserRegistrationDTO.builder()
                .fullname("fullname")
                .username("username")
                .password("password")
                .email("email@email.com")
                .phone("+123(12)1234567")
                .build();

        mockMvc.perform(patch("/api/v1/user/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("User updated"));

        verify(userService).update(any(UserRegistrationDTO.class));
    }

    @Test
    void AuthController_delete_ShouldReturnOk() throws Exception {
        UserDetails userDetails = new User("username", "password", List.of());
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContextHolder.setContext(securityContext);

        mockMvc.perform(delete("/api/v1/user/delete"))
                .andExpect(status().isOk())
                .andExpect(content().string("User Deleted"));

        verify(userService).delete("username");
    }
}
