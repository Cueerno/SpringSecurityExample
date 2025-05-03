package com.radiuk.securityexample.controller;

import com.radiuk.securityexample.model.User;
import com.radiuk.securityexample.service.AdminService;
import com.radiuk.securityexample.service.UserService;
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
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(
            @RequestParam(value = "pageNo", required = false) Integer pageNo,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "sort", required = false) String sort) {
        return ResponseEntity.ok(userService.getAllUsers(pageNo, pageSize, sort));
    }

    @PostMapping("/make_admin/{id}")
    public ResponseEntity<?> makeAdmin(@PathVariable Long id) {
        adminService.makeAdmin(id);
        return ResponseEntity.status(HttpStatus.OK).body("User made");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("User Deleted");
    }
}
