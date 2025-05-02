package com.radiuk.securityexample.controller;

import com.radiuk.securityexample.dto.UserAuthDTO;
import com.radiuk.securityexample.dto.UserRegistrationDTO;
import com.radiuk.securityexample.security.JwtCore;
import com.radiuk.securityexample.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class AuthController {

    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final JwtCore jwtCore;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO) {
        userService.register(userRegistrationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserAuthDTO userAuthDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userAuthDTO.getUsername(), userAuthDTO.getPassword());
        authenticationManager.authenticate(authenticationToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userAuthDTO.getUsername());
        String token = jwtCore.generateToken(userDetails);
        return new ResponseEntity<>(Map.of("token:", token), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.status(HttpStatus.OK).body("Logout successful");
    }

    @PatchMapping("/update")
    public ResponseEntity<?> update(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO) {
        userService.update(userRegistrationDTO);
        return ResponseEntity.status(HttpStatus.OK).body("User updated");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete() {
        userService.deleteByUsername(userService.getUserDetails().getUsername());
        return ResponseEntity.status(HttpStatus.OK).body("User Deleted");
    }
}


