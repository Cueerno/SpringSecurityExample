package com.radiuk.securityexample.service.impl;

import com.radiuk.securityexample.exception.UserNotFoundException;
import com.radiuk.securityexample.model.User;
import com.radiuk.securityexample.repository.UserRepository;
import com.radiuk.securityexample.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void makeAdmin(Long id) {
        User userToUpdated = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        userToUpdated.setRole(User.Role.ROLE_ADMIN);
        userRepository.save(userToUpdated);
    }
}

