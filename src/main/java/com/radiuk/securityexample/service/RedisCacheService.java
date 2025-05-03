package com.radiuk.securityexample.service;

import com.radiuk.securityexample.model.User;
import com.radiuk.securityexample.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisCacheService {

    private final UserRepository userRepository;
    private final RedisTemplate<String, User> redisTemplate;

    public User cacheUserByUsername(String username) {
        User cachedUser = redisTemplate.opsForValue().get(username);

        if (cachedUser != null) {
            return cachedUser;
        }

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        redisTemplate.opsForValue().set(username, user, 3, TimeUnit.MINUTES);

        return user;
    }
}
