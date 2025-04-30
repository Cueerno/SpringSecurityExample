package com.radiuk.securityexample.controller;

import com.radiuk.securityexample.model.User;
import com.radiuk.securityexample.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(adminService.getUsers());
    }

    @PostMapping("/make_admin/{id}")
    public ResponseEntity<?> makeAdmin(@PathVariable Long id) {
        adminService.makeAdmin(id);
        return ResponseEntity.status(HttpStatus.OK).body("User made");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        adminService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("User Deleted");
    }
}
