package com.radiuk.securityexample.service;

import com.radiuk.securityexample.dto.UserRegistrationDTO;
import com.radiuk.securityexample.model.User;

public interface UserService {

    User findById(Long id);

    void register(UserRegistrationDTO userRegistrationDTO);

    void update(UserRegistrationDTO userRegistrationDTO);

    void delete(String username);
}
