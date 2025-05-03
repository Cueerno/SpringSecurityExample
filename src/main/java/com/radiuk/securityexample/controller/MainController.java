package com.radiuk.securityexample.controller;

import com.radiuk.securityexample.model.User;
import com.radiuk.securityexample.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/main")
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;

    @GetMapping("/data")
    public ResponseEntity<User> getAllData() {
        return ResponseEntity.ok(userService.getUserDetails().getUser());
    }

    @GetMapping("/username")
    public ResponseEntity<String> getUsername() {
        return ResponseEntity.ok(userService.getUserDetails().getUsername());
    }
}
