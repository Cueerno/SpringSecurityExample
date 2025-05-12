package com.radiuk.securityexample.service.impl;

import com.radiuk.securityexample.config.UserMapperConfig;
import com.radiuk.securityexample.dto.UserAuthDTO;
import com.radiuk.securityexample.dto.UserRegistrationDTO;
import com.radiuk.securityexample.exception.UserNotCreatedException;
import com.radiuk.securityexample.exception.UserNotFoundException;
import com.radiuk.securityexample.model.Role;
import com.radiuk.securityexample.model.User;
import com.radiuk.securityexample.repository.UserRepository;
import com.radiuk.securityexample.security.JwtCore;
import com.radiuk.securityexample.security.UserDetailsImpl;
import com.radiuk.securityexample.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtCore jwtCore;
    private final UserDetailsService userDetailsService;

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers(Integer pageNo, Integer pageSize, String sort) {
        Sort parseSort = (sort == null || sort.trim().isEmpty()) ? null : parseSort(sort);

        if (pageNo == null || pageSize == null) {
            return (parseSort == null) ? userRepository.findAll() : userRepository.findAll(parseSort);
        }

        Pageable pageable = (parseSort == null) ? PageRequest.of(pageNo, pageSize) : PageRequest.of(pageNo, pageSize, parseSort);
        return userRepository.findAll(pageable).getContent();
    }

    private Sort parseSort(String sort) {
        String[] sortParams = sort.split(",");
        return sortParams.length == 2
                ? Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0])
                : Sort.by(Sort.Direction.ASC, sortParams[0]);
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    @Transactional
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
    public void deleteByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.deleteByUsername(user.getUsername());
    }

    @Override
    public void deleteById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.deleteById(user.getId());
    }

    @Override
    public String getToken(UserAuthDTO userAuthDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userAuthDTO.getUsername(), userAuthDTO.getPassword());
        authenticationManager.authenticate(authenticationToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userAuthDTO.getUsername());
        return jwtCore.generateToken(userDetails);
    }

    @Override
    public UserDetailsImpl getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetailsImpl) authentication.getPrincipal();
    }
}
