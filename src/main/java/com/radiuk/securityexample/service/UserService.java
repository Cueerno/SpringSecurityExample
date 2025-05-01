package com.radiuk.securityexample.service;

import com.radiuk.securityexample.dto.UserRegistrationDTO;
import com.radiuk.securityexample.model.User;
import com.radiuk.securityexample.security.UserDetailsImpl;

public interface UserService {

    User findById(Long id);

    void register(UserRegistrationDTO userRegistrationDTO);

    void update(UserRegistrationDTO userRegistrationDTO);

    void delete(String username);

    UserDetailsImpl getUserDetails();
}
