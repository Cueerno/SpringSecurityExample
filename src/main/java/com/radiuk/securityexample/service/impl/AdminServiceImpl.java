package com.radiuk.securityexample.service.impl;

import com.radiuk.securityexample.exception.UserNotFoundException;
import com.radiuk.securityexample.model.Role;
import com.radiuk.securityexample.model.User;
import com.radiuk.securityexample.repository.UserRepository;
import com.radiuk.securityexample.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public void makeAdmin(Long id) {
        User userToUpdated = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        userToUpdated.setRole(Role.ROLE_ADMIN);
        userRepository.save(userToUpdated);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}

