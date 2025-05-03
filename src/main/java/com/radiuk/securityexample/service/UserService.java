package com.radiuk.securityexample.service;

import com.radiuk.securityexample.dto.UserAuthDTO;
import com.radiuk.securityexample.dto.UserRegistrationDTO;
import com.radiuk.securityexample.model.User;
import com.radiuk.securityexample.security.UserDetailsImpl;

import java.util.List;

public interface UserService {

    List<User> getAllUsers(Integer pageNo, Integer pageSize);

    User findById(Long id);

    void register(UserRegistrationDTO userRegistrationDTO);

    void update(UserRegistrationDTO userRegistrationDTO);

    void deleteByUsername(String username);

    void deleteById(Long id);

    String getToken(UserAuthDTO userAuthDTO);

    UserDetailsImpl getUserDetails();
}
