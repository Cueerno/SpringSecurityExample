package com.radiuk.securityexample.repository;

import com.radiuk.securityexample.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    void deleteByUsername(String username);

    boolean existsUserByUsername(String username);

    boolean existsUserByEmail(String email);

    boolean existsUserByPhone(String phone);

}
