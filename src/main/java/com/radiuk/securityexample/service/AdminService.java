package com.radiuk.securityexample.service;

import com.radiuk.securityexample.model.User;

import java.util.List;

public interface AdminService {

    List<User> getUsers();

    void makeAdmin(Long id);

    void deleteById(Long id);
}
