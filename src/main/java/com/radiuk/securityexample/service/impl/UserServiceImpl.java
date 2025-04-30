package com.radiuk.securityexample.service.impl;

import com.radiuk.securityexample.config.UserMapperConfig;
import com.radiuk.securityexample.dto.UserRegistrationDTO;
import com.radiuk.securityexample.exception.UserNotCreatedException;
import com.radiuk.securityexample.exception.UserNotFoundException;
import com.radiuk.securityexample.model.Role;
import com.radiuk.securityexample.model.User;
import com.radiuk.securityexample.repository.UserRepository;
import com.radiuk.securityexample.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public void register(UserRegistrationDTO userRegistrationDTO) {
        User user = UserMapperConfig.INSTANCE.UserRegistrationDTOToUser(userRegistrationDTO);

        if (userRepository.existsUserByUsername(user.getUsername())) {
            throw new UserNotCreatedException("User with this username already exists");
        }

        if (userRepository.existsUserByEmail(user.getEmail())) {
            throw new UserNotCreatedException("User with this email already exists");
        }

        if (userRepository.existsUserByPhone(user.getPhone())) {
            throw new UserNotCreatedException("User with this phone already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_USER);
        user.setCreatedAt(OffsetDateTime.now());
        user.setUpdatedAt(OffsetDateTime.now());

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void update(UserRegistrationDTO userRegistrationDTO) {
        User updatedUser = UserMapperConfig.INSTANCE.UserRegistrationDTOToUser(userRegistrationDTO);
        User userToUpdated = userRepository.findByUsername(updatedUser.getUsername()).orElseThrow(() -> new UserNotFoundException("User not found"));

        updatedUser.setId(userToUpdated.getId());
        updatedUser.setPassword(userToUpdated.getPassword());
        updatedUser.setRole(userToUpdated.getRole());
        updatedUser.setCreatedAt(userToUpdated.getCreatedAt());
        updatedUser.setUpdatedAt(OffsetDateTime.now());

        userRepository.save(updatedUser);
    }

    @Override
    @Transactional
    public void delete(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.deleteByUsername(user.getUsername());
    }
}
