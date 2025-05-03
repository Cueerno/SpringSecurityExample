package com.radiuk.securityexample.service.impl;

import com.radiuk.securityexample.security.UserDetailsImpl;
import com.radiuk.securityexample.service.RedisCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final RedisCacheService redisCacheService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new UserDetailsImpl(redisCacheService.cacheUserByUsername(username));
    }
}
