package com.radiuk.securityexample.repository;

import com.radiuk.securityexample.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void userRepository_FindByUsername_CheckIsPresent() {
        User user = User.builder().username("username").build();

        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByUsername(user.getUsername());

        Assertions.assertThat(foundUser.isPresent()).isTrue();
    }

    @Test
    public void userRepository_FindByUsername_ReturnUser() {
        User user = User.builder().username("username").build();

        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByUsername(user.getUsername());

        Assertions.assertThat(foundUser).isNotNull();
    }

    @Test
    public void UserRepository_DeleteByUsername_ReturnUserIsEmpty() {
        User user = User.builder().username("username").build();

        userRepository.save(user);
        userRepository.deleteByUsername(user.getUsername());

        Optional<User> foundUser = userRepository.findByUsername(user.getUsername());

        Assertions.assertThat(foundUser).isEmpty();
    }


}